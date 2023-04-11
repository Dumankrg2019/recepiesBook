package kaz.dev.recepiesbook.data

import kaz.dev.recepiesbook.data.network.FoodRecipesApi
import kaz.dev.recepiesbook.models.FoodRecipes
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

   suspend fun getRecipes(queries: Map<String, String>)
    : Response<FoodRecipes> {
        return foodRecipesApi.getRecipes(queries)
    }
}