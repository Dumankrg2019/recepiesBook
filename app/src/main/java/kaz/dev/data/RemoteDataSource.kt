package kaz.dev.data

import kaz.dev.data.network.FoodRecipesApi
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