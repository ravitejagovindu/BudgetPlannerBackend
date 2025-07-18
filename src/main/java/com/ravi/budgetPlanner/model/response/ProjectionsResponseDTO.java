package com.ravi.budgetPlanner.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectionsResponseDTO {
    private int balanceProjected;
    private int balanceActual;
    private int balanceDifference;
    private List<ProjectionsByType> projectionsByType;
}
