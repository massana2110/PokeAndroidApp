package com.massana2110.pokeandroid.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.massana2110.pokeandroid.databinding.ItemPokemonCardBinding
import com.massana2110.pokeandroid.databinding.ItemPokemonTypeChipBinding
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.squareup.picasso.Picasso

class PokemonListAdapter(
    private var pokemonList: List<PokemonItemModel>
) : RecyclerView.Adapter<PokemonListAdapter.PokemonItemViewHolder>() {

    fun updateList(newList: List<PokemonItemModel>) {
        val diffCallback = PokemonListDiffUtil(pokemonList, newList)
        val result = DiffUtil.calculateDiff(diffCallback)
        pokemonList = newList
        result.dispatchUpdatesTo(this)
    }

    inner class PokemonItemViewHolder(
        private val binding: ItemPokemonCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PokemonItemModel) {
            binding.pokemonIdTxtView.text = "#${item.pokemonId}"
            binding.pokemonNameTxtView.text = item.pokemonName

            binding.root.setCardBackgroundColor(Color.parseColor(item.pokemonTypes[0].colorHex))

            if (item.pokemonSprite.isNotBlank()) {
                Picasso.get().load(item.pokemonSprite)
                    .into(binding.pokemonSpriteImgView)
            }

            binding.linearLayoutTypes.removeAllViews()
            item.pokemonTypes.forEach { type ->
                val typeViewBinding = ItemPokemonTypeChipBinding.inflate(
                    LayoutInflater.from(binding.linearLayoutTypes.context),
                    binding.linearLayoutTypes,
                    false
                )

                typeViewBinding.typePokemonImageView.setImageResource(type.iconDrawableResId)
                typeViewBinding.typePokemonTxtView.text = type.displayName
                binding.linearLayoutTypes.addView(typeViewBinding.root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonItemViewHolder {
        val binding = ItemPokemonCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PokemonItemViewHolder(binding)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: PokemonItemViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

}

/**
 * Util class for updating recycler view items
 * efficiently, comparing items between old list with the new one
 * and calculate which ones are different to notify the adapter that
 * data has changed
 */
class PokemonListDiffUtil(
    private val oldList: List<PokemonItemModel>,
    private val newList: List<PokemonItemModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].pokemonId == newList[newItemPosition].pokemonId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}