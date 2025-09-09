package nutrition.util

import nutrition.model.{Food, NutritionalItem, Recipe}

import scala.collection.mutable.ListBuffer

/**
 * Generic utility class demonstrating generic programming concepts
 * Provides type-safe operations on collections of nutritional items
 * Supports UN Goal 2 by enabling advanced nutritional analysis
 */
class NutritionCalculator[T <: NutritionalItem] {

  /**
   * Generic method: Calculate total nutrition for a collection of items
   * @param items Collection of any type that extends NutritionalItem
   * @param amounts Corresponding amounts for each item
   * @return Combined nutritional information
   */
  def calculateTotalNutrition(items: List[T], amounts: List[Double] = List()): Map[String, Double] = {
    require(amounts.isEmpty || items.length == amounts.length, "Items and amounts must have same length")

    val totalNutrients = scala.collection.mutable.Map[String, Double]()

    items.zipWithIndex.foreach { case (item, index) =>
      val amount = if (amounts.isEmpty) 1.0 else amounts(index)
      val itemNutrients = item.getNutritionalInfo(amount)

      itemNutrients.foreach { case (nutrient, value) =>
        totalNutrients(nutrient) = totalNutrients.getOrElse(nutrient, 0.0) + value
      }
    }

    totalNutrients.toMap
  }

  /**
   * Generic method: Find items that match specific criteria
   * @param items Collection to search
   * @param predicate Function that defines the criteria
   * @return Filtered collection
   */
  def findItemsMatching(items: List[T], predicate: T => Boolean): List[T] = {
    items.filter(predicate)
  }

  /**
   * Generic method: Sort items by nutrition score
   * @param items Collection to sort
   * @param ascending Sort order (default: descending for best first)
   * @return Sorted collection
   */
  def sortByNutritionScore(items: List[T], ascending: Boolean = false): List[T] = {
    if (ascending) items.sortBy(_.getNutritionScore)
    else items.sortBy(_.getNutritionScore).reverse
  }

  /**
   * Generic method: Group items by category
   * @param items Collection to group
   * @return Map of category to items
   */
  def groupByCategory(items: List[T]): Map[String, List[T]] = {
    items.groupBy(_.getCategory)
  }

  /**
   * Generic method: Calculate average nutrition score
   * @param items Collection to analyze
   * @return Average score
   */
  def calculateAverageScore(items: List[T]): Double = {
    if (items.isEmpty) 0.0
    else items.map(_.getNutritionScore).sum / items.length
  }

  /**
   * Generic method: Find top N items by score
   * @param items Collection to analyze
   * @param n Number of top items to return
   * @return Top N items
   */
  def getTopNItems(items: List[T], n: Int): List[T] = {
    sortByNutritionScore(items).take(n)
  }

  /**
   * Generic method: Calculate nutritional diversity
   * Measures how varied the nutritional profiles are
   * @param items Collection to analyze
   * @return Diversity score (higher = more diverse)
   */
  def calculateNutritionalDiversity(items: List[T]): Double = {
    if (items.isEmpty) return 0.0

    val categories = items.map(_.getCategory).distinct
    val categoryCount = categories.length.toDouble
    val totalItems = items.length.toDouble

    // Shannon diversity index adapted for nutrition
    val diversity = categories.map { category =>
      val proportion = items.count(_.getCategory == category) / totalItems
      if (proportion > 0) -proportion * math.log(proportion) else 0.0
    }.sum

    diversity * 10 // Scale for readability
  }
}

/**
 * Specialized nutrition calculator for Food items
 * Demonstrates inheritance of generic classes
 */
class FoodNutritionCalculator extends NutritionCalculator[Food] {

  /**
   * Food-specific method: Find foods suitable for specific dietary needs
   * @param foods Collection of foods
   * @param dietaryNeed Specific dietary requirement
   * @return Foods matching the dietary need
   */
  def findFoodsForDiet(foods: List[Food], dietaryNeed: String): List[Food] = {
    findItemsMatching(foods, _.isSuitableFor(dietaryNeed))
  }

  /**
   * Food-specific method: Calculate macronutrient balance
   * @param foods Collection of foods
   * @param amounts Corresponding amounts
   * @return Macronutrient percentages
   */
  def calculateMacroBalance(foods: List[Food], amounts: List[Double] = List()): Map[String, Double] = {
    val totalNutrition = calculateTotalNutrition(foods, amounts)
    val totalCalories = totalNutrition.getOrElse("Calories", 0.0)

    if (totalCalories == 0) return Map("Protein" -> 0.0, "Carbohydrates" -> 0.0, "Fats" -> 0.0)

    Map(
      "Protein" -> (totalNutrition.getOrElse("Protein", 0.0) * 4 / totalCalories * 100),
      "Carbohydrates" -> (totalNutrition.getOrElse("Carbohydrates", 0.0) * 4 / totalCalories * 100),
      "Fats" -> (totalNutrition.getOrElse("Fats", 0.0) * 9 / totalCalories * 100)
    )
  }

  /**
   * Food-specific method: Find complementary foods
   * Foods that together provide balanced nutrition
   * @param primaryFood The main food item
   * @param availableFoods Pool of foods to choose from
   * @return Foods that complement the primary food
   */
  def findComplementaryFoods(primaryFood: Food, availableFoods: List[Food]): List[Food] = {
    val primaryNutrients = primaryFood.getNutritionalInfo()
    val primaryCategory = primaryFood.getCategory

    availableFoods.filter { food =>
      val foodCategory = food.getCategory
      val foodNutrients = food.getNutritionalInfo()

      // Find foods that complement nutritionally
      primaryCategory match {
        case "High-Protein" =>
          foodCategory == "Carbohydrate-Rich" || foodNutrients.getOrElse("Fiber", 0.0) > 3.0
        case "Carbohydrate-Rich" =>
          foodCategory == "High-Protein" || foodCategory == "High-Fat"
        case "High-Fat" =>
          foodCategory == "High-Fiber" || foodNutrients.getOrElse("Protein", 0.0) > 10.0
        case "Low-Calorie" =>
          foodNutrients.getOrElse("Protein", 0.0) > 15.0 || foodNutrients.getOrElse("Fats", 0.0) > 10.0
        case _ =>
          food.getNutritionScore > primaryFood.getNutritionScore
      }
    }
  }
}

/**
 * Specialized nutrition calculator for Recipe items
 * Demonstrates inheritance of generic classes
 */
class RecipeNutritionCalculator extends NutritionCalculator[Recipe] {

  /**
   * Recipe-specific method: Find recipes by cooking time
   * @param recipes Collection of recipes
   * @param maxTime Maximum cooking time in minutes
   * @return Recipes that can be made within the time limit
   */
  def findQuickRecipes(recipes: List[Recipe], maxTime: Int): List[Recipe] = {
    findItemsMatching(recipes, _.getEstimatedCookingTime <= maxTime)
  }

  /**
   * Recipe-specific method: Find recipes by difficulty
   * @param recipes Collection of recipes
   * @param difficulty Desired difficulty level
   * @return Recipes matching the difficulty
   */
  def findRecipesByDifficulty(recipes: List[Recipe], difficulty: String): List[Recipe] = {
    findItemsMatching(recipes, _.getDifficultyLevel == difficulty)
  }

  /**
   * Recipe-specific method: Calculate meal prep efficiency
   * Based on nutrition score, cooking time, and storage suitability
   * @param recipes Collection of recipes
   * @return Efficiency scores for meal prep planning
   */
  def calculateMealPrepEfficiency(recipes: List[Recipe]): Map[Recipe, Double] = {
    recipes.map { recipe =>
      val nutritionScore = recipe.getNutritionScore
      val timeEfficiency = math.max(0, 60 - recipe.getEstimatedCookingTime) / 60.0 * 100
      val storageBonus = if (recipe.isSuitableForMealPrep) 20.0 else 0.0

      val efficiency = (nutritionScore + timeEfficiency + storageBonus) / 3.0
      recipe -> efficiency
    }.toMap
  }
}

/**
 * Companion object with factory methods and utilities
 * Demonstrates object-oriented design patterns
 */
object NutritionCalculator {

  /**
   * Factory method: Create calculator for specific type
   */
  def forFoods(): FoodNutritionCalculator = new FoodNutritionCalculator()

  def forRecipes(): RecipeNutritionCalculator = new RecipeNutritionCalculator()

  def forItems[T <: NutritionalItem](): NutritionCalculator[T] = new NutritionCalculator[T]()

  /**
   * Utility method: Compare two nutritional items
   * @param item1 First item
   * @param item2 Second item
   * @return Comparison result (-1, 0, 1)
   */
  def compareNutritionalValue[T <: NutritionalItem](item1: T, item2: T): Int = {
    val score1 = item1.getNutritionScore
    val score2 = item2.getNutritionScore

    if (score1 < score2) -1
    else if (score1 > score2) 1
    else 0
  }

  /**
   * Utility method: Calculate food security impact
   * Higher scores indicate better contribution to food security goals
   * @param items Collection of nutritional items
   * @return Impact score
   */
  def calculateFoodSecurityImpact[T <: NutritionalItem](items: List[T]): Double = {
    if (items.isEmpty) return 0.0

    val avgNutritionScore = items.map(_.getNutritionScore).sum / items.length
    val diversityScore = new NutritionCalculator[T]().calculateNutritionalDiversity(items)
    val securityContribution = items.count(_.contributesToFoodSecurity).toDouble / items.length * 100

    (avgNutritionScore + diversityScore + securityContribution) / 3.0
  }
}