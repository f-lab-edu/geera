package com.seungminyi.geera.utill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.seungminyi.geera.project.ProjectMemberRoleType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProjectPermissionCheck {
    ProjectMemberRoleType value();
}
