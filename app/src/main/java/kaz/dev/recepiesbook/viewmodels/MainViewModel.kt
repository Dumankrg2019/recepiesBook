package kaz.dev.recepiesbook.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kaz.dev.recepiesbook.data.Repository
import kaz.dev.recepiesbook.data.database.RecipesEntity
import kaz.dev.recepiesbook.models.FoodRecipes
import kaz.dev.recepiesbook.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application)
    : AndroidViewModel(application) {

    /** ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }
    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.M)
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("NO Internet Connection!")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipes) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not founded.")
            }
            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }
            else -> {
               return NetworkResult.Error(response.message())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
        private fun hasInternetConnection(): Boolean {
            val  connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)


                return when {
                    capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

        }
}