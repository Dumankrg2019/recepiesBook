package kaz.dev.recepiesbook.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kaz.dev.recepiesbook.Constance
import kaz.dev.recepiesbook.Constance.Companion.API_KEY
import kaz.dev.recepiesbook.Constance.Companion.QUERY_ADD_RECIPE_INFORMATION
import kaz.dev.recepiesbook.Constance.Companion.QUERY_API_KEY
import kaz.dev.recepiesbook.Constance.Companion.QUERY_DIET
import kaz.dev.recepiesbook.Constance.Companion.QUERY_FILL_INGREDIENTS
import kaz.dev.recepiesbook.Constance.Companion.QUERY_NUMBER
import kaz.dev.recepiesbook.Constance.Companion.QUERY_TYPE

class RecipesViewModel(application: Application): AndroidViewModel(application) {

     fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "main course"
        queries[QUERY_DIET] = "gluten free"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}