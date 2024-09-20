package com.lunghr.computationalmathlab6.math.onestepmethods

import com.lunghr.computationalmathlab6.dto.Function
import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver
import kotlin.math.abs


class RungeKutta : DifferentialEquationSolver() {
    override fun solve(requestData: RequestData) {
        val (hStep, x0, y0, xn, accuracy) = with(requestData) {
            listOf(h!!, x0!!, y0!!, xn!!, accuracy!!).map { it }
        }
        val function = requestData.function!!

        var h = hStep
        var (xList, yList) = calculateRungeKuttaMethod(x0, xn, y0, h, function)
        var r: Double
        var yRungeKuttaExact: List<Double>

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
        println("Runge-Kutta accuracy: $r")
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
            return (y + 1 / 6.0 * (firstK(x, y, h, function) + 2 * secondK(x, y, h, function) + 3 *
                    thirdK(x, y, h, function) + fourthK(x, y, h, function)))
        }

        private fun firstK(x: Double, y: Double, h: Double, function: Function): Double = h * function.calculate(x, y)

        private fun secondK(x: Double, y: Double, h: Double, function: Function): Double =
            h * function.calculate(x + h / 2, y + firstK(x, y, h, function) / 2)

        private fun thirdK(x: Double, y: Double, h: Double, function: Function): Double =
            h * function.calculate(x + h / 2, y + secondK(x, y, h, function) / 2)

        private fun fourthK(x: Double, y: Double, h: Double, function: Function): Double =
            h * function.calculate(x + h, y + thirdK(x, y, h, function))
    }


    private fun calculateRungeKuttaMethod(
        x0: Double, xn: Double, y0: Double, h: Double, function: Function
    ): Pair<List<Double>, List<Double>> {
        val nStep = (abs(xn - x0) / h).toInt() + 1

        val x = (0 until nStep).map { x0 + it * h }
        val y = MutableList(nStep) { 0.0 }.apply {
            this[0] = y0
            (1 until nStep).forEach { i ->
                this[i] = calculateRungeKuttaStep(x[i - 1], this[i - 1], h, function)
            }
        }

        return x to y
    }
}

