package com.keepcoding.dragonball.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class LoginViewModel : ViewModel() {

    val BASE_URL = "https://dragonball.keepcoding.education/api/"

    val stateLiveData: MutableLiveData<LoginActivityState> by lazy {
        MutableLiveData<LoginActivityState>()
    }

    fun login(email: String, password: String) {
        setValueOnMainThreadToError(LoginActivityState.Loading)
        val client = OkHttpClient()
        val url = "${BASE_URL}auth/login"

/*      val loginString = "$email:$password"
        val base64LoginString = Base64.getEncoder().encodeToString(loginString.toByteArray())
        Log.d("Encoded String", base64LoginString)
                val reqBody = ByteArray(0).toRequestBody(null, 0, 0)*/
        val credentials = Credentials.basic(email, password)
        val formBody = FormBody.Builder() //Esto hace que la request sea tipo POST
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", credentials)
            .post(formBody)
            .build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setValueOnMainThreadToError(LoginActivityState.Error(e.message.toString()))
                Log.d("Login Error", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code != 200) {
                    Log.d("Error", response.message)
                    setValueOnMainThreadToError(LoginActivityState.Error(response.message))
                } else {
                    val token = response.body?.string()
                    token?.let {
                        Log.d("Token", it)
                        setValueOnMainThreadToError(LoginActivityState.SuccessLogin(it))
                    }
                }
            }

        })
    }

    fun setValueOnMainThreadToError(value: LoginActivityState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class LoginActivityState {
        data class SuccessLogin(val token: String) : LoginActivityState()
        data class Error(val message: String) : LoginActivityState()
        object Loading : LoginActivityState()
    }
}