name := "NutritionalInformationDatabase"

version := "1.0"

scalaVersion := "3.3.6"

// Determine the platform for JavaFX
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "20.0.0-R31"
)

// Add JavaFX dependencies with platform classifier
libraryDependencies ++= Seq("controls", "fxml").map { module =>
  "org.openjfx" % s"javafx-$module" % "20.0.1" classifier osName
}

fork := true

// Remove the module path options that are causing issues
javaOptions ++= Seq(
  "-Dprism.verbose=true"
)