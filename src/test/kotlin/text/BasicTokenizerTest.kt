/*
 * Copyright © 2017–2020 Marcel Svitalsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
