package com.keepcoding.dragonball.characters.battle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.characters.CharactersActivity
import com.keepcoding.dragonball.characters.CharactersViewModel
import com.keepcoding.dragonball.characters.list.ListFragment
import com.keepcoding.dragonball.databinding.FragmentBattleBinding
import com.keepcoding.dragonball.model.DbCharacter


class BattleFragment(private val character:DbCharacter) : Fragment() {

    companion object{
        fun newInstance() = BattleFragment(DbCharacter("D13A40E5-4418-4223-9CE6-D2F9A28EBE94","Goku","","https://cdn.alfabetajuega.com/alfabetajuega/2020/12/goku1.jpg?width=300",true))
    }

    private lateinit var binding:FragmentBattleBinding
    private val viewModel:CharactersViewModel by activityViewModels()

    var lifeText:String = ""

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

        setObservers()
        setListeners()

        lifeText = getString(R.string.life)

        binding.lySelectedCharacter.apply {
            tvName.text = character.name
            tvHealth.text ="$lifeText ${character.currentLife}"
            Glide.with(requireContext())
                .load(character.photo)
                .placeholder(R.drawable.balls_image)
                .into(ivCharacter)

            pbHealth.progress = character.currentLife
        }

        setRandomCharacter()

    }

    private fun setRandomCharacter(){

        viewModel.getRandomCharacter()
        binding.lyRandomCharacter.apply {
            cvCharacter.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))
        }
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, ListFragment())
            transaction.commit()
        }
    }

    private fun setObservers(){
        viewModel.randomCharacter.observe(requireActivity()){
            binding.lyRandomCharacter.apply {
                tvName.text = it?.name
                tvHealth.text = "$lifeText ${it?.currentLife.toString()}"

                context?.let { it1 ->
                    Glide.with(it1)
                        .load(it?.photo)
                        .placeholder(R.drawable.balls_image)
                        .into(ivCharacter)
                }

                pbHealth.progress = it?.currentLife ?: 100
            }
        }
    }
}