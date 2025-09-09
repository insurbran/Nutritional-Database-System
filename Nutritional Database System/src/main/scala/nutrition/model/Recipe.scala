package nutrition.model

import scalafx.beans.property.{StringProperty, IntegerProperty}
import scalafx.collections.ObservableBuffer
import scala.collection.mutable.ListBuffer

/**
 * Recipe class demonstrating composition and aggregation
 * Extends NutritionalItem to show polymorphism
 * Represents a collection of foods combined to create a meal
 */
class Recipe(_name: String, _servings: Int = 1) extends NutritionalItem {

  // Observable properties
  val name: StringProperty = StringProperty(_name)
  val servings: IntegerProperty = IntegerProperty(_servings)

  // Composition: Recipe HAS ingredients (aggregation relationship)
  private val ingredients: ListBuffer[(Food, Double)] = ListBuffer()

  // Observable buffer for GUI binding
  val ingredientsObservable: ObservableBuffer[(Food, Double)] = ObservableBuffer()

  /**
   * Add an ingredient to the recipe
   * @param food The food item to add
   * @param amount Amount in grams
   */
  def addIngredient(food: Food, amount: Double): Unit = {
    require(amount > 0, "Amount must be positive")
    val ingredient = (food, amount)
    ingredients += ingredient
    ingredientsObservable += ingredient
  }

  /**
   * Remove an ingredient from the recipe
   * @param index Index of ingredient to remove
   */
  def removeIngredient(index: Int): Unit = {
    if (index >= 0 && index < ingredients.size) {
      ingredients.remove(index)
      ingredientsObservable.remove(index)
    }
  }

  /**
   * Get all ingredients as a read-only list
   */
  def getIngredients: List[(Food, Double)] = ingredients.toList

  /**
   * Implementation of abstract method from NutritionalItem
   * Polymorphic behavior - calculates nutrition by combining all ingredients
   * @param amount Multiplier for the recipe (e.g., 2.0 for double recipe)
   */
  override def getNutritionalInfo(amount: Double = 1.0): Map[String, Double] = {
    val totalNutrients = scala.collection.mutable.Map[String, Double]()

    // Aggregate nutrition from all ingredients
    ingredients.foreach { case (food, ingredientAmount) =>
      val foodNutrients = food.getNutritionalInfo(ingredientAmount)
      foodNutrients.foreach { case (nutrient, value) =>
        totalNutrients(nutrient) = totalNutrients.getOrElse(nutrient, 0.0) + (value * amount)
      }
    }

    // Divide by number of servings to get per-serving nutrition
    totalNutrients.map { case (nutrient, value) =>
      nutrient -> (value / servings.value)
    }.toMap
  }

  /**
   * Implementation of abstract method from NutritionalItem
   * Recipe-specific nutrition scoring
   */
  override def getNutritionScore: Double = {
    if (ingredients.isEmpty) return 0.0

    // Average the nutrition scores of all ingredients, weighted by amount
    val totalWeight = ingredients.map(_._2).sum
    if (totalWeight == 0) return 0.0

    val weightedScore = ingredients.map { case (food, amount) =>
      food.getNutritionScore * (amount / totalWeight)
    }.sum

    // Bonus for recipe complexity (more ingredients = potentially more balanced)
    val complexityBonus = math.min(ingredients.size * 0.5, 5.0)
    weightedScore + complexityBonus
  }

  /**
   * Implementation of abstract method from NutritionalItem
   */
  override def getCategory: String = {
    if (ingredients.isEmpty) return "Empty Recipe"

    val nutrients = getNutritionalInfo()
    val totalCalories = nutrients.getOrElse("Calories", 0.0)

    if (totalCalories < 200) "Light Meal"
    else if (totalCalories > 600) "Hearty Meal"
    else if (ingredients.exists(_._1.getCategory == "High-Protein")) "Protein-Rich Meal"
    else if (ingredients.exists(_._1.getCategory == "High-Fat")) "High-Fat Meal"
    else "Balanced Meal"
  }

  /**
   * Recipe-specific method: Get cooking time estimate
   * Based on ingredient complexity
   */
  def getEstimatedCookingTime: Int = {
    val baseTime = 15 // Base 15 minutes
    val ingredientTime = ingredients.size * 5 // 5 minutes per ingredient
    val complexityTime = ingredients.count { case (food, _) =>
      food.getCategory == "High-Protein" && food.name.value.toLowerCase.contains("meat")
    } * 10 // Extra time for meat preparation

    baseTime + ingredientTime + complexityTime
  }

  /**
   * Recipe-specific method: Get difficulty level
   */
  def getDifficultyLevel: String = {
    val ingredientCount = ingredients.size
    val hasComplexIngredients = ingredients.exists { case (food, _) =>
      food.name.value.toLowerCase.contains("meat") ||
        food.name.value.toLowerCase.contains("fish")
    }

    if (ingredientCount <= 3 && !hasComplexIngredients) "Easy"
    else if (ingredientCount <= 6) "Medium"
    else "Hard"
  }

  /**
   * Recipe-specific method: Check if recipe is suitable for meal prep
   * Based on ingredient storage properties
   */
  def isSuitableForMealPrep: Boolean = {
    val hasPerishableIngredients = ingredients.exists { case (food, _) =>
      food.getStorageInfo.toLowerCase.contains("refrigerate immediately")
    }

    !hasPerishableIngredients && ingredients.size >= 2
  }

  /**
   * Recipe-specific method: Get cost estimate
   * Simple estimation based on ingredient categories
   */
  def getEstimatedCostCategory: String = {
    val expensiveIngredients = ingredients.count { case (food, amount) =>
      val category = food.getCategory
      (category == "High-Protein" && food.name.value.toLowerCase.contains("meat")) ||
        food.name.value.toLowerCase.contains("salmon") ||
        amount > 200.0 // Large amounts
    }

    if (expensiveIngredients == 0) "Budget-Friendly"
    else if (expensiveIngredients <= 2) "Moderate Cost"
    else "Higher Cost"
  }

  /**
   * Override isSuitableFor to include recipe-specific logic
   */
  override def isSuitableFor(dietaryNeed: String): Boolean = {
    val baseResult = super.isSuitableFor(dietaryNeed)

    dietaryNeed.toLowerCase match {
      case "meal-prep" => isSuitableForMealPrep
      case "quick-meal" => getEstimatedCookingTime <= 30
      case "budget" => getEstimatedCostCategory == "Budget-Friendly"
      case "complex-nutrition" => ingredients.size >= 4 && getNutritionScore > 20
      case _ => baseResult
    }
  }

  /**
   * Get recipe summary with cooking information
   */
  def getRecipeSummary: String = {
    val nutrients = getNutritionalInfo()
    s"${name.value} (${getCategory})\n" +
      f"Servings: ${servings.value}\n" +
      f"Calories per serving: ${nutrients.getOrElse("Calories", 0.0)}%.0f\n" +
      f"Cooking time: ${getEstimatedCookingTime} minutes\n" +
      f"Difficulty: ${getDifficultyLevel}\n" +
      f"Cost category: ${getEstimatedCostCategory}\n" +
      f"Ingredients: ${ingredients.size}"
  }

  override def toString: String = s"${name.value} (${ingredients.size} ingredients)"
}