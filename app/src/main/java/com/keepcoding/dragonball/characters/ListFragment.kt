package com.keepcoding.dragonball.characters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.FragmentListBinding
import com.keepcoding.dragonball.login.LoginViewModel

class ListFragment : Fragment() {


    private lateinit var binding:FragmentListBinding
    private val viewModel: CharactersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(requireActivity().getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE)) {
            val token = getString("Token", "")
            print("Token Activity $token")
            token?.let {
                viewModel.getCharacters(it)
            }
        }

        setListeners()
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container,BattleFragment())
            transaction.commit()
        }
    }

}