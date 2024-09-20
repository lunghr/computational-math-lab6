package com.lunghr.computationalmathlab6.dto

data class ResponseData(
    val euler: MethodData = MethodData(),
    val rungeKutta: MethodData = MethodData(),
    val milne: MethodData = MethodData(),
    val privateSolution: String = ""
)

data class MethodData(
    val x: List<Double> = listOf(),
    val y: List<Double> = listOf(),
    val yExact: List<Double> = listOf()
)