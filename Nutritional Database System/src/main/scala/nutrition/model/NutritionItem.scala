package nutrition.model

import scalafx.beans.property.StringProperty

/**
 * Abstract base class for all nutritional items
 * Demonstrates inheritance and polymorphism principles
 * Supports UN Goal 2 by providing common nutritional interface
 */
abstract class NutritionalItem {

  // Abstract properties that must be implemented by subclasses
  val name: StringProperty

  /**
   * Abstract method: Get nutritional information for a given amount
   * @param amount Amount in grams (default 100g)
   * @return Map of nutrient names to values per specified amount
   */
  def getNutritionalInfo(amount: Double = 100.0): Map[String, Double]

  /**
   * Abstract method: Calculate nutrition score/quality
   * Different items may have different scoring algorithms
   */
  def getNutritionScore: Double

  /**
   * Abstract method: Get the primary category of this nutritional item
   */
  def getCategory: String

  /**
   * Concrete method: Check if item is suitable for specific dietary needs
   * Polymorphic behavior - different subclasses can override
   */
  def isSuitableFor(dietaryNeed: String): Boolean = {
    val nutrients = getNutritionalInfo()
    dietaryNeed.toLowerCase match {
      case "high-protein" => nutrients.getOrElse("Protein", 0.0) > 15.0
      case "low-calorie" => nutrients.getOrElse("Calories", 0.0) < 100.0
      case "high-fiber" => nutrients.getOrElse("Fiber", 0.0) > 5.0
      case "high-iron" => nutrients.getOrElse("Iron", 0.0) > 2.0
      case _ => true // Default: suitable for general consumption
    }
  }

  /**
   * Concrete method: Get nutritional density (nutrients per calorie)
   * Higher values indicate more nutrient-dense foods (better for food security)
   */
  def getNutritionalDensity: Double = {
    val nutrients = getNutritionalInfo()
    val calories = nutrients.getOrElse("Calories", 1.0)
    val importantNutrients = nutrients.getOrElse("Protein", 0.0) +
      nutrients.getOrElse("Fiber", 0.0) +
      nutrients.getOrElse("Iron", 0.0) +
      (nutrients.getOrElse("Vitamin C", 0.0) / 10.0)

    if (calories > 0) importantNutrients / calories else 0.0
  }

  /**
   * Concrete method: Check if item contributes to food security goals
   * Based on nutrient density and availability
   */
  def contributesToFoodSecurity: Boolean = {
    getNutritionalDensity > 0.05 && getNutritionScore > 5.0
  }

  /**
   * Template method pattern: Get formatted nutrition summary
   * Uses polymorphic methods to create consistent output
   */
  final def getNutritionSummary: String = {
    val nutrients = getNutritionalInfo()
    val category = getCategory
    val score = getNutritionScore

    s"${name.value} ($category)\n" +
      f"Nutrition Score: $score%.1f\n" +
      f"Calories: ${nutrients.getOrElse("Calories", 0.0)}%.1f\n" +
      f"Protein: ${nutrients.getOrElse("Protein", 0.0)}%.1f g\n" +
      f"Nutritional Density: ${getNutritionalDensity}%.3f"
  }

  override def toString: String = name.value
}