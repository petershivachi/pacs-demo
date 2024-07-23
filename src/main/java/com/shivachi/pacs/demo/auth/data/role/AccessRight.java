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
    DOWNLOAD_USER_TEMPLATE("Download User Template", "Configurations", "User Management"),
    USER_BULK_UPLOAD("User Bulk Upload", "Configurations", "User Management"),
    REMOVE_USER("User Bulk Upload", "Configurations", "User Management"),

    //=========================== HEXCELL USER MANAGEMENT =================================================
    VIEW_HEXCELL_USER("View HexCell User", "Configurations", "HexCell User Management"),
    UPDATE_HEXCELL_USER("Update HexCell User", "Configurations", "HexCell User Management"),

    //=========================== AREA MANAGEMENT =================================================
    CREATE_AREA("Create Area", "Configurations", "Area Management"),
    UPDATE_AREA("Update Area", "Configurations", "Area Management"),
    UPDATE_AREA_STATUS("Update Area Status", "Configurations", "Area Management"),
    VIEW_AREA_DETAILS("View Area Details", "Configurations", "Area Management"),
    VIEW_AREAS("View Areas", "Configurations", "Area Management"),
    VIEW_AREA_DCUS("View Area DCUs", "Configurations", "Area Management"),
    DOWNLOAD_AREA_TEMPLATE("Download Area Template", "Configurations", "Area Management"),
    AREA_BULK_UPLOAD("Area Bulk Upload", "Configurations", "Area Management"),
    DELETE_AREA("Delete Meter", "Configurations", "Area Management"),

    //=========================== DCUs MANAGEMENT =================================================
    CREATE_DCU("Create Dcu", "Configurations", "Dcu Management"),
    UPDATE_DCU("Update Dcu", "Configurations", "Dcu Management"),
    VIEW_DCUS("View DCUs", "Configurations", "Dcu Management"),
    DOWNLOAD_DCU_TEMPLATE("Download Dcu Template", "Configurations", "Dcu Management"),
    DCU_BULK_UPLOAD("Dcu Bulk Upload", "Configurations", "Dcu Management"),
    DELETE_DCU("Delete Dcu", "Configurations", "Dcu Management"),

    //=========================== METER MANAGEMENT =================================================
    CREATE_METER("Create Meter", "Configurations", "Meter Management"),
    UPDATE_METER("Update Meter", "Configurations", "Meter Management"),
    VIEW_METERS("View Meters", "Configurations", "Meter Management"),
    DOWNLOAD_METER_TEMPLATE("Download Meter Template", "Configurations", "Meter Management"),
    METER_BULK_UPLOAD("Meter Bulk Upload", "Configurations", "Meter Management"),
    USER_METERS("User Meters", "Configurations", "Meter Management"),
    DELETE_METER("Delete Meter", "Configurations", "Meter Management"),

    //=========================== HEXCELL INTEGRATION =================================================
    VIEW_METER_DATA("View Meter Data", "Hex Cell Integration", "Hex Integration"),
    UPDATE_HEX_CELL_USER("Update Hex Cell User", "Hex Cell Integration", "Hex Cell User"),
    VIEW_HEX_CELL_USER("View Hex Cell User", "Hex Cell Integration", "Hex Cell User"),

    //=========================== MPESA INTEGRATION =================================================
    REGISTER_URLS("Register Urls", "Mpesa Integration", "Mpesa Integration"),

    //=========================== MPESA INTEGRATION =================================================
    VIEW_PURCHASES("View Purchases", "Water Purchases", "Purchases"),
    PURCHASES_BY_SHORT_CODE("Purchases By Shortcode", "Water Purchases", "Purchases"),

    //=========================== PAYMENTS =================================================
    VIEW_PAYMENTS("View Payments", "Payments", "Mpesa Payments"),
    PAYMENTS_BY_SHORT_CODE("Payments By Short Code", "Payments", "Mpesa Payments");

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
