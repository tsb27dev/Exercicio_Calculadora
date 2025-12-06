package pt.ipca.calculator

import kotlin.math.sqrt

class CalculatorBrain {

    var operand = 0.0
    private var memory = 0.0
    private var pendingOperation: Operation? = null
    private var previousOperand = 0.0

    enum class Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        EQUALS,
        SQUARE_ROOT,
        PERCENT,
        CHANGE_SIGN;

        companion object {
            fun parse(value: String): Operation {
                return when (value) {
                    "+" -> ADD
                    "-" -> SUBTRACT
                    "x", "×" -> MULTIPLY
                    "/", "÷" -> DIVIDE
                    "=" -> EQUALS
                    "√" -> SQUARE_ROOT
                    "%" -> PERCENT
                    "+/-" -> CHANGE_SIGN
                    else -> throw IllegalArgumentException("Invalid operation")
                }
            }
        }
    }

    // Função para operações básicas
    fun doOperation(newOperand: Double, newOperation: Operation): Double {
        when(newOperation) {
            Operation.SQUARE_ROOT -> {
                operand = sqrt(newOperand)
                return operand
            }
            Operation.PERCENT -> {
                operand = newOperand / 100
                return operand
            }
            Operation.CHANGE_SIGN -> {
                operand = -newOperand
                return operand
            }
            Operation.EQUALS -> {
                if (pendingOperation != null) {
                    operand = executeOperation(previousOperand, newOperand, pendingOperation!!)
                    pendingOperation = null
                } else {
                    operand = newOperand
                }
                return operand
            }
            Operation.ADD, Operation.SUBTRACT, Operation.MULTIPLY, Operation.DIVIDE -> {
                if (pendingOperation != null) {
                    operand = executeOperation(previousOperand, newOperand, pendingOperation!!)
                } else {
                    operand = newOperand
                }
                previousOperand = operand
                pendingOperation = newOperation
                return operand
            }
        }
    }

    private fun executeOperation(operand1: Double, operand2: Double, operation: Operation): Double {
        return when(operation) {
            Operation.ADD -> operand1 + operand2
            Operation.SUBTRACT -> operand1 - operand2
            Operation.MULTIPLY -> operand1 * operand2
            Operation.DIVIDE -> {
                if (operand2 != 0.0) operand1 / operand2
                else 0.0 // Evita divisão por zero
            }
            else -> operand2
        }
    }

    // Funções de memória
    fun memoryAdd(value: Double) {
        memory += value
    }

    fun memorySubtract(value: Double) {
        memory -= value
    }

    fun memoryRecall(): Double {
        return memory
    }

    fun memoryClear() {
        memory = 0.0
    }

    // Função para limpar (CE - Clear Entry)
    fun clearEntry(): Double {
        operand = 0.0
        return operand
    }

    // Função para reset completo (ON)
    fun reset() {
        operand = 0.0
        memory = 0.0
        pendingOperation = null
        previousOperand = 0.0
    }

    // Verifica se há algo na memória
    fun hasMemory(): Boolean {
        return memory != 0.0
    }
}