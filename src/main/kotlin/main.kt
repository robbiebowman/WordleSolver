import com.robbiebowman.wordle.*

fun main(args: Array<String>) {
    while (true) {
        val engine = SolverEngine()
        val corpus = engine.getWords("five-letter-words.txt")
        var remainingPossibilities = corpus
        var guessResult: String
        val guessedWordsAndResults = mutableListOf<Pair<String, String>>()
        while (remainingPossibilities.count() > 1) {
            val charUsages = engine.countCharUsages(remainingPossibilities, guessedWordsAndResults)
            val guess = if (remainingPossibilities.size == 2)
                remainingPossibilities.first()
            else
                engine.wordWithHighestScore(corpus) { i -> engine.mostFreqUsage(i, charUsages, guessedWordsAndResults) }
            println("Try ${guess.uppercase()}")
            println("Input results (g for green, y for yellows, b for blacks. Example: bbbyg):")
            guessResult = readLine()!!
            guessedWordsAndResults.add(Pair(guess, guessResult))
            remainingPossibilities = engine.buildNewWords(remainingPossibilities, guess, guessResult)
            println("${remainingPossibilities.size} possibilities remain. ")
            if (remainingPossibilities.size < 20) println("Those are: $remainingPossibilities")
        }
        println("Hooray! The answer is ${remainingPossibilities.first().uppercase()}")
    }
}
