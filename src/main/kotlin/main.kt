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
    println(words.first())
}

fun buildNewWords(currentWords: List<String>, guess: String, guessResult: String): List<String> {
    return currentWords.filter { w ->
        guessResult.mapIndexed { i, r ->
            when (r) {
                HIT -> guess[i] == w[i]
                PARTIAL -> w.contains(guess[i])
                MISS -> !w.contains(guess[i])
                else -> throw Exception("You messed up the results, dummy")
            }
        }.all { it }
    }
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
        val gotYellowInThisPositionAlready = guessedWordsAndResults.any { w -> w.component1()[i] == c && w.component2()[i] == PARTIAL }
        if (input.indexOf(c) == i && !gotYellowInThisPositionAlready) usageCounts[c]!! else 0
    }.sum()
