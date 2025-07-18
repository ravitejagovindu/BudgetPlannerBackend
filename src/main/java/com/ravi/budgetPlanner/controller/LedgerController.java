package com.ravi.budgetPlanner.controller;

import com.ravi.budgetPlanner.model.LedgerDTO;
import com.ravi.budgetPlanner.model.response.ApiResponse;
import com.ravi.budgetPlanner.model.response.ChartExpenseResponseDTO;
import com.ravi.budgetPlanner.model.response.ChartResponseDTO;
import com.ravi.budgetPlanner.service.LedgerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ledgers")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LedgerDTO>>> getAllLedgers(@RequestParam int year, @RequestParam int month){
        List<LedgerDTO> data = ledgerService.getAllLedgers(year,month);
        ApiResponse<List<LedgerDTO>> body = new ApiResponse<>("Success",null,data);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createLedger(@RequestBody @Valid LedgerDTO ledgerDTO){
        ledgerService.createLedgerDTO(ledgerDTO);
        ApiResponse<String> body = new ApiResponse<>("Success",null,"Created");
        return new ResponseEntity<>(body,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateLedger(@PathVariable int id, @RequestBody LedgerDTO ledgerDTO){
        ledgerService.updateLedger(id,ledgerDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLedger(@PathVariable int id){
        ledgerService.deleteLedger(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
