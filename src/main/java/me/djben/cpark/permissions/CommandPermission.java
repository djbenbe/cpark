package me.djben.cpark.permissions;

import java.lang.annotation.*;

/**
 * Declares a cloud-annotated method requires a given permission
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CommandPermissionMulti.class)
public @interface CommandPermission {
    Permissions value();
}

