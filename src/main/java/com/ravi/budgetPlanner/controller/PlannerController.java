package com.ravi.budgetPlanner.controller;

import com.ravi.budgetPlanner.model.PlannerDTO;
import com.ravi.budgetPlanner.model.response.ApiResponse;
import com.ravi.budgetPlanner.model.response.ProjectionsResponseDTO;
import com.ravi.budgetPlanner.service.PlannerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planners")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PlannerController {

    private final PlannerService plannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlannerDTO>>> getAllPlanners(@RequestParam int year, @RequestParam int month){
        List<PlannerDTO> data = plannerService.getAllPlanners(year,month);
        ApiResponse<List<PlannerDTO>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createProjection(@RequestBody PlannerDTO planner){
        plannerService.createBasePlanner(planner);
        ApiResponse<String> body = new ApiResponse<>("Success",null,"Created");
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PostMapping("/subcategories")
    public ResponseEntity<ApiResponse<String>> createSubCategories(@RequestBody PlannerDTO planner){
        plannerService.createSubCategories(planner);
        ApiResponse<String> body = new ApiResponse<>("Success",null,"Created");
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProjection(@PathVariable int id, @RequestBody PlannerDTO planner){
        plannerService.updateProjection(id,planner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProjection(@PathVariable int id){
        plannerService.deleteProjection(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
