package cz.mpts.libs.extrautils.kotlin.text

private class FlaggedPart(val text: String, val quoted: Boolean)

private val blanks = "\\s+".toRegex()

private val String.tokenizedSimple
    get() = trim().split(blanks)

private fun Char.significantQuote(escaped: Boolean) =
    !escaped && (this == '"')

private val MutableList<Char>.getString
    get() = String(toCharArray()).also { clear() }

private fun startingQuotes(txt: String, index: Int, inQuotes: Boolean) =
    !inQuotes && ((index == 0) || txt[index - 1].isWhitespace())

private fun closingQuotes(txt: String, index: Int, inQuotes: Boolean) =
    inQuotes && ((index == (txt.length - 1)) || txt[index + 1].isWhitespace())

private val Char.nonEscapable
    get() = this != '"' && this != '\\'

private fun MutableList<Char>.checkEscapeAndAdd(c: Char, escaping: Boolean) {
    if (escaping && c.nonEscapable) add('\\')
    add(c)
}

private fun MutableList<Char>.processChar(c: Char, escaping: Boolean) =
    (c == '\\' && !escaping).also { willBeEscaping ->
        if (!willBeEscaping) checkEscapeAndAdd(c, escaping)
    }

private fun MutableList<FlaggedPart>.addFlaggedPart(buffer: MutableList<Char>,
                                                    isQuoted: Boolean = false) =
    apply {
        if (buffer.isNotEmpty()) {
            val s = buffer.getString
            if (s.isNotBlank() || isQuoted) add(FlaggedPart(s, isQuoted))
        }
    }

private fun MutableList<Char>.onFinish(inQuotes: Boolean, escaping: Boolean) =
    apply {
        if (inQuotes) add(0, '"')
        if (escaping) add('\\')
    }

private val List<FlaggedPart>.result
    get() = flatMap { part ->
        if (part.quoted) listOf(part.text)
        else part.text.tokenizedSimple
    }

private fun doTokenize(txt: String) : List<String> {
    var inQuotes = false
    var escaping = false
    val partsFlagged = mutableListOf<FlaggedPart>()
    val buffer = mutableListOf<Char>()
    txt.forEachIndexed { index, c ->
        if (c.significantQuote(escaping)) {
            if (startingQuotes(txt, index, inQuotes) || closingQuotes(txt, index, inQuotes)) {
                partsFlagged.addFlaggedPart(buffer, inQuotes)
                inQuotes = !inQuotes
            }
            else buffer.add(c)
        }
        else escaping = buffer.processChar(c, escaping)
    }
    return partsFlagged.addFlaggedPart(buffer.onFinish(inQuotes, escaping), false).result
}

private fun tokenize(txt: String) = with (txt.trim()) {
    if (isEmpty()) emptyList()
    else doTokenize(this)
}

val String.tokenized
    get() = tokenize(this)
