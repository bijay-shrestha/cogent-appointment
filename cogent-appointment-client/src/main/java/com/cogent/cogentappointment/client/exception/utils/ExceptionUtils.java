package com.cogent.cogentappointment.client.exception.utils;

import com.cogent.cogentappointment.client.utils.commons.StringUtil;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author smriti on 7/2/19
 */
public class ExceptionUtils {

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String generateMessage(Class clazz) {
        return "No " + StringUtil.splitByCharacterTypeCamelCase(clazz.getSimpleName()) + "(s) found.";
    }

    public static String generateMessage(Class clazz, String errorMessage) {
        return StringUtil.splitByCharacterTypeCamelCase(clazz.getSimpleName()) + errorMessage;
    }

    public static String generateDebugMessage(Class clazz, String debugMessage) {
        return StringUtil.splitByCharacterTypeCamelCase(clazz.getSimpleName()) + debugMessage;
    }

    public static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) + " was not found for parameters " + searchParams;
    }

    public static String generateDebugMessage(Class clazz) {
        return StringUtil.splitByCharacterTypeCamelCase(clazz.getSimpleName()) + "(s) is empty.";
    }

    public static <K, V> Map<K, V> toMap(Class<K> keyType,
                                         Class<V> valueType,
                                         Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");

        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
