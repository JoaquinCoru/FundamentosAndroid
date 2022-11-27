package com.keepcoding.dragonball.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.ui.characters.CharactersActivity
import com.keepcoding.dragonball.databinding.LoginConstraintBinding

class LoginActivity : AppCompatActivity() {

//    var tiempoInicio = 0L
    private lateinit var binding:LoginConstraintBinding
    private val viewModel: LoginViewModel by viewModels()

    private var loginEnabled:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginConstraintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewModel.stateLiveData.observe(this){

            when(it){
                is LoginViewModel.LoginActivityState.SuccessLogin -> {
                    saveToken(it.token)
                    binding.pbLoading?.visibility = View.INVISIBLE
                    binding.etName.setText("")
                    binding.etPassword.setText("")
                    CharactersActivity.launch(this)
                }
                is LoginViewModel.LoginActivityState.Loading -> {
                    binding.pbLoading?.visibility = View.VISIBLE
                }
                is LoginViewModel.LoginActivityState.Error -> {
                    Log.d(LoginActivity::class.java.simpleName,"Error: ${it.message}")
                    showToast("Error: ${it.message}")
                    binding.pbLoading?.visibility = View.INVISIBLE
                }
            }
        }
    }


    private fun setListeners(){
        with(binding){
            etName.doAfterTextChanged {
                loginEnabled = etName.text.isNotEmpty() && etPassword.text.isNotEmpty()
            }
            etPassword.doAfterTextChanged {
                loginEnabled = etName.text.isNotEmpty() && etPassword.text.isNotEmpty()
            }

            btnLogin.setOnClickListener {
                if (!loginEnabled) showToast(getString(R.string.missing_fields_message))
                else {
                    viewModel.login(etName.text.toString(),etPassword.text.toString())
                }
            }
        }
    }


    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun saveToken(token:String){

        with(getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE)){
            val editPreferences = edit()
            editPreferences.putString("Token",token)
            editPreferences.apply()
            println("Token guardado ${token}")
        }
    }

/*    override fun onStart() {
        super.onStart()
        tiempoInicio = System.currentTimeMillis()
    }

    override fun onStop() {
        val tiempoFin = System.currentTimeMillis()
        val tiempoEnLaApp = tiempoFin -tiempoInicio

        with(getPreferences(Context.MODE_PRIVATE)){
            val tiempoAnterior = getLong("Tiempo",0L)
            val preferenciasEditables = edit()
            preferenciasEditables.putLong("Tiempo",tiempoEnLaApp + tiempoAnterior)
            preferenciasEditables.apply()
        }

        super.onStop()
    }*/
}