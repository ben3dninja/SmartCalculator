package calculator

import java.math.BigInteger

object Memory {
    private data class Variable(val name: String, var value: BigInteger)

    private val variables = mutableSetOf<Variable>()

    fun getVariableValue(name: String): BigInteger? {
        for (variable in variables) if (variable.name == name) return variable.value
        return null
    }

    private fun getVariable(name: String): Variable? {
        for (variable in variables) if (variable.name == name) return variable
        return null
    }

    fun editOrAdd(name: String, value: BigInteger) {
        val variable = getVariable(name)
        if (variable == null) {
            variables += Variable(name, value)
        } else {
            variable.value = value
        }
    }
}