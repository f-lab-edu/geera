package com.seungminyi.geera.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectQuery {
    private Long memberId;
    private String sortKey;
    private String sortOrder;
    private int start;
    private int size;
}
