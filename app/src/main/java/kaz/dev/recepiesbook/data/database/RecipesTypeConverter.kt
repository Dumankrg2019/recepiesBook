package kaz.dev.recepiesbook.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kaz.dev.recepiesbook.models.FoodRecipes

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipes: FoodRecipes): String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipes {
        val listType = object: TypeToken<FoodRecipes>() {}.type
        return gson.fromJson(data, listType)
    }
}