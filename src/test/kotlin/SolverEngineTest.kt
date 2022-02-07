import com.robbiebowman.wordle.getSuggestion
import org.junit.Test
import kotlin.test.assertNotNull

class SolverEngineTest {

    @Test
    fun testSolverEngine() {
        val suggestion = getSuggestion(listOf("place", "rides", "brent", "kefir", "morph"), listOf("bbbby", "ybbyg", "byybb", "bgbby", "ybgbb"))

        assertNotNull(suggestion.possibleAnswers)
    }
}