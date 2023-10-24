package com.seungminyi.geera.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;


@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotNull
    private String email;
    @JsonIgnore
    @NotNull
    private String password;
    @NotNull
    private String name;
}
