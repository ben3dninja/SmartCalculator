package calculator

import java.math.BigInteger

enum class Operator(val symbol: Char, val operate: (BigInteger, BigInteger) -> BigInteger) {
    ADD('+', { a, b -> a + b }),
    MULTIPLY('*', { a, b -> a * b}),
    DIVIDE('/', { a, b -> a / b}),
    POWER('^', { a, b -> a.pow(b.toInt()) });

    companion object {
        fun getOperator(symbol: Char): Operator? {
            for (op in values()) {
                if (op.symbol == symbol) return op
            }
            return null
        }

        fun getSymbols(): List<Char> {
            val list = mutableListOf<Char>()
            for (op in Operator.values()) {
                list += op.symbol
            }
            return list
        }

        val lowPrioritySymbolsRegex = "[+]".toRegex()
        val midPrioritySymbolsRegex = "[/*]".toRegex()
        val highPrioritySymbolsRegex = "[\\^]".toRegex()
        val symbols = "[${getSymbols().joinToString("")}]".toRegex()
    }

}
