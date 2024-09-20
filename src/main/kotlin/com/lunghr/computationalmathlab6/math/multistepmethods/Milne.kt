package com.lunghr.computationalmathlab6.math.multistepmethods

import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver
import com.lunghr.computationalmathlab6.math.onestepmethods.RungeKutta.Companion.calculateRungeKuttaStep
import kotlin.math.abs

class Milne : DifferentialEquationSolver() {
    override fun solve(requestData: RequestData) {
        val (h, x0, y0, xn, accuracy) = with(requestData) {
            listOf(h!!, x0!!, y0!!, xn!!, accuracy!!).map { it }
        }
        val function = requestData.function!!
        val n = ((xn - x0) / h).toInt()
        val yMilneExact = calculatePrivateSolution(x0, xn, y0, function, h)

        val x = MutableList(n + 1) { 0.0 }
        val y = MutableList(n + 1) { 0.0 }

        x[0] = x0
        y[0] = y0
        for (i in 1 until 4) {
            x[i] = x[i - 1] + h
            y[i] = calculateRungeKuttaStep(x[i - 1], y[i - 1], h, function)
        }
        for (i in 3 until n) {
            x[i + 1] = x[i] + h
            var yPredict = y[i - 3] + (4.0 / 3.0) * h * (2 * function.calculate(x[i], y[i]) - function.calculate(x[i - 1], y[i - 1]) + 2 * function.calculate(x[i - 2], y[i - 2]))
            var yCorrect = y[i - 1] + h / 3.0 * (function.calculate(x[i - 1], y[i - 1]) +
                    4.0 * function.calculate(x[i], y[i]) + function.calculate(x[i+1], yPredict))
            while (abs(yCorrect - yPredict) > accuracy) {
                yPredict = yCorrect
                yCorrect = y[i - 1] + h / 3.0 * (function.calculate(x[i - 1], y[i - 1]) +
                        4.0 * function.calculate(x[i], y[i]) + function.calculate(x[i+1], yPredict))
            }
            y[i + 1] = yCorrect
        }

        responseData = responseData.copy(
            milne = responseData.milne.copy(
                x = x,
                y = y,
                yExact = yMilneExact
            )
        )
    }
}
