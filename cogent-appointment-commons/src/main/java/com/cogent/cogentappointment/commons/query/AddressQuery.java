package com.cogent.cogentappointment.commons.query;

/**
 * @author Sauravi Thapa ON 6/15/20
 */
public class AddressQuery {

    public static String QUERY_TO_GET_LIST_OF_ZONES=
            "SELECT" +
                    " id as id," +                     //[0]
                    " display_name as zoneName" +      //[1]
                    " FROM " +
                    " address a" +
                    " where" +
                    " is_new = 0" +
                    " AND parent_id IS NULL";
}
