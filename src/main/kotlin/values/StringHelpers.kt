package cz.mpts.libs.extrautils.kotlin.values

infix fun String.asWordIn(s: String): Boolean =
    trim().run {
        val lookAt = s.trim()
        when {
            isEmpty() -> false
            lookAt.length < length -> false
            this == lookAt -> true
            this !in lookAt -> false
            lookAt.indexOf(this) == 0 && lookAt.substring(length, length + 1).isBlank() -> true
            lookAt.indexOf(this) == 0 -> false
            lookAt.indexOf(this) == lookAt.length - length && lookAt.substring(lookAt.length - length - 1, lookAt.length - length).isBlank() -> true
            lookAt.indexOf(this) == lookAt.length - length -> false
            this envelopedByOthersIn lookAt -> true
            else -> false
        }
    }


private infix fun String.envelopedByOthersIn(s: String): Boolean =
    run {
        val startsAt = s.indexOf(this)
        val endsAt = startsAt + length - 1
        s[startsAt - 1].isBlankOrPunctuation &&
                s[endsAt + 1].isBlankOrPunctuation
    }


private val Char.isBlankOrPunctuation: Boolean
    get() = when {
        isWhitespace() -> true
        isLetterOrDigit() -> false
        this in PUNCTUATIONS -> true
        else -> false
    }


private val PUNCTUATIONS =
    arrayOf('.', ',', ':', ';', '?', '!', '¡', '¿',
            '"', '\'', '„', '“', '”', '»', '«', '›', '‹',
            '(', ')', '[', ']', '{', '}', '|',
           '-', '–', '—')
