package com.cogent.cogentappointment.client.loghandler;

/**
 * @author Rupak
 */
public class LogDescription {

    public static String getSuccessLogDescription(String feature, String action) {

        String logDescription = "";
        if (action.equalsIgnoreCase("Add")) {
            logDescription = feature + " is added successfully...";
        }

        if (action.equalsIgnoreCase("Create")) {
            logDescription = feature + " is created successfully...";
        }

        if (action.equalsIgnoreCase("Delete")) {
            logDescription = feature + " is deleted successfully...";
        }

        if (action.equalsIgnoreCase("Edit")) {
            logDescription = feature + " is edited successfully...";
        }

        if (action.equalsIgnoreCase("View")) {
            logDescription = feature + " is viewed successfully...";
        }

        if (action.equalsIgnoreCase("Manage")) {
            logDescription = feature + " is managed successfully...";
        }

        if (action.equalsIgnoreCase("Reset Password")) {
            logDescription = feature + "'s password reset successfully...";
        }

        if (action.equalsIgnoreCase("Clone And Add New")) {
            logDescription = feature + " is cloned and added new...";
        }

        if (action.equalsIgnoreCase("Approve")) {
            logDescription = feature + " is approved successfully...";
        }

        if (action.equalsIgnoreCase("Reject")) {
            logDescription = feature + " is rejected successfully...";
        }

        if (action.equalsIgnoreCase("Approve")) {
            logDescription = feature + " is approved successfully...";
        }

        if (action.equalsIgnoreCase("Refund")) {
            logDescription = feature + " is refunded successfully...";
        }

        return logDescription;
    }

    public static String getFailedLogDescription() {

        return "Process cannot be completed due to exception...";

    }


}
