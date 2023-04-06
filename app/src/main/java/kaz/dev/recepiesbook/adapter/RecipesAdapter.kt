package kaz.dev.recepiesbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kaz.dev.recepiesbook.databinding.ItemRowRecipesBinding
import kaz.dev.recepiesbook.models.FoodRecipes
import kaz.dev.recepiesbook.models.Result
import kaz.dev.recepiesbook.util.RecipesDiffUtil

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    class MyViewHolder(private val binding: ItemRowRecipesBinding)
        : RecyclerView.ViewHolder(binding.root){

            fun bind(result: Result) {
                binding.result = result
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRowRecipesBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipes) {
        val recipesDiffUtil = newData.results?.let { RecipesDiffUtil(recipes, it as List<Result>) }
        val diffUtilResult = recipesDiffUtil?.let { DiffUtil.calculateDiff(it) }
        recipes = newData.results as List<Result>
        diffUtilResult?.dispatchUpdatesTo(this)
    }
}