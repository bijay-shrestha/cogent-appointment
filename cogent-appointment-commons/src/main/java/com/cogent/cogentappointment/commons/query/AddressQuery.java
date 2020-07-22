package com.cogent.cogentappointment.commons.query;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
public class AddressQuery {

    public static String QUERY_TO_GET_LIST_OF_ZONES =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +             //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.isNew = 0" +
                    " AND a.parentId IS NULL" +
                    " AND a.geographyType=0";

    public static String QUERY_TO_GET_LIST_OF_PROVINCE =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +             //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.isNew = 1" +
                    " AND a.parentId IS NULL" +
                    " AND a.geographyType=3";

    public static String QUERY_TO_GET_LIST_OF_DISTRICT_BY_ZONE_ID =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +                   //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.parentId.id=:zoneId" +
                    " AND a.geographyType=1" +
                    " AND a.isNew=0";

    public static String QUERY_TO_GET_LIST_OF_DISTRICT_BY_PROVINCE_ID =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +                   //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.parentId.id=:provinceId" +
                    " AND a.geographyType=1" +
                    " AND a.isNew=1";

    public static String QUERY_TO_GET_LIST_STREET_BY_DISTRICT_ID =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +                   //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.parentId.id=:districtId" +
                    " AND a.geographyType=2" +
                    " AND a.isNew=0";

    public static String QUERY_TO_GET_LIST_MUNICIPALITY_BY_DISTRICT_ID =
            "SELECT" +
                    " a.id as value," +                     //[0]
                    " a.value as label" +                   //[1]
                    " FROM " +
                    " Address a" +
                    " WHERE" +
                    " a.parentId.id=:districtId" +
                    " AND a.geographyType=2" +
                    " AND a.isNew=1";

}
