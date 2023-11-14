package com.seungminyi.geera.issue.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueConditionsDto {
    private Long project;
    private Long member;
    private String sort;
    private String order;
}
