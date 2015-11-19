package io.projuice.annotations;

import io.projuice.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.projuice.model.Role.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface ProjectRoleCheck {
	Role[] value() default ADMIN;
}
