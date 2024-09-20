package com.lunghr.computationalmathlab6.dto

import kotlin.math.exp

enum class Function {
    FIRST {
        override fun calculate(x: Double, y: Double): Double =
            -2 * y

        override fun solveCauchy(x: Double, y: Double): Double =
            y/ exp(-2 * x)

        override fun calculatePrivateSolution(x: Double, C: Double): Double =
            C * exp(-2 * x)

        override fun generatePrivateSolutionString(C: Double): String {
            return "${C} * e^{-2*x}"
        }
    },
    SECOND {
        override fun calculate(x: Double, y: Double): Double = (x + y) / 2

        override fun solveCauchy(x: Double, y: Double): Double = (y + x + 2) / exp(x / 2)

        override fun calculatePrivateSolution(x: Double, C: Double): Double = C * exp(x / 2) - x - 2
        override fun generatePrivateSolutionString(C: Double): String {
            return "${C} * e^{(x / 2)} - x - 2"
        }
    };

    abstract fun calculate(x: Double, y: Double): Double
    abstract fun solveCauchy(x: Double, y: Double): Double
    abstract fun calculatePrivateSolution(x: Double, C: Double): Double
    abstract fun generatePrivateSolutionString(C: Double): String
}
