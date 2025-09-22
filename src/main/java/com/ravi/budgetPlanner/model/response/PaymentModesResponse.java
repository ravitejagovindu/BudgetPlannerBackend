package com.ravi.budgetPlanner.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PaymentModesResponse {
    private List<String> modes;
}
