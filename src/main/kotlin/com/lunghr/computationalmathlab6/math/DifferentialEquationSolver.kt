package com.lunghr.computationalmathlab6.math

import com.lunghr.computationalmathlab6.dto.Function
import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.dto.ResponseData
import com.lunghr.computationalmathlab6.math.multistepmethods.Milne
import com.lunghr.computationalmathlab6.math.onestepmethods.ImprovedEuler
import com.lunghr.computationalmathlab6.math.onestepmethods.RungeKutta
import kotlin.math.abs
import kotlin.math.pow

abstract class DifferentialEquationSolver {

    companion object {
        var responseData: ResponseData = ResponseData()
    }

    abstract fun solve(requestData: RequestData);

    fun calculateResponse(requestData: RequestData): ResponseData {
        CombinedSolver().solve(requestData)
        val C = requestData.function?.solveCauchy(requestData.x0!!, requestData.y0!!)?: throw IllegalArgumentException("C cannot be null")
        responseData = responseData.copy(privateSolution = requestData.function.generatePrivateSolutionString(C))
        return responseData
    }

    fun calculatePrivateSolution(x0: Double, xn: Double, y0: Double, function: Function, step: Double): List<Double> {
        val nSteps = (abs(xn - x0) / step).toInt() + 1
        val x = (0 until nSteps).map { x0 + it * step }
        val yExact = x.map { function.calculatePrivateSolution(it, function.solveCauchy(x0, y0)) }
        return yExact
    }


    fun calculateRungeRule(
        result: Pair<List<Double>, List<Double>>, resultHalf: Pair<List<Double>, List<Double>>, p:
        Int
    ): Double {
        return abs(result.second.last() - resultHalf.second.last()) / (2.0.pow(p) - 1)
    }


    public class CombinedSolver : DifferentialEquationSolver() {
        override fun solve(requestData: RequestData) {
            ImprovedEuler().solve(requestData)
            RungeKutta().solve(requestData)
            Milne().solve(requestData)
        }
    }


}