package com.cogent.cogentappointment.client.query;

/**
 * @author rupak ON 2020/06/16-12:38 PM
 */
public class AdminFavouriteQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN =
            " SELECT" +
                    " fav.path as value," +                                             //[0]
                    " fav.name as label" +                                            //[1]
                    " FROM Favourite fav" +
                    " WHERE fav.status = 'Y'" +
                    " ORDER BY label ASC";
}
