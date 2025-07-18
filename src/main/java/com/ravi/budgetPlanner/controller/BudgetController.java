package com.ravi.budgetPlanner.controller;

import com.ravi.budgetPlanner.model.response.ApiResponse;
import com.ravi.budgetPlanner.model.response.BudgetTypesResponse;
import com.ravi.budgetPlanner.service.BudgetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<BudgetTypesResponse>>> getAllBudgetTypes(){
        List<BudgetTypesResponse> data = budgetService.getAllPlannerTypes();
        ApiResponse<List<BudgetTypesResponse>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

}
