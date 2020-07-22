package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti ON 2020/07/21-1:21 PM
 */
class CdnFileQuery {

    static String QUERY_TO_FETCH_DOCTOR_AVATAR =
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
                    " END as fileUri";

    static String QUERY_TO_FETCH_DOCTOR_AVATAR_NATIVE =
            " CASE" +
                    " WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " da.file_uri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl, SUBSTRING_INDEX(da.file_uri, 'public', -1))" +
                    " ELSE" +
                    " da.file_uri" +
                    " END as fileUri";

    static String QUERY_TO_FETCH_ADMIN_AVATAR =
            " CASE" +
                    " WHEN" +
                    " (av.status is null OR av.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " av.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(av.fileUri, 'public', -1))" +
                    " ELSE" +
                    " av.fileUri" +
                    " END as fileUri";

    static String QUERY_TO_FETCH_HOSPITAL_LOGO =
            " CASE" +
                    " WHEN" +
                    " (hl.status is null OR hl.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " hl.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(hl.fileUri, 'public', -1))" +
                    " ELSE" +
                    " hl.fileUri";
}
