package com.lunghr.computationalmathlab6.math.multistepmethods

import com.lunghr.computationalmathlab6.dto.Function
import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver
import com.lunghr.computationalmathlab6.math.onestepmethods.RungeKutta.Companion.calculateRungeKuttaStep
import kotlin.math.abs

class Milne : DifferentialEquationSolver() {
    override fun solve(requestData: RequestData) {
        val (hStep, x0, y0, xn, accuracy) = with(requestData) {
            listOf(h!!, x0!!, y0!!, xn!!, accuracy!!).map { it }
        }
        val function = requestData.function!!
        var step = hStep
        var (xMilne, yMilne) = calculateMilneMethod(x0, y0, step, function, x0, xn, y0)
        var r: Double
        var yMilneExact: List<Double>
        do {
            yMilneExact = calculatePrivateSolution(x0, xn, y0, function, step)
            val (xMilneHalf, yMilneHalf) = calculateMilneMethod(x0, y0, step / 2, function, x0, xn, y0)
            r = calculateRungeRule(Pair(xMilne, yMilne), Pair(xMilneHalf, yMilneHalf), 4)
            if (r > accuracy) {
                step /= 2
                val result = calculateMilneMethod(x0, y0, step, function, x0, xn, y0)
                xMilne = result.first
                yMilne = result.second
            }
        } while (r > accuracy)
        println("Milne accuracy: $r")
        responseData = responseData.copy(milne = responseData.milne.copy(x = xMilne, y = yMilne, yExact = yMilneExact))
    }


    private fun calculatePrediction(x: List<Double>, y: List<Double>, h: Double, function: Function, i: Int): Double {
        return y[i - 4] + 4.0 * h / 3.0 * (2 * function.calculate(x[i - 3], y[i - 3]) - function.calculate(
            x[i - 2],
            y[i - 2]
        ) + 2 * function.calculate(x[i - 1], y[i - 1]))
    }


    private fun calculateCorrection(
        x: List<Double>,
        y: List<Double>,
        yPrediction: Double,
        h: Double,
        function: Function,
        i: Int
    ):
            Double = y[i - 2] + h / 3.0 * (function.calculate(x[i - 2], y[i - 2]) + 4.0 * function.calculate(
        x[i - 1], y[i - 1]
    ) + function.calculate(x[i], yPrediction))



    private fun calculateMilneMethod(x: Double, y: Double, step: Double, function: Function, x0:Double, xn: Double, y0:Double ): Pair<List<Double>, List<Double>> {
        val nSteps = (abs(xn - x0) / step).toInt() + 1
        val x = (0 until nSteps).map { x0 + it * step }
        val y = MutableList(nSteps) { 0.0 }.apply {
            this[0] = y0
            (1 until 4).forEach { i ->
                this[i] = calculateRungeKuttaStep(x[i - 1], this[i - 1],step, function)
            }
            (4 until nSteps).forEach { i ->
                this[i] = calculateCorrection(x, this, calculatePrediction(x, this, step, function, i), step, function, i)
            }
        }

        return x to y
    }
}