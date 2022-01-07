import java.io.File
import java.lang.Exception

const val HIT = 'g'
const val PARTIAL = 'y'
const val MISS = 'b'

fun main(args: Array<String>) {
    var words = getWords("five-letter-words.txt")
    var guessResult: String
    val guessedWordsAndResults = HashMap<String, String>()
    while (words.count() > 1) {
        val charUsages = countCharUsages(words)
        val guess = wordWithHighestScore(words) { i -> mostFreqUsage(i, charUsages, guessedWordsAndResults) }
        println("Try ${guess.uppercase()}")
        println("Input results (g for green, y for yellows, b for blacks. Example: bbbyg):")
        guessResult = readLine()!!
        guessedWordsAndResults[guess] = guessResult
        words = buildNewWords(words, guess, guessResult)
    }
    println("Hooray! The answer is ${words.first().uppercase()}")
}

fun buildNewWords(currentWords: List<String>, guess: String, result: String): List<String> {
    return currentWords.filter { w ->
        result.mapIndexed { i, r ->
            val c = guess[i]
            when (r) {
                HIT -> c == w[i]
                PARTIAL -> w.contains(c)
                MISS -> !w.contains(c) ||
                        if (isBlackBecauseCharWasGuessedTooManyTimes(i, c, guess, result))
                            hasFewerOfCharThanGuess(c, w, guess)
                        else
                            false
                else -> throw Exception("You messed up the results, dummy")
            }
        }.all { it }
    }
}

fun hasFewerOfCharThanGuess(char: Char, word: String, guess: String): Boolean {
    return word.count { it == char } < guess.count { it == char }
}

fun isBlackBecauseCharWasGuessedTooManyTimes(index: Int, char: Char, guess: String, result: String): Boolean {
    val numYellowsForCharBeforeCurrent =
        guess.take(index).mapIndexed { i, c -> result[i] == PARTIAL && c == char }.count { it }
    val numGreensForChar = guess.mapIndexed { i, c -> result[i] == HIT && c == char }.count { it }
    val numCharInTotal = guess.count { it == char }
    return numYellowsForCharBeforeCurrent + numGreensForChar < numCharInTotal
}

fun getWords(filePath: String): List<String> = File(filePath).readText().split('\n').dropLast(1).map { it.trim() }

fun countCharUsages(words: List<String>): Map<Char, Int> {
    val allChars = words.joinToString("")
    return ('a'..'z').map { a -> a to allChars.count { c -> c == a } }.toMap()
}

fun wordWithHighestScore(words: List<String>, scoring: (String) -> Int): String {
    return words.maxByOrNull { scoring(it) }!!
}

fun mostFreqUsage(input: String, usageCounts: Map<Char, Int>, guessedWordsAndResults: Map<String, String>) =
    input.toCharArray().mapIndexed { i, c ->
        val gotYellowOrGreenInThisPositionAlready =
            guessedWordsAndResults.any { w -> w.component1()[i] == c && w.component2()[i] != MISS }
        if (input.indexOf(c) == i && !gotYellowOrGreenInThisPositionAlready) usageCounts[c]!! else 0
    }.sum()
