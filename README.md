# WordleSolver
Solves the wonderful puzzles of Wordle https://www.powerlanguage.co.uk/wordle/

[![](https://jitpack.io/v/robbiebowman/WordleSolver.svg)](https://jitpack.io/#robbiebowman/WordleSolver)

## Setup
```groovy
dependencies {
    implementation 'com.github.robbiebowman:WordleSolver:v0.8'
}
```
## Sample usage
```kotlin
import com.robbiebowman.wordle.getSuggestion
```
```kotlin
val bestGuess = getSuggestion(listOf("spork", "spill"), listOf("ggbbb", "ggbgg"), hardMode=true)
println(bestGuess.word) // spell

```