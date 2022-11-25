package com.keepcoding.dragonball.characters.list


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.characters.battle.BattleFragment
import com.keepcoding.dragonball.characters.CharactersViewModel
import com.keepcoding.dragonball.databinding.FragmentListBinding
import com.keepcoding.dragonball.model.DbCharacter

class ListFragment : Fragment(), ListAdapterCallback {

    private lateinit var binding:FragmentListBinding
    private val viewModel:CharactersViewModel by activityViewModels()
    private val adapter = ListAdapter(this)

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

        setObservers()
        setListeners()
    }

    private fun setObservers(){
        viewModel.stateLiveData.observe(requireActivity()){

            when(it){
                is CharactersViewModel.FragmentListState.SuccessCharacters -> {
                    Log.d(ListFragment::class.java.simpleName, "Success: $it")
                    binding.progressBar.visibility = View.INVISIBLE
                    adapter.updateList(it.characterList)
                    binding.rvCharacters.adapter = adapter
                    binding.rvCharacters.layoutManager = GridLayoutManager(context,2)
                }
                is CharactersViewModel.FragmentListState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d(ListFragment::class.java.simpleName, "Loading")
                }

                is CharactersViewModel.FragmentListState.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    showToast("Error: ${it.message}")
                    Log.d(ListFragment::class.java.simpleName, "Error: ${it.message}")
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnSelect.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, BattleFragment())
            transaction.commit()
        }
    }

    override fun onItemClicked(item: DbCharacter) {
        binding.btnSelect.isEnabled = true
        viewModel.setSelectedCharacter(item)
    }

/*    private fun defaultCharacter():DbCharacter{
        return DbCharacter("D13A40E5-4418-4223-9CE6-D2F9A28EBE94","Goku","","https://cdn.alfabetajuega.com/alfabetajuega/2020/12/goku1.jpg?width=300",true)
    }*/

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

}