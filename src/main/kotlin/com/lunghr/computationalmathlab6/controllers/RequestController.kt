package com.lunghr.computationalmathlab6.controllers

import com.lunghr.computationalmathlab6.dto.RequestData
import com.lunghr.computationalmathlab6.dto.ResponseData
import com.lunghr.computationalmathlab6.math.DifferentialEquationSolver.CombinedSolver
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RequestController {
    @PostMapping("/diff")
    fun receiveData(@RequestBody requestData: RequestData): ResponseEntity<ResponseData> {
        val solver= CombinedSolver();
        return ResponseEntity.ok(solver.calculateResponse(requestData))
    }


}