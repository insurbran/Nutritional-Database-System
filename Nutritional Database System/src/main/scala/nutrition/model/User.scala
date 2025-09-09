package nutrition.model

import scalafx.beans.property.{StringProperty, IntegerProperty, DoubleProperty}

/**
 * Simple User class representing a person with basic information
 * Used for personalized nutrition recommendations
 */
class User(
            _name: String,
            _age: Int,
            _gender: String,
            _weight: Double,
            _height: Double,
            _activityLevel: String
          ) {

  // Observable properties for GUI data binding
  val name: StringProperty = StringProperty(_name)
  val age: IntegerProperty = IntegerProperty(_age)
  val gender: StringProperty = StringProperty(_gender)
  val weight: DoubleProperty = DoubleProperty(_weight)
  val height: DoubleProperty = DoubleProperty(_height)
  val activityLevel: StringProperty = StringProperty(_activityLevel)

  /**
   * Calculate Body Mass Index (BMI)
   */
  def getBMI: Double = {
    val heightInMeters = height.value / 100.0
    weight.value / (heightInMeters * heightInMeters)
  }

  /**
   * Get BMI category for health assessment
   */
  def getBMICategory: String = {
    val bmi = getBMI
    if (bmi < 18.5) "Underweight"
    else if (bmi < 25) "Normal"
    else if (bmi < 30) "Overweight"
    else "Obese"
  }

  /**
   * Calculate daily calorie needs using simple formula
   */
  def getDailyCalorieNeeds: Double = {
    // Basic calculation - can be enhanced later
    val baseCalories = if (gender.value.toLowerCase == "male") 2000 else 1800
    val ageFactor = if (age.value > 50) 0.9 else 1.0
    val activityFactor = activityLevel.value match {
      case "Sedentary" => 1.0
      case "Light" => 1.2
      case "Moderate" => 1.4
      case "Active" => 1.6
      case _ => 1.2
    }

    baseCalories * ageFactor * activityFactor
  }

  /**
   * Get basic protein needs (grams per day)
   */
  def getProteinNeeds: Double = weight.value * 0.8 // 0.8g per kg body weight

  /**
   * Get user info summary
   */
  def getUserSummary: String = {
    f"${name.value} - Age: ${age.value}, BMI: ${getBMI}%.1f (${getBMICategory}), " +
      f"Daily Calories: ${getDailyCalorieNeeds}%.0f"
  }

  override def toString: String = name.value
}
