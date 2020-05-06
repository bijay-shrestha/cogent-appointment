package com.cogent.cogentappointment.admin.loghandler;

/**
 * @author Rupak
 */
public class LogDescription {

    public static String getSuccessLogDescription(String feature, String action) {

        String logDescription = "";
        if (action.equalsIgnoreCase("Add")) {
            logDescription = feature + " is added successfully...";
        }

        if (action.equalsIgnoreCase("Create") || action.equalsIgnoreCase("Create [Single Tab]")) {
            logDescription = feature + " is created successfully...";
        }

        if (action.equalsIgnoreCase("Delete") || action.equalsIgnoreCase("Delete [Single Tab]")) {
            logDescription = feature + " is deleted successfully...";
        }

        if (action.equalsIgnoreCase("Edit") || action.equalsIgnoreCase("Edit [Single Tab]")) {
            logDescription = feature + " is edited successfully...";
        }

        if (action.equalsIgnoreCase("View") || action.equalsIgnoreCase("View [Single Tab]")) {
            logDescription = feature + " is viewed successfully...";
        }

        if (action.equalsIgnoreCase("Manage") || action.equalsIgnoreCase("Create [Single Tab]")) {
            logDescription = feature + " is managed successfully...";
        }

        if (action.equalsIgnoreCase("Reset Password")) {
            logDescription = feature + "'s password reset successfully...";
        }

        if (action.equalsIgnoreCase("Clone And Add New")) {
            logDescription = feature + " is cloned and added new...";
        }

        if (action.equalsIgnoreCase("Approve") || action.equalsIgnoreCase("Approve [Single Tab]")) {
            logDescription = feature + " is approved successfully...";
        }

        if (action.equalsIgnoreCase("Reject") || action.equalsIgnoreCase("Reject [Single Tab]")) {
            logDescription = feature + " is rejected successfully...";
        }

        if (action.equalsIgnoreCase("Refund") || action.equalsIgnoreCase("Refund [Single Tab]")) {
            logDescription = feature + " is refunded successfully...";
        }

        if (action.equalsIgnoreCase("Forgot Password")) {
            logDescription = feature + " is requested...";
        }

        return logDescription;
    }


    public static String getFailedLogDescription(String feature, String action, int status) {

        String[] featureName = feature.split("\\s+");

        String log = "";
        switch (status) {
            case 401:
                log = feature + " Invalid Credentials...";
                break;
            case 400:
                log = " Invalid Inputs...";
                break;
            case 404:
                log = featureName[0] + " with details not found...";
                break;
            case 409:
                log =  featureName[0]  + " already exist with given details...";
                break;
            case 500:
                log = "Internal Server error...";
                break;
            default:
                log="Process cannot be completed due to exception...";
        }

        return log;

    }
}
