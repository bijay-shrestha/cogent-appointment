package com.cogent.cogentappointment.admin.utils.commons;

import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author smriti on 2019-08-26
 */
public class QueryUtils {

    public static BiFunction<EntityManager, String, Query> createQuery = (EntityManager::createQuery);

    public static <T> List<T> transformQueryToResultList(Query query, Class<T> requestDTO) {
        return query.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(requestDTO))
                .getResultList();
    }

    public static <T> T transformQueryToSingleResult(Query query, Class<T> requestDTO) {
        return (T) query.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(requestDTO))
                .getSingleResult();
    }

    /*DTO projections using ResultTransformer and a Native SQL query*/
    public static BiFunction<EntityManager, String, Query> createNativeQuery = (EntityManager::createNativeQuery);

    public static <T> List<T> transformNativeQueryToResultList(Query query, Class<T> requestDTO) {
        return query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(requestDTO))
                .getResultList();
    }

    public static <T> T transformNativeQueryToSingleResult(Query query, Class<T> requestDTO) {
        return (T) query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(requestDTO))
                .getSingleResult();
    }

}


