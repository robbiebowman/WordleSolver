# WordleSolver
A library that solves the wonderful puzzles of Wordle https://www.powerlanguage.co.uk/wordle/

You can import the library into your own project, or run the main method and play through the command line.

[![](https://jitpack.io/v/robbiebowman/WordleSolver.svg)](https://jitpack.io/#robbiebowman/WordleSolver)

## Setup
```groovy
dependencies {
    implementation 'com.github.robbiebowman:WordleSolver:v1.2'
}
```
## Sample usage
```kotlin
import com.robbiebowman.wordle.SolverEngine
```
```kotlin
val engine = SolverEngine()
val bestGuess = engine.getSuggestion(
    listOf("spork", "spill"), 
    listOf("ggbbb", "ggbgg"), 
    hardMode=true
)
println(bestGuess.word) // spell
println(bestGuess.possibleAnswers.size) // 1
```