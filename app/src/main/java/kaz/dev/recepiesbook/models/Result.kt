package kaz.dev.recepiesbook.models

data class Result(
    val aggregateLikes: Int?,
    val cheap: Boolean?,
    val dairyFree: Boolean?,
    val extendedIngredients: List<ExtendedIngredient?>?,
    val glutenFree: Boolean?,
    val id: Int?,
    val image: String?,
    val readyInMinutes: Int?,
    val sourceName: String?,
    val summary: String?,
    val title: String?,
    val unusedIngredients: List<Any?>?,
    val usedIngredientCount: Int?,
    val usedIngredients: List<Any?>?,
    val vegan: Boolean?,
    val vegetarian: Boolean?,
    val veryHealthy: Boolean?,
)