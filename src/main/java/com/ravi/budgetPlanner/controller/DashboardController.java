package com.ravi.budgetPlanner.controller;

import com.ravi.budgetPlanner.model.response.*;
import com.ravi.budgetPlanner.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/ledgers")
    public ResponseEntity<ApiResponse<List<ChartResponseDTO>>> getAllLedgersByYear(@RequestParam int year){
        List<ChartResponseDTO> data = dashboardService.getAllLedgers(year);
        ApiResponse<List<ChartResponseDTO>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<List<ChartExpenseResponseDTO>>> getAllExpensesByYear(@RequestParam int year){
        List<ChartExpenseResponseDTO> data = dashboardService.getAllExpenses(year);
        ApiResponse<List<ChartExpenseResponseDTO>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/projections")
    public ResponseEntity<ApiResponse<ProjectionsResponseDTO>> getAllProjections(@RequestParam int year, @RequestParam int month){
        ProjectionsResponseDTO data = dashboardService.getAllProjections(year,month);
        ApiResponse<ProjectionsResponseDTO> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/individualBalances")
    public ResponseEntity<ApiResponse<List<IndividualResponseDTO>>> getIndividualBalances(@RequestParam int year, @RequestParam int month){
        List<IndividualResponseDTO> data = dashboardService.getIndividualBalances(year,month);
        ApiResponse<List<IndividualResponseDTO>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

}
