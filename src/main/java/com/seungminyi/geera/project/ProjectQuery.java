package com.seungminyi.geera.project;

import lombok.Builder;
import lombok.Data;

@Builder
public class ProjectQuery {
    private Long memberId;
    private String sortKey;
    private String sortOrder;
    private int start;
    private int size;
}
