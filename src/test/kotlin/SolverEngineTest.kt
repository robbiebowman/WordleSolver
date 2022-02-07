import com.robbiebowman.wordle.SolverEngine
import org.junit.Test
import kotlin.test.assertNotNull

class SolverEngineTest {

    @Test
    fun testSolverEngine() {
        val engine = SolverEngine()
        val suggestion = engine.getSuggestion(
            listOf("speak", "train", "altho"),
            listOf("bbbyb", "ybybb", "ggyby"),
            hardMode = true
        )

        assertNotNull(suggestion.possibleAnswers)
    }
}