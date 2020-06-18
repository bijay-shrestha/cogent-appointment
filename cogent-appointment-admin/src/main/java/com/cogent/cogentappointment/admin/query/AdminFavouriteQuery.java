package com.cogent.cogentappointment.admin.query;

/**
 * @author rupak ON 2020/06/16-12:25 PM
 */
public class AdminFavouriteQuery {
    public static final String QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN =
            " SELECT" +
                    " fav.path as value," +                                             //[0]
                    " fav.name as label" +                                            //[1]
                    " FROM Favourite fav" +
                    " WHERE fav.status = 'Y'" +
                    " ORDER BY label ASC";


    public static final String QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN_WITH_ICON =
            " SELECT" +
                    " fav.path as value," +                                             //[0]
                    " CONCAT(fav.iconName, ',', fav.name) as label" +                   //[1]
                    " FROM Favourite fav" +
                    " LEFT JOIN AdminFavourite af ON af.favouriteId.id=fav.id"+
                    " WHERE fav.status = 'Y'" +
                    " ORDER BY label ASC";
}
