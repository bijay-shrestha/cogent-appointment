package com.cogent.cogentappointment.commons.query;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
public class AddressQuery {

    public static String QUERY_TO_GET_LIST_OF_ZONES=
            "SELECT" +
                    " id as value," +                     //[0]
                    " displayName as label" +      //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " isNew = 0" +
                    " AND parentId IS NULL";

}
