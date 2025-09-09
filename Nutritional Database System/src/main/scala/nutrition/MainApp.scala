package nutrition

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.collections.ObservableBuffer
import scalafx.Includes._
import nutrition.model._
import nutrition.util._

/**
 * Enhanced MainApp with OOP Features Demonstration
 * Shows inheritance, polymorphism, and generic programming in action
 */
object MainApp extends JFXApp3:

  val foods: ObservableBuffer[Food] = ObservableBuffer()
  val recipes: ObservableBuffer[Recipe] = ObservableBuffer()

  // Create nutrition calculators (demonstrates generics)
  val foodCalculator = NutritionCalculator.forFoods()
  val recipeCalculator = NutritionCalculator.forRecipes()

  override def start(): Unit = {
    initializeSampleData()

    stage = new PrimaryStage {
      title = "Enhanced Nutritional Information Database - OOP Demo"
      width = 1000
      height = 800
      scene = createMainScene()
    }

    println("Enhanced application started with OOP features!")
  }

  private def initializeSampleData(): Unit = {
    // Add foods (polymorphism - all are NutritionalItems)
    foods ++= Seq(
      new Food("Apple", 52.0, 0.3, 13.8, 0.2, 2.4, 107.0, 4.0, 0.12),
      new Food("Banana", 89.0, 1.1, 22.8, 0.3, 2.6, 358.0, 8.7, 0.37),
      new Food("Chicken Breast", 165.0, 31.0, 0.0, 3.6, 0.0, 256.0, 0.0, 0.89),
      new Food("Brown Rice", 123.0, 2.6, 23.0, 0.9, 1.8, 43.0, 0.0, 0.15),
      new Food("Broccoli", 34.0, 2.8, 6.6, 0.4, 2.6, 316.0, 89.2, 0.12),
      new Food("Salmon", 208.0, 25.4, 0.0, 12.4, 0.0, 363.0, 0.0, 0.38),
      new Food("Avocado", 160.0, 2.0, 8.5, 14.7, 6.7, 485.0, 10.0, 0.55),
      new Food("Almonds", 579.0, 21.2, 21.6, 49.9, 12.5, 733.0, 0.0, 3.71)
    )

    // Add sample recipes (composition - recipes HAVE foods)
    val healthySalad = new Recipe("Healthy Salad", 2)
    healthySalad.addIngredient(foods(4), 100.0) // Broccoli
    healthySalad.addIngredient(foods(6), 50.0)  // Avocado
    recipes += healthySalad

    val proteinBowl = new Recipe("Protein Power Bowl", 1)
    proteinBowl.addIngredient(foods(2), 150.0) // Chicken
    proteinBowl.addIngredient(foods(3), 100.0) // Brown Rice
    proteinBowl.addIngredient(foods(4), 75.0)  // Broccoli
    recipes += proteinBowl
  }

  private def createMainScene(): Scene = {
    val foodTable = createFoodTable()
    val inputForm = createInputForm()
    val buttonBox = createButtonBox(foodTable, inputForm)
    val detailsArea = createDetailsArea()
    val oopFeaturesBox = createOOPFeaturesBox() // NEW: OOP demonstration buttons
    val resultsArea = createResultsArea() // NEW: Results display area

    foodTable.selectionModel.value.selectedItem.onChange { (_, _, newValue) =>
      if (newValue != null) {
        updateDetailsArea(detailsArea, newValue)
        fillInputForm(inputForm, newValue)
      } else {
        clearDetailsArea(detailsArea)
        clearInputForm(inputForm)
      }
    }

    val mainLayout = new BorderPane {
      top = new VBox {
        padding = Insets(10)
        children = Seq(
          new Label("Enhanced Nutritional Information Database") {
            style = "-fx-font-size: 18px; -fx-font-weight: bold;"
          },
          new Label("Demonstrating OOP: Inheritance, Polymorphism & Generics") {
            style = "-fx-font-size: 12px; -fx-style: italic;"
          }
        )
      }

      center = new VBox {
        padding = Insets(10)
        spacing = 10
        children = Seq(
          new Label("Food Database:") {
            style = "-fx-font-weight: bold;"
          },
          foodTable,
          detailsArea,
          new Separator(),
          new Label("OOP Features Demonstration:") {
            style = "-fx-font-weight: bold; -fx-text-fill: blue;"
          },
          oopFeaturesBox,
          resultsArea
        )
      }

      bottom = new VBox {
        padding = Insets(10)
        spacing = 10
        children = Seq(
          new Separator(),
          new Label("Add/Edit Food:") {
            style = "-fx-font-weight: bold;"
          },
          inputForm,
          buttonBox
        )
      }
    }

    new Scene(mainLayout)
  }

  /**
   * NEW: Create OOP features demonstration buttons
   */
  private def createOOPFeaturesBox(): HBox = {
    val polymorphismBtn = new Button("Show Polymorphism") {
      onAction = _ => demonstratePolymorphism()
    }

    val inheritanceBtn = new Button("Show Inheritance") {
      onAction = _ => demonstrateInheritance()
    }

    val genericsBtn = new Button("Use Generics") {
      onAction = _ => demonstrateGenerics()
    }

    val compositionBtn = new Button("Show Composition") {
      onAction = _ => demonstrateComposition()
    }

    val recommendBtn = new Button("Get Recommendations") {
      onAction = _ => getRecommendations()
    }

    new HBox {
      spacing = 10
      children = List(polymorphismBtn, inheritanceBtn, genericsBtn, compositionBtn, recommendBtn)
    }
  }

  /**
   * NEW: Create results display area
   */
  private def createResultsArea(): VBox = {
    val resultsLabel = new Label("Click OOP feature buttons to see demonstrations")
    val resultsText = new TextArea {
      prefRowCount = 8
      editable = false
      text = "Results will appear here..."
    }

    val vbox = new VBox {
      spacing = 5
      children = List(resultsLabel, resultsText)
    }

    vbox.userData = Map("text" -> resultsText)
    vbox
  }

  /**
   * Demonstrate Polymorphism - same method, different behavior
   */
  private def demonstratePolymorphism(): Unit = {
    val results = new StringBuilder()
    results.append("=== POLYMORPHISM DEMONSTRATION ===\n\n")
    results.append("Same method 'getNutritionalInfo()' behaves differently:\n\n")

    // Show how Food and Recipe implement same method differently
    val apple = foods(0)
    val recipe = recipes(0)

    results.append(s"${apple.name.value} (Food class):\n")
    val appleNutrients = apple.getNutritionalInfo()
    appleNutrients.foreach { case (nutrient, value) =>
      results.append(f"  $nutrient: $value%.1f\n")
    }

    results.append(s"\n${recipe.name.value} (Recipe class):\n")
    val recipeNutrients = recipe.getNutritionalInfo()
    recipeNutrients.foreach { case (nutrient, value) =>
      results.append(f"  $nutrient: $value%.1f\n")
    }

    results.append("\nSame method call, different calculations! 🎯")

    updateResults(results.toString())
  }

  /**
   * Demonstrate Inheritance - show class hierarchy
   */
  private def demonstrateInheritance(): Unit = {
    val results = new StringBuilder()
    results.append("=== INHERITANCE DEMONSTRATION ===\n\n")
    results.append("Class Hierarchy:\n")
    results.append("  NutritionalItem (Abstract Base Class)\n")
    results.append("    ├── Food (Concrete Implementation)\n")
    results.append("    └── Recipe (Concrete Implementation)\n\n")

    val apple = foods(0)
    results.append(s"${apple.name.value} behaviors:\n")
    results.append(s"  Category: ${apple.getCategory}\n")
    results.append(s"  Nutrition Score: ${apple.getNutritionScore}\n")
    results.append(s"  Suitable for weight-loss: ${apple.isSuitableFor("weight-loss")}\n")
    results.append(s"  Contributes to food security: ${apple.contributesToFoodSecurity}\n")
    results.append(s"  Nutritional density: ${apple.getNutritionalDensity}\n\n")

    results.append("All methods inherited from NutritionalItem! 👨‍👩‍👧‍👦")

    updateResults(results.toString())
  }

  /**
   * Demonstrate Generic Programming
   */
  private def demonstrateGenerics(): Unit = {
    val results = new StringBuilder()
    results.append("=== GENERIC PROGRAMMING DEMONSTRATION ===\n\n")

    // Show generic calculator working with different types
    val foodList = foods.take(3).toList
    val avgScore = foodCalculator.calculateAverageScore(foodList)
    val topFoods = foodCalculator.getTopNItems(foodList, 2)
    val categories = foodCalculator.groupByCategory(foodList)

    results.append("Generic NutritionCalculator[Food] results:\n")
    results.append(f"Average nutrition score: $avgScore%.1f\n")
    results.append(s"Top 2 foods: ${topFoods.map(_.name.value).mkString(", ")}\n")
    results.append(s"Food categories: ${categories.keys.mkString(", ")}\n\n")

    // Show same calculator working with recipes
    val recipeList = recipes.toList
    val recipeAvg = recipeCalculator.calculateAverageScore(recipeList)

    results.append("Same calculator working with Recipe type:\n")
    results.append(f"Average recipe score: $recipeAvg%.1f\n")
    results.append(s"Recipe categories: ${recipeList.map(_.getCategory).mkString(", ")}\n\n")

    results.append("One calculator, multiple types! Type safety guaranteed! 🔧")

    updateResults(results.toString())
  }

  /**
   * Demonstrate Composition - Recipe HAS Foods
   */
  private def demonstrateComposition(): Unit = {
    val results = new StringBuilder()
    results.append("=== COMPOSITION DEMONSTRATION ===\n\n")
    results.append("Recipe 'HAS-A' relationship with Foods:\n\n")

    recipes.foreach { recipe =>
      results.append(s"${recipe.name.value}:\n")
      recipe.getIngredients.foreach { case (food, amount) =>
        results.append(f"  - ${food.name.value}: ${amount}g\n")
      }
      results.append(f"  Total calories: ${recipe.getNutritionalInfo().getOrElse("Calories", 0.0)}%.0f\n")
      results.append(f"  Cooking time: ${recipe.getEstimatedCookingTime} minutes\n")
      results.append(f"  Difficulty: ${recipe.getDifficultyLevel}\n\n")
    }

    results.append("Recipes are composed of multiple Food objects! 🍽️")

    updateResults(results.toString())
  }

  /**
   * Get personalized recommendations using OOP features
   */
  private def getRecommendations(): Unit = {
    val results = new StringBuilder()
    results.append("=== INTELLIGENT RECOMMENDATIONS ===\n\n")

    // Use polymorphism and generics for recommendations
    val foodList = foods.toList

    // Find foods for different dietary needs (polymorphism)
    val weightLossFoods = foodCalculator.findFoodsForDiet(foodList, "weight-loss")
    val highProteinFoods = foodCalculator.findFoodsForDiet(foodList, "high-protein")
    val heartHealthyFoods = foodCalculator.findFoodsForDiet(foodList, "heart-healthy")

    results.append("DIETARY RECOMMENDATIONS:\n\n")
    results.append(s"For Weight Loss: ${weightLossFoods.map(_.name.value).mkString(", ")}\n\n")
    results.append(s"High Protein Foods: ${highProteinFoods.map(_.name.value).mkString(", ")}\n\n")
    results.append(s"Heart Healthy Options: ${heartHealthyFoods.map(_.name.value).mkString(", ")}\n\n")

    // Calculate food security impact (generics)
    val securityImpact = NutritionCalculator.calculateFoodSecurityImpact(foodList)
    results.append(f"FOOD SECURITY ANALYSIS:\n")
    results.append(f"Overall food security impact score: $securityImpact%.1f\n\n")

    // Show complementary foods
    if (foodList.nonEmpty) {
      val primaryFood = foodList.head
      val complementary = foodCalculator.findComplementaryFoods(primaryFood, foodList.tail)
      results.append(s"Foods that complement ${primaryFood.name.value}:\n")
      results.append(s"${complementary.map(_.name.value).mkString(", ")}\n\n")
    }

    results.append("Powered by OOP: Inheritance + Polymorphism + Generics! 🧠")

    updateResults(results.toString())
  }

  /**
   * Update the results display area
   */
  private def updateResults(text: String): Unit = {
    val resultsArea = stage.scene.value.getRoot.lookup(".results-area").asInstanceOf[VBox]
    if (resultsArea != null && resultsArea.userData != null) {
      val fields = resultsArea.userData.asInstanceOf[Map[String, TextArea]]
      fields.get("text").foreach(_.text = text)
    }
  }

  // Keep all your existing methods (createFoodTable, createInputForm, etc.)
  // ... [Previous methods stay the same] ...

  private def createFoodTable(): TableView[Food] = {
    val table = new TableView[Food](foods) {
      prefHeight = 200
    }

    val nameCol = new TableColumn[Food, String]("Name") {
      prefWidth = 120
      cellValueFactory = _.value.name
    }

    val caloriesCol = new TableColumn[Food, String]("Calories") {
      prefWidth = 80
      cellValueFactory = { cellData =>
        scalafx.beans.binding.Bindings.createStringBinding(
          () => cellData.value.calories.value.toString,
          cellData.value.calories
        )
      }
    }

    val proteinCol = new TableColumn[Food, String]("Protein") {
      prefWidth = 80
      cellValueFactory = { cellData =>
        scalafx.beans.binding.Bindings.createStringBinding(
          () => s"${cellData.value.protein.value}g",
          cellData.value.protein
        )
      }
    }

    val carbsCol = new TableColumn[Food, String]("Carbs") {
      prefWidth = 80
      cellValueFactory = { cellData =>
        scalafx.beans.binding.Bindings.createStringBinding(
          () => s"${cellData.value.carbohydrates.value}g",
          cellData.value.carbohydrates
        )
      }
    }

    val fatsCol = new TableColumn[Food, String]("Fats") {
      prefWidth = 80
      cellValueFactory = { cellData =>
        scalafx.beans.binding.Bindings.createStringBinding(
          () => s"${cellData.value.fats.value}g",
          cellData.value.fats
        )
      }
    }

    table.columns ++= List(nameCol, caloriesCol, proteinCol, carbsCol, fatsCol)
    table
  }

  private def createInputForm(): GridPane = {
    val grid = new GridPane {
      hgap = 10
      vgap = 5
      padding = Insets(10)
    }

    val nameField = new TextField { promptText = "Food name" }
    val caloriesField = new TextField { promptText = "Calories per 100g" }
    val proteinField = new TextField { promptText = "Protein (g)" }
    val carbsField = new TextField { promptText = "Carbohydrates (g)" }
    val fatsField = new TextField { promptText = "Fats (g)" }
    val fiberField = new TextField { promptText = "Fiber (g)" }
    val potassiumField = new TextField { promptText = "Potassium (mg)" }
    val vitaminCField = new TextField { promptText = "Vitamin C (mg)" }
    val ironField = new TextField { promptText = "Iron (mg)" }

    grid.userData = Map(
      "name" -> nameField,
      "calories" -> caloriesField,
      "protein" -> proteinField,
      "carbs" -> carbsField,
      "fats" -> fatsField,
      "fiber" -> fiberField,
      "potassium" -> potassiumField,
      "vitaminC" -> vitaminCField,
      "iron" -> ironField
    )

    grid.add(new Label("Name:"), 0, 0)
    grid.add(nameField, 1, 0)
    grid.add(new Label("Calories:"), 2, 0)
    grid.add(caloriesField, 3, 0)

    grid.add(new Label("Protein (g):"), 0, 1)
    grid.add(proteinField, 1, 1)
    grid.add(new Label("Carbs (g):"), 2, 1)
    grid.add(carbsField, 3, 1)

    grid.add(new Label("Fats (g):"), 0, 2)
    grid.add(fatsField, 1, 2)
    grid.add(new Label("Fiber (g):"), 2, 2)
    grid.add(fiberField, 3, 2)

    grid.add(new Label("Potassium (mg):"), 0, 3)
    grid.add(potassiumField, 1, 3)
    grid.add(new Label("Vitamin C (mg):"), 2, 3)
    grid.add(vitaminCField, 3, 3)

    grid.add(new Label("Iron (mg):"), 0, 4)
    grid.add(ironField, 1, 4)

    grid
  }

  private def createButtonBox(table: TableView[Food], form: GridPane): HBox = {
    val addBtn = new Button("Add Food") {
      onAction = _ => handleAddFood(form)
    }

    val editBtn = new Button("Edit Food") {
      onAction = _ => handleEditFood(table, form)
    }

    val deleteBtn = new Button("Delete Food") {
      onAction = _ => handleDeleteFood(table, form)
    }

    val aboutBtn = new Button("About") {
      onAction = _ => showAboutDialog()
    }

    new HBox {
      spacing = 10
      children = List(addBtn, editBtn, deleteBtn, aboutBtn)
    }
  }

  private def createDetailsArea(): VBox = {
    val detailsLabel = new Label("Select a food item to view details")
    val scoreLabel = new Label("")

    val vbox = new VBox {
      spacing = 5
      children = List(detailsLabel, scoreLabel)
    }

    vbox.userData = Map("details" -> detailsLabel, "score" -> scoreLabel)
    vbox
  }

  private def updateDetailsArea(detailsArea: VBox, food: Food): Unit = {
    val fields = detailsArea.userData.asInstanceOf[Map[String, Label]]
    fields("details").text = s"${food.name.value}: ${food.calories.value} calories, ${food.protein.value}g protein"
    fields("score").text = f"Nutrition Score: ${food.getNutritionScore}%.1f"
  }

  private def clearDetailsArea(detailsArea: VBox): Unit = {
    val fields = detailsArea.userData.asInstanceOf[Map[String, Label]]
    fields("details").text = "Select a food item to view details"
    fields("score").text = ""
  }

  private def fillInputForm(form: GridPane, food: Food): Unit = {
    val fields = form.userData.asInstanceOf[Map[String, TextField]]
    fields("name").text = food.name.value
    fields("calories").text = food.calories.value.toString
    fields("protein").text = food.protein.value.toString
    fields("carbs").text = food.carbohydrates.value.toString
    fields("fats").text = food.fats.value.toString
    fields("fiber").text = food.fiber.value.toString
    fields("potassium").text = food.potassium.value.toString
    fields("vitaminC").text = food.vitaminC.value.toString
    fields("iron").text = food.iron.value.toString
  }

  private def clearInputForm(form: GridPane): Unit = {
    val fields = form.userData.asInstanceOf[Map[String, TextField]]
    fields.values.foreach(_.text = "")
  }

  private def handleAddFood(form: GridPane): Unit = {
    val fields = form.userData.asInstanceOf[Map[String, TextField]]
    val name = fields("name").text.value.trim

    if (name.nonEmpty) {
      try {
        val newFood = new Food(
          name,
          getDouble(fields("calories").text.value),
          getDouble(fields("protein").text.value),
          getDouble(fields("carbs").text.value),
          getDouble(fields("fats").text.value),
          getDouble(fields("fiber").text.value),
          getDouble(fields("potassium").text.value),
          getDouble(fields("vitaminC").text.value),
          getDouble(fields("iron").text.value)
        )

        foods += newFood
        clearInputForm(form)
        showAlert("Success", s"Added ${newFood.name.value} to database!")

      } catch {
        case e: Exception =>
          showAlert("Error", s"Failed to add food: ${e.getMessage}")
      }
    } else {
      showAlert("Error", "Please enter a food name!")
    }
  }

  private def handleEditFood(table: TableView[Food], form: GridPane): Unit = {
    val selected = table.selectionModel.value.selectedItem.value
    if (selected != null) {
      try {
        val fields = form.userData.asInstanceOf[Map[String, TextField]]

        selected.name.value = fields("name").text.value.trim
        selected.calories.value = getDouble(fields("calories").text.value)
        selected.protein.value = getDouble(fields("protein").text.value)
        selected.carbohydrates.value = getDouble(fields("carbs").text.value)
        selected.fats.value = getDouble(fields("fats").text.value)
        selected.fiber.value = getDouble(fields("fiber").text.value)
        selected.potassium.value = getDouble(fields("potassium").text.value)
        selected.vitaminC.value = getDouble(fields("vitaminC").text.value)
        selected.iron.value = getDouble(fields("iron").text.value)

        showAlert("Success", s"Updated ${selected.name.value}!")

      } catch {
        case e: Exception =>
          showAlert("Error", s"Failed to update food: ${e.getMessage}")
      }
    } else {
      showAlert("Error", "Please select a food item to edit!")
    }
  }

  private def handleDeleteFood(table: TableView[Food], form: GridPane): Unit = {
    val selectedIndex = table.selectionModel.value.selectedIndex.value
    if (selectedIndex >= 0) {
      val food = foods(selectedIndex)
      foods.remove(selectedIndex)
      clearInputForm(form)
      showAlert("Success", s"Deleted ${food.name.value}!")
    } else {
      showAlert("Error", "Please select a food item to delete!")
    }
  }

  private def showAboutDialog(): Unit = {
    showAlert("About",
      "Enhanced Nutritional Information Database v2.0\n\n" +
        "Supporting UN Goal 2: Zero Hunger\n" +
        "Demonstrating Advanced OOP Concepts:\n" +
        "• Inheritance (NutritionalItem hierarchy)\n" +
        "• Polymorphism (method overriding)\n" +
        "• Generic Programming (type-safe collections)\n" +
        "• Composition (Recipe has Foods)\n\n" +
        "Built with ScalaFX and advanced design patterns")
  }

  private def getDouble(text: String): Double = {
    try {
      if (text.trim.isEmpty) 0.0 else text.toDouble
    } catch {
      case _: NumberFormatException => 0.0
    }
  }

  private def showAlert(alertTitle: String, message: String): Unit = {
    val alert = new Alert(Alert.AlertType.Information) {
      title = alertTitle
      headerText = None
      contentText = message
    }
    alert.showAndWait()
  }