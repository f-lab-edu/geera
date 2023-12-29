package com.seungminyi.geera.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ProjectTeamMember {
    private Long memberId;
    private String name;
    private String email;
}
