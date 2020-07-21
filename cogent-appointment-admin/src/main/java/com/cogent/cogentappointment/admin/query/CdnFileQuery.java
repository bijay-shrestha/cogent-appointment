package com.cogent.cogentappointment.admin.query;

/**
 * @author rupak ON 2020/07/21-1:21 PM
 */
public class CdnFileQuery {

    public static String QUERY_TO_FETCH_DOCTOR_AVATAR =
            " CASE" +
            " WHEN" +
            " (da.status is null OR da.status = 'N')" +
            " THEN null" +
            " WHEN" +
            " da.fileUri LIKE 'public%'" +
            " THEN" +
            " CONCAT(:cdnUrl,SUBSTRING_INDEX(da.fileUri, 'public', -1))" +
            " ELSE" +
            " da.fileUri" +
            " END as fileUri,";
}
