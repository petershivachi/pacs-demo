package com.shivachi.pacs.demo.auth.data.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@RequiredArgsConstructor
public enum AccessRight {
    //=========================== ROLE MANAGEMENT =================================================
    VIEW_ACCESS_RIGHTS("View Access Rights", "Access Management", "Role Management"),
    CREATE_ROLE("Create Role", "Access Management", "Role Management"),
    UPDATE_ROLE("Update Role", "Access Management", "Role Management"),
    UPDATE_ROLE_STATUS("Update Role Status", "Access Management", "Role Management"),
    VIEW_ROLE_DETAILS("View Role Details", "Access Management", "Role Management"),
    VIEW_ROLES("View Roles", "Access Management", "Role Management"),

    //=========================== USER MANAGEMENT =================================================
    CREATE_USER("Create User", "Access Management", "User Management"),
    UPDATE_USER("Update User", "Access Management", "User Management"),
    DELETE_USER("Delete User", "Access Management", "User Management"),
    UPDATE_USER_PASSWORD("Update User Password", "Access Management", "User Management"),
    UPDATE_USER_ROLE("Update User Role", "Access Management", "User Management"),
    VIEW_USERS("View Users", "Access Management", "User Management"),

    //=========================== IMAGE MANAGEMENT =================================================
    SAVE_IMAGE("Save Image", "Image Management", " ImageManagement"),
    VIEW_IMAGE("View Image", "Image Management", "Image Management");

    private  final String name;
    private final String category;
    private final String subCategory;

    /**
     * fromString - this function maps an access right string to its respective value in the enum. this
     * method ensures that if an access right is deleted the system won't raise any error on startup.
     *
     * @param value: the string representation of the enum value e.g. VERIFY_UGANDA_MTN_C2B_RECON
     * @return null | AccessRight: an access right based on the provided value or null if the value do not exist.
     */
    public static @Nullable AccessRight fromString(String value) {
        try {
            return AccessRight.valueOf(value);
        } catch (Exception ignored) {
            return null;
        }
    }
}
