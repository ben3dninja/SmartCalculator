package calculator

fun main() {
    var input = readLine()!!
    while (true) {
        if (input == "") {
        } else if (input.first() == '/') {
            when (input.substring(1)) {
                "help" -> {
                    println("This program is a calculator.")
                    println("Multiple + give one +, even amounts of - give one + and odd amounts of - give one -.")
                }
                "exit" -> break
                else -> println("Unknown command")
            }
        } else {
            if (input.contains("(\\d \\d)".toRegex()) || input.last() in Operator.getSymbols() ||
                    input.last() == '-') {

                println("Invalid expression")
            } else {
                val filtered = filterInput(input)
                if (isOperatorWithInvalidTerms(filtered) || !doParenthesesMatch(filtered)) {
                    println("Invalid expression")
                } else {
                    if (filtered.contains('=')) {
                        if (!"[a-zA-Z]+=.*".toRegex().matches(filtered)) {
                            println("Invalid identifier")
                        } else if (!"[^=]+=[+]?[-]?((([a-zA-Z]+)|(\\d+))[-${Operator.symbols.pattern}]*)+".toRegex().matches(filtered) ||
                            "(\\d[a-zA-Z])|([a-zA-Z]\\d)".toRegex().containsMatchIn(filtered)) {
                            println("Invalid assignment")
                        } else {
                            try {
                                val expression = Expression(filtered.split('=')[1])
                                val value = expression.evaluate()
                                Memory.editOrAdd(filtered.split('=').first(), value)
                            } catch (e: Exception) {
                                println(e.message)
                            }
                        }
                    } else {
                        try {
                            val expression = Expression(filtered)
                            println(expression.evaluate())
                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
                }
            }
        }
        input = readLine()!!
    }
    println("Bye!")
}
