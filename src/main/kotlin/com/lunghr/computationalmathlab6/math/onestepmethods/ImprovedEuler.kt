package com.lunghr.computationalmathlab6.math.onestepmethods

import com.lunghr.computationalmathlab6.dto.Function
import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver
import kotlin.math.abs

class ImprovedEuler : DifferentialEquationSolver() {
    override fun solve(requestData: RequestData){
        val (hStep, x0, y0, xn, accuracy) = with(requestData) {
            listOf(h!!, x0!!, y0!!, xn!!, accuracy!!).map { it }
        }
        val function = requestData.function!!

        var step = hStep
        var (xList, yList) = calculateImprovedEulerMethod(x0, y0, xn, step, function)
        var r: Double
        var yEulerExact: List<Double>

        do {
            yEulerExact = calculatePrivateSolution(x0, xn, y0, function, step)
            val (xListHalf, yListHalf) = calculateImprovedEulerMethod(x0, y0, xn, step / 2, function)
            r = calculateRungeRule(Pair(xList, yList), Pair(xListHalf, yListHalf), 2)
            if (r > accuracy) {
                step /= 2
                val result = calculateImprovedEulerMethod(x0, y0, xn, step, function)
                xList = result.first
                yList = result.second

            }
        } while (r > accuracy)
        println("Improved Euler accuracy: $r")
        responseData = responseData.copy(euler = responseData.euler.copy(x = xList, y = yList, yExact = yEulerExact))
    }

    private fun calculateSimpleEulerStep(x: Double, y: Double, step: Double, function: Function): Double {
        return y + step * function.calculate(x, y)
    }

    private fun calculateImprovedEulerStep(x: Double, y: Double, step: Double, function: Function): Double {
        return y + step * (function.calculate(x, y) + function.calculate(
            x + step,
            calculateSimpleEulerStep(x, y, step, function)
        )) / 2
    }

    private fun calculateImprovedEulerMethod(x0: Double, y0: Double, xn: Double, step: Double, function: Function)
            : Pair<List<Double>, List<Double>> {
        val nSteps = (abs(xn - x0) / step).toInt() + 1
        val x = (0 until nSteps).map { x0 + it * step }
        val y = MutableList(nSteps) { 0.0 }.apply {
            this[0] = y0
            (1 until nSteps).forEach { i -> this[i] = calculateImprovedEulerStep(x[i - 1], this[i - 1], step, function) }
        }

        return x to y
    }


}