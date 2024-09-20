package com.lunghr.computationalmathlab6.dto

data class RequestData(
    val function: Function? = null,
    val x0: Double? = null,
    val xn: Double? = null,
    val y0: Double? = null,
    val h: Double? = null,
    val accuracy: Double? = null
)