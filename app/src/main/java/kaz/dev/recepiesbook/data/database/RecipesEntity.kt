package kaz.dev.recepiesbook.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kaz.dev.recepiesbook.Constance.Companion.RECIPES_TABLE
import kaz.dev.recepiesbook.models.FoodRecipes

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipes: FoodRecipes
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}