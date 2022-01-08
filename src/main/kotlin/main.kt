import java.io.File
import java.lang.Exception

const val HIT = 'g'
const val PARTIAL = 'y'
const val MISS = 'b'

fun main(args: Array<String>) {
    while (true) {
        val corpus = getWords("five-letter-words.txt")
        var remainingPossibilities = corpus
        var guessResult: String
        val guessedWordsAndResults = mutableListOf<Pair<String, String>>()
        while (remainingPossibilities.count() > 1) {
            val charUsages = countCharUsages(remainingPossibilities, guessedWordsAndResults)
            val guess = wordWithHighestScore(corpus) { i -> mostFreqUsage(i, charUsages, guessedWordsAndResults) }
            println("Try ${guess.uppercase()}")
            println("Input results (g for green, y for yellows, b for blacks. Example: bbbyg):")
            guessResult = readLine()!!
            guessedWordsAndResults.add(Pair(guess, guessResult))
            remainingPossibilities = buildNewWords(remainingPossibilities, guess, guessResult)
            println("${remainingPossibilities.size} possibilities remain. ")
            if (remainingPossibilities.size < 20) println("Those are: $remainingPossibilities")
        }
        println("Hooray! The answer is ${remainingPossibilities.first().uppercase()}")
    }
}

fun buildNewWords(currentWords: List<String>, guess: String, result: String): List<String> {
    return currentWords.filter { w ->
        result.mapIndexed { i, r ->
            val c = guess[i]
            when (r) {
                HIT -> c == w[i]
                PARTIAL -> w.contains(c) && c != w[i]
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

fun countCharUsages(words: List<String>, guessedWordsAndResults: List<Pair<String, String>>): Map<Char, Int> {
    val allChars = words.joinToString("")
    return ('a'..'z').map { a ->
        a to if (
            charHasBeenGuessedAlready(a, guessedWordsAndResults)
            && !hasUnplacedOccurrences(a, guessedWordsAndResults)
        )
            0
        else
            allChars.count { c -> c == a }
    }.toMap()
}

fun charHasBeenGuessedAlready(char: Char, guessedWordsAndResults: List<Pair<String, String>>): Boolean {
    return guessedWordsAndResults.any { e -> e.component1().contains(char) }
}

fun hasUnplacedOccurrences(char: Char, guessedWordsAndResults: List<Pair<String, String>>): Boolean {
    val maxInGuess = guessedWordsAndResults.map { it.first.count { c -> c == char } }.maxOrNull() ?: 0
    return guessedWordsAndResults
        .filter { it.first.count { c -> c == char } == maxInGuess }
        .none {
            it.first.mapIndexed { i, c ->
                if (c == char) it.second[i] == HIT else true
            }.all { b -> b }
        }
}

fun wordWithHighestScore(words: List<String>, scoring: (String) -> Int): String {
    return words.maxByOrNull { scoring(it) }!!
}

fun mostFreqUsage(input: String, usageCounts: Map<Char, Int>, guessedWordsAndResults: List<Pair<String, String>>) =
    input.toCharArray().mapIndexed { i, c ->
        val gotYellowOrGreenInThisPositionAlready =
            guessedWordsAndResults.any { w -> w.component1()[i] == c && w.component2()[i] != MISS }
        if (input.indexOf(c) == i && !gotYellowOrGreenInThisPositionAlready) usageCounts[c]!! else 0
    }.sum()
