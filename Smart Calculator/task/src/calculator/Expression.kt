package calculator

import java.math.BigInteger

class Expression(expression: String) {
    private var tempExpression = if (expression[0] == '+') "0$expression" else expression

    fun evaluate(): BigInteger {
        while (isVariableLeft(tempExpression)) {
            val variableIndex = getFirstVariableIndex(tempExpression)!!
            val variableName = getFirstVariableName(tempExpression)!!
            if (Memory.getVariableValue(variableName) == null) {
                throw RuntimeException("Unknown variable")
            } else {
                tempExpression = replaceVariableWithValue(tempExpression, variableIndex, variableName, Memory.getVariableValue(variableName)!!)
            }
        }
        while (isParenthesesLeft(tempExpression)) {
            val index1 = getFirstParenthesesIndex(tempExpression)!!
            val index2 = getMatchingParenthesesIndex(tempExpression, index1)!!
            tempExpression = replaceParenthesesWithResult(tempExpression, index1, index2)
        }
        while (isHighPriorityOperatorLeft(tempExpression)) {
            val operatorIndex = getFirstHighPriorityOperatorIndex(tempExpression)!!
            val operation = destructOperation(operatorIndex)
            tempExpression = replaceOperationWithResult(tempExpression, operation, operatorIndex)
        }
        while (isMidPriorityOperatorLeft(tempExpression)) {
            val operatorIndex = getFirstMidPriorityOperatorIndex(tempExpression)!!
            val operation = destructOperation(operatorIndex)
            tempExpression = replaceOperationWithResult(tempExpression, operation, operatorIndex)
        }
        while (isLowPriorityOperatorLeft(tempExpression)) {
            val operatorIndex = getFirstLowPriorityOperatorIndex(tempExpression)!!
            val operation = destructOperation(operatorIndex)
            tempExpression = replaceOperationWithResult(tempExpression, operation, operatorIndex)
        }
        return BigInteger(tempExpression)
    }

    private fun destructOperation(operatorIndex: Int): Triple<BigInteger, Operator, BigInteger> {
        val operator = Operator.getOperator(tempExpression[operatorIndex])!!
        val leftTerm = getLastIntIn(tempExpression.substring(0, operatorIndex))
        val rightTerm = getFirstIntIn(tempExpression.substring(operatorIndex + 1))
        return Triple(leftTerm, operator, rightTerm)
    }

    private fun isParenthesesLeft(expression: String): Boolean {
        return "\\(".toRegex().containsMatchIn(expression)
    }

    private fun isLowPriorityOperatorLeft(expression: String): Boolean {
        return Operator.lowPrioritySymbolsRegex.containsMatchIn(expression)
    }

    private fun isHighPriorityOperatorLeft(expression: String): Boolean {
        return Operator.highPrioritySymbolsRegex.containsMatchIn(expression)
    }

    private fun isMidPriorityOperatorLeft(expression: String): Boolean {
        return Operator.midPrioritySymbolsRegex.containsMatchIn(expression)
    }

    private fun isVariableLeft(expression: String): Boolean {
        return "[a-zA-Z]".toRegex().containsMatchIn(expression)
    }

    private fun getFirstVariableIndex(expression: String): Int? {
        return getRegexFirstIndex("[a-zA-Z]".toRegex(), expression)
    }

    private fun getFirstVariableName(expression: String): String? {
        val index: Int? = getFirstVariableIndex(expression) ?: return null
        var name = ""
        for (letter in expression.substring(index!!)) {
            if (!letter.isLetter()) {
                break
            } else {
                name += letter
            }
        }
        return name
    }

    private fun getFirstLowPriorityOperatorIndex(expression: String): Int? {
        return getRegexFirstIndex(Operator.lowPrioritySymbolsRegex, expression)
    }

    private fun getFirstMidPriorityOperatorIndex(expression: String): Int? {
        return getRegexFirstIndex(Operator.midPrioritySymbolsRegex, expression)
    }

    private fun getFirstHighPriorityOperatorIndex(expression: String): Int? {
        return getRegexFirstIndex(Operator.highPrioritySymbolsRegex, expression)
    }

    private fun getFirstParenthesesIndex(expression: String): Int? {
        return getRegexFirstIndex("\\(".toRegex(), expression)
    }

    private fun replaceOperationWithResult(expression: String, operation: Triple<BigInteger, Operator, BigInteger>,
                                           operatorIndex: Int): String {
        val (leftTerm, operator, rightTerm) = operation
        return expression.substring(0, operatorIndex - leftTerm.toString().length) +
                operator.operate(leftTerm, rightTerm).toString() +
                expression.substring(operatorIndex + rightTerm.toString().length + 1)
    }

    private fun replaceVariableWithValue(expression: String, index: Int, name: String, value: BigInteger): String {
        return expression.substring(0, index) + value + expression.substring(index + name.length)
    }

    private fun replaceParenthesesWithResult(expression: String, index1: Int, index2: Int): String {
        val midExpression = Expression(expression.substring(index1 + 1, index2))
        val result = midExpression.evaluate()
        return expression.substring(0, index1) + result + expression.substring(index2 + 1)
    }
}