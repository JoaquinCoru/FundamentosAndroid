package com.keepcoding.dragonball.ui.characters.list


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
import com.keepcoding.dragonball.ui.characters.battle.BattleFragment
import com.keepcoding.dragonball.ui.characters.CharactersViewModel
import com.keepcoding.dragonball.databinding.FragmentListBinding
import com.keepcoding.dragonball.model.DbCharacter
import kotlin.system.exitProcess

class ListFragment : Fragment(), ListAdapterCallback {

    private lateinit var binding:FragmentListBinding
    private val viewModel: CharactersViewModel by activityViewModels()
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

        viewModel.checkSurvivors(requireContext())
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

        viewModel.charactersAlive.observe(requireActivity()){
            if (it <= 1){
                if (viewModel.getSurvivor()?.name?.isBlank() == true){
                    binding.lyExitWindow.tvWinner.text = context?.getString(R.string.game_over) ?:"Juego terminado"
                }else{
                    binding.lyExitWindow.tvWinner.text = "${context?.getString(R.string.winner)} ${viewModel.getSurvivor()?.name}"
                }
                binding.btnReset.isEnabled = false
                binding.lyExitWindow.clExitWindow.visibility = View.VISIBLE
            }

            else
                binding.lyExitWindow.clExitWindow.visibility = View.INVISIBLE
        }

    }

    private fun setListeners() {
        binding.btnSelect.setOnClickListener {

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, BattleFragment())
            transaction.commit()
        }

        binding.lyExitWindow.exitButton.setOnClickListener {
            exitProcess(0)
        }

        binding.btnReset.setOnClickListener {
            viewModel.reset()
        }

        binding.lyExitWindow.resetButton.setOnClickListener {
            binding.lyExitWindow.clExitWindow.visibility = View.INVISIBLE
            viewModel.reset()
            binding.btnReset.isEnabled = true
        }
    }

    override fun onItemClicked(item: DbCharacter) {
        viewModel.charactersAlive.observe(requireActivity()){ survivorsNumber ->
            if (survivorsNumber <= 1){
                binding.btnSelect.isEnabled = false

            }else{
                binding.btnSelect.isEnabled = true
                viewModel.setSelectedCharacter(item)
            }
        }

    }


    private fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

}