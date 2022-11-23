package com.keepcoding.dragonball.characters.battle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.characters.list.ListFragment
import com.keepcoding.dragonball.databinding.FragmentBattleBinding
import com.keepcoding.dragonball.model.DbCharacter


class BattleFragment(private val character:DbCharacter) : Fragment() {

    companion object{
        fun newInstance() = BattleFragment(DbCharacter("D13A40E5-4418-4223-9CE6-D2F9A28EBE94","Goku","","https://cdn.alfabetajuega.com/alfabetajuega/2020/12/goku1.jpg?width=300",true))
    }

    private lateinit var binding:FragmentBattleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBattleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        Toast.makeText(context,"Seleccionado ${character.name}",Toast.LENGTH_SHORT).show()
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, ListFragment())
            transaction.commit()
        }
    }
}