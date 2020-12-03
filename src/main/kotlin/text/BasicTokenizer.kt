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

private class FlaggedPart(val text: String, val quoted: Boolean)

@Suppress("unused")
private class WorkingData(var inQuotes: Boolean = false,
                          var escaping: Boolean = false,
                          val buffer: MutableList<Char> = mutableListOf())
{
    fun switchInQuotes() {
        inQuotes = !inQuotes
    }

    private val Char.nonEscapable
        get() = this != '"' && this != '\\'

    private fun checkEscapeAndAdd(c: Char) {
        if (escaping && c.nonEscapable) buffer.add('\\')
        buffer.add(c)
    }

    fun processChar(c: Char) {
        escaping = (c == '\\' && !escaping).also { willBeEscaping ->
            if (!willBeEscaping) checkEscapeAndAdd(c)
        }
    }

    val bufferAsString
        get() = String(buffer.toCharArray()).also { buffer.clear() }

    val empty
        get() = buffer.isEmpty()

    val nonEmpty
        get() = buffer.isNotEmpty()

    fun onFinish() = apply {
        if (inQuotes) buffer.add(0, '"')
        if (escaping) buffer.add('\\')
    }

    fun addChar(c: Char) = buffer.add(c)
}

private fun Char.significantQuote(escaped: Boolean) =
    !escaped && (this == '"')

private fun startingQuotes(txt: String, index: Int, inQuotes: Boolean) =
    !inQuotes && ((index == 0) || txt[index - 1].isWhitespace())

private fun closingQuotes(txt: String, index: Int, inQuotes: Boolean) =
    inQuotes && ((index == (txt.length - 1)) || txt[index + 1].isWhitespace())

private fun MutableList<FlaggedPart>.addFlaggedPart(data: WorkingData,
                                                    isQuoted: Boolean = false) =
    data.run {
        if (nonEmpty) {
            val s = bufferAsString
            if (s.isNotBlank() || isQuoted) add(FlaggedPart(s, isQuoted))
        }
    }

private val blanks = "\\s+".toRegex()

private val String.tokenizedSimple
    get() = trim().split(blanks)

private val List<FlaggedPart>.result
    get() = flatMap { part ->
        if (part.quoted) listOf(part.text)
        else part.text.tokenizedSimple
    }

private fun doTokenize(txt: String) = mutableListOf<FlaggedPart>().run {
    WorkingData().let { data ->
        txt.forEachIndexed { index, c ->
            if (c.significantQuote(data.escaping)) {
                if (startingQuotes(txt, index, data.inQuotes) || closingQuotes(txt, index, data.inQuotes)) {
                    addFlaggedPart(data, data.inQuotes)
                    data.switchInQuotes()
                }
                else data.addChar(c)
            }
            else data.processChar(c)
        }
        addFlaggedPart(data.onFinish(), false)
    }
    result
}

val String.tokenized
    get() = with (trim()) {
        if (isEmpty()) emptyList()
        else doTokenize(this)
    }
