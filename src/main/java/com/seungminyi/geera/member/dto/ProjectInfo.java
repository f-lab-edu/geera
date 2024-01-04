package com.seungminyi.geera.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectInfo {
    private Long projectId;
    private String projectName;
    private String creatorName;
    private String creatorEmail;
}
