package kaz.dev.recepiesbook.models

data class FoodRecipes(
    val number: Int?,
    val offset: Int?,
    val results: List<Result?>?,
    val totalResults: Int?
)