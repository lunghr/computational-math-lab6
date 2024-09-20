package com.lunghr.computationalmathlab6.math.onestepmethods

import com.lunghr.computationalmathlab6.dto.Function
import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver


class RungeKutta : DifferentialEquationSolver() {
    override fun solve(requestData: RequestData) {
        val (hStep, x0, y0, xn, accuracy) = with(requestData) {
            listOf(h!!, x0!!, y0!!, xn!!, accuracy!!).map { it }
        }
        var yRungeKuttaExact: List<Double>
        val function = requestData.function!!
        var h = hStep
        var (xList, yList) = calculateRungeKuttaMethod(x0, xn, y0, h, function)
        var r: Double

        do {
            yRungeKuttaExact = calculatePrivateSolution(x0, xn, y0, function, h)
            val (xListHalf, yListHalf) = calculateRungeKuttaMethod(x0, xn, y0, h / 2, function)
            r = calculateRungeRule(Pair(xList, yList), Pair(xListHalf, yListHalf), 2)
            if (r > accuracy) {
                h /= 2
                val result = calculateRungeKuttaMethod(x0, xn, y0, h, function)
                xList = result.first
                yList = result.second
            }
        } while (r > accuracy)
        responseData = responseData.copy(
            rungeKutta = responseData.rungeKutta.copy(
                x = xList,
                y = yList,
                yExact = yRungeKuttaExact
            )
        )
    }

    companion object {
        fun calculateRungeKuttaStep(x: Double, y: Double, h: Double, function: Function): Double {
            val k1 = h * function.calculate(x, y)
            val k2 = h * function.calculate(x + h / 2, y + k1 / 2)
            val k3 = h * function.calculate(x + h / 2, y + k2 / 2)
            val k4 = h * function.calculate(x + h, y + k3)
            return y + (k1 + 2 * k2 + 2 * k3 + k4) / 6.0
        }
    }


    private fun calculateRungeKuttaMethod(
        x0: Double, xn: Double, y0: Double, h: Double, function: Function
    ): Pair<List<Double>, List<Double>> {
        val n = ((xn - x0) / h).toInt()
        val x = MutableList(n + 1) { 0.0 }
        val y = MutableList(n + 1) { 0.0 }

        x[0] = x0
        y[0] = y0

        for (i in 1..n) {
            x[i] = x[i - 1] + h
            y[i] = calculateRungeKuttaStep(x[i - 1], y[i - 1], h, function)
        }

        return x to y
    }
}

