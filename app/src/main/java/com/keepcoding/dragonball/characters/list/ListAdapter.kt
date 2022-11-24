package com.keepcoding.dragonball.characters.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.FragmentListBinding
import com.keepcoding.dragonball.databinding.ListItemBinding
import com.keepcoding.dragonball.model.DbCharacter

interface ListAdapterCallback {
    fun onItemClicked(item: DbCharacter)
}

class ListAdapter(var callback: ListAdapterCallback) : Adapter<ListAdapter.ListViewHolder>() {

    private var items = listOf<DbCharacter>()
    private var selectedPosition = -1

    inner class ListViewHolder(val binding: ListItemBinding) : ViewHolder(binding.root) {
        fun bind(item: DbCharacter, position: Int) {
            binding.tvName.text = item.name
            binding.tvHealth.text ="Vida: ${item.currentLife.toString()}"

            Glide.with(binding.ivCharacter.context)
                .load(item.photo)
                .placeholder(R.drawable.balls_image)
                .into(binding.ivCharacter)

            if (position == selectedPosition) {
                println("Entra en posición")
                binding.cardView.strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.orange)
                }else {
                binding.cardView.strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.white)
            }

            binding.root.setOnClickListener {
//                Toast.makeText(binding.root.context, item.name, Toast.LENGTH_SHORT).show()
                selectedPosition = position
                notifyDataSetChanged()
                callback.onItemClicked(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(list: List<DbCharacter>) {
        items = list
        notifyDataSetChanged()
    }
}