package com.ravi.budgetPlanner.advice;

import com.ravi.budgetPlanner.exception.BadRequestException;
import com.ravi.budgetPlanner.exception.BudgetException;
import com.ravi.budgetPlanner.exception.DataNotFoundException;
import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;
import com.ravi.budgetPlanner.model.response.ApiResponse;
import com.ravi.budgetPlanner.model.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseBudgetAdvice {


    @ExceptionHandler({DataNotFoundException.class, BadRequestException.class, BudgetException.class})
    public ResponseEntity<ApiResponse> handleNotFoundException(BudgetException ex) {
        Error error = new Error(ex.getCode(), ex.getMessage());
        ApiResponse body = new ApiResponse("Failed", error, null);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleInternalServerError(Exception ex) {
        Error error = new Error(ErrorCodes.INTERNAL.getCode(), ErrorCodes.INTERNAL.getMessage());
        ApiResponse body = new ApiResponse("Failed", error, null);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
