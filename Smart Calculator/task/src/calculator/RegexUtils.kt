package calculator

import java.math.BigInteger

fun getRegexFirstIndex(regex: Regex, expression: String): Int? {
    val result = regex.find(expression) ?: return null
    return result.range.first
}

fun getRegexLastIndex(regex: Regex, expression: String): Int {
    val result = regex.findAll(expression)
    return result.last().range.first
}

fun getMatchingParenthesesIndex(expression: String, index: Int): Int? {
    var counter = 1
    val domain = expression.substring(index + 1)
    for (i in domain.indices) {
        if (domain[i] == '(') counter++
        if (domain[i] == ')') counter --
        if (counter == 0) return index + i + 1
    }
    return null
}

fun getFirstIntIn(expression: String): BigInteger {
    var counter = 0
    while (counter <= expression.lastIndex && Regex("[-\\d]").matches(expression[counter].toString())) counter++
    return BigInteger(expression.substring(0, counter))
}

fun getLastIntIn(expression: String): BigInteger {
    var counter = expression.lastIndex
    while (counter >= 0 && Regex("[-\\d]").matches(expression[counter].toString())) counter--
    return BigInteger(expression.substring(counter + 1, expression.length))
}

fun filterInput(input: String): String {
    return input
        .replace(" +".toRegex(), "")
        .replace("[+]*-[+]*".toRegex(), "-")
        .replace("(([-]{2})+)".toRegex(), "+")
        .replace("[+]{2,}".toRegex(), "+")
        .replace("[+]*-[+]*".toRegex(), "-")
        .replace("-".toRegex(), "+-")
}

fun doParenthesesMatch(expression: String): Boolean {
    var counter = 0
    for (char in expression) {
        if (char == '(') counter ++
        if (char == ')') counter --
    }
    return counter == 0
}

fun isOperatorWithInvalidTerms(expression: String): Boolean {
    return "([^\\da-zA-Z)][*/^])|([*/^][^\\da-zA-Z(])".toRegex().containsMatchIn(expression)
}
