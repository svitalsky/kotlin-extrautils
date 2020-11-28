package cz.mpts.libs.extrautils.kotlin.text

import org.junit.Test
import kotlin.test.assertEquals

class BasicTokenizerTest {

    private val testees = mapOf(
        "" to emptyList(),
        "word" to listOf("word"),
        " \"" to listOf("\""),
        "ano \"\" ne" to
                listOf("ano", "ne"),
        "\"two words or three or…\"" to
                listOf("two words or three or…"),
        "with escaped \\\" quote" to
                listOf("with", "escaped", "\"", "quote"),
        "two words" to
                listOf("two", "words"),
        "text with \"some quotes\" etc." to
                listOf("text", "with", "some quotes", "etc."),
        "text \"with quotes\" and \\\"escaped too" to
                listOf("text", "with quotes", "and", "\"escaped", "too"),
        "all \\\\ \"hell broke\\\\\" loose \" they say \\\"\"" to
                listOf("all", "\\", "hell broke\\", "loose", " they say \""),
        "some \\ and more" to
                listOf("some", "\\", "and", "more"),
        "some and other \"and even" to
                listOf("some", "and", "other", "\"and", "even"),
        "some and other \"and even\\" to
                listOf("some", "and", "other", "\"and", "even\\"),
    )


    @Test
    fun tokenizeTest() {
        testees.forEach { (txt, expected) ->
            assertEquals(expected, txt.tokenized)
        }
    }
}
