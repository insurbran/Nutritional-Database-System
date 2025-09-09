package nutrition.model

import scalafx.beans.property.{DoubleProperty, StringProperty}

/**
 * Enhanced Food class that extends NutritionalItem
 * Demonstrates inheritance, polymorphism, and encapsulation
 * Represents individual food items with comprehensive nutritional data
 */
class Food(
            _name: String,           // Food name
            _calories: Double,       // Calories per 100g
            _protein: Double,        // Protein in grams per 100g
            _carbohydrates: Double,  // Carbohydrates in grams per 100g
            _fats: Double,           // Fats in grams per 100g
            _fiber: Double,          // Fiber in grams per 100g
            _potassium: Double,      // Potassium in mg per 100g
            _vitaminC: Double,       // Vitamin C in mg per 100g
            _iron: Double            // Iron in mg per 100g
          ) extends NutritionalItem {

  // Observable properties for GUI data binding (encapsulation)
  val name: StringProperty = StringProperty(_name)
  val calories: DoubleProperty = DoubleProperty(_calories)
  val protein: DoubleProperty = DoubleProperty(_protein)
  val carbohydrates: DoubleProperty = DoubleProperty(_carbohydrates)
  val fats: DoubleProperty = DoubleProperty(_fats)
  val fiber: DoubleProperty = DoubleProperty(_fiber)
  val potassium: DoubleProperty = DoubleProperty(_potassium)
  val vitaminC: DoubleProperty = DoubleProperty(_vitaminC)
  val iron: DoubleProperty = DoubleProperty(_iron)

  /**
   * Implementation of abstract method from NutritionalItem
   * Polymorphic behavior - each food calculates its own nutritional info
   * @param amount Amount in grams
   * @return Map of nutrient name to value for specified amount
   */
  override def getNutritionalInfo(amount: Double = 100.0): Map[String, Double] = {
    val factor = amount / 100.0
    Map(
      "Calories" -> (calories.value * factor),
      "Protein" -> (protein.value * factor),
      "Carbohydrates" -> (carbohydrates.value * factor),
      "Fats" -> (fats.value * factor),
      "Fiber" -> (fiber.value * factor),
      "Potassium" -> (potassium.value * factor),
      "Vitamin C" -> (vitaminC.value * factor),
      "Iron" -> (iron.value * factor)
    )
  }

  /**
   * Implementation of abstract method from NutritionalItem
   * Food-specific nutrition scoring algorithm
   * Higher score means more nutritious per calorie (supports food security)
   */
  override def getNutritionScore: Double = {
    val nutrientSum = protein.value + fiber.value + (vitaminC.value / 10) + (iron.value * 10)
    if (calories.value > 0) nutrientSum / calories.value * 100 else 0.0
  }

  /**
   * Implementation of abstract method from NutritionalItem
   * Categorizes food based on primary macronutrient
   */
  override def getCategory: String = {
    val nutrients = getNutritionalInfo()
    val proteinCal = nutrients("Protein") * 4      // 4 calories per gram
    val carbsCal = nutrients("Carbohydrates") * 4  // 4 calories per gram
    val fatsCal = nutrients("Fats") * 9            // 9 calories per gram
    val totalCal = nutrients("Calories")

    if (fatsCal / totalCal > 0.5) "High-Fat"
    else if (proteinCal / totalCal > 0.4) "High-Protein"
    else if (carbsCal / totalCal > 0.6) "Carbohydrate-Rich"
    else if (nutrients("Fiber") > 5.0) "High-Fiber"
    else if (totalCal < 50) "Low-Calorie"
    else "Balanced"
  }

  /**
   * Override parent method with food-specific dietary suitability
   * Demonstrates method overriding (polymorphism)
   */
  override def isSuitableFor(dietaryNeed: String): Boolean = {
    val baseResult = super.isSuitableFor(dietaryNeed)

    // Add food-specific rules
    dietaryNeed.toLowerCase match {
      case "weight-loss" => calories.value < 150 && fiber.value > 2.0 && fats.value < 10.0
      case "muscle-building" => protein.value > 20.0
      case "heart-healthy" => potassium.value > 200 && fiber.value > 3.0 && fats.value < 15.0
      case "immune-support" => vitaminC.value > 10.0 || iron.value > 1.0
      case "diabetic-friendly" => fiber.value > 3.0 && carbohydrates.value < 15.0
      case "low-fat" => fats.value < 3.0
      case "high-energy" => calories.value > 200 && fats.value > 10.0
      case "keto-friendly" => fats.value > 15.0 && carbohydrates.value < 5.0
      case _ => baseResult
    }
  }

  /**
   * Food-specific method: Check if food is nutrient-dense
   * Important for food security - maximize nutrition per calorie
   */
  def isNutrientDense: Boolean = {
    getNutritionalDensity > 0.1 && contributesToFoodSecurity
  }

  /**
   * Food-specific method: Get food's contribution to daily values
   * Based on standard daily nutritional requirements
   */
  def getDailyValueContribution(amount: Double = 100.0): Map[String, Double] = {
    val nutrients = getNutritionalInfo(amount)
    val dailyValues = Map(
      "Protein" -> 50.0,      // grams
      "Fiber" -> 25.0,        // grams
      "Vitamin C" -> 90.0,    // mg
      "Iron" -> 18.0,         // mg
      "Potassium" -> 3500.0   // mg
    )

    dailyValues.map { case (nutrient, dv) =>
      val contribution = (nutrients.getOrElse(nutrient, 0.0) / dv) * 100
      nutrient -> math.min(contribution, 100.0) // Cap at 100%
    }
  }

  /**
   * Food-specific method: Get storage and preservation info
   * Important for food security planning
   */
  def getStorageInfo: String = {
    val category = getCategory
    category match {
      case "High-Protein" if name.value.toLowerCase.contains("meat") =>
        "Refrigerate immediately, use within 3-5 days"
      case "High-Fiber" if name.value.toLowerCase.contains("fruit") =>
        "Store in cool, dry place, consume within 1 week"
      case "Carbohydrate-Rich" =>
        "Store in airtight container, shelf-stable for months"
      case _ => "Follow standard food storage guidelines"
    }
  }

  /**
   * Enhanced toString with category information
   */
  override def toString: String = s"${name.value} (${getCategory})"
}