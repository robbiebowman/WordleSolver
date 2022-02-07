import com.robbiebowman.wordle.getSuggestion
import org.junit.Test
import kotlin.test.assertNotNull

class SolverEngineTest {

    @Test
    fun testSolverEngine() {
        val suggestion = getSuggestion(
            listOf("speak", "train", "altho"),
            listOf("bbbyb", "ybybb", "ggyby"),
            hardMode = true
        )

        assertNotNull(suggestion.possibleAnswers)
    }
}