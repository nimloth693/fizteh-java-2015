package ru.fizteh.fivt.students.okalitova.collectionsql;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by nimloth on 16.12.15.
 */
public class Conditions<T> {
    public static <T> Predicate<T> rlike(Function<T, String> expression,
                                         String regexp) {
        return (item -> expression.apply(item).matches(regexp));
    }

    public static <T> Predicate<T> like(Function<T, String> expression,
                                        String pattern) {
        return (item -> expression.apply(item).equals(pattern));
    }

    public static <T> Predicate<T> notNull(Function<T, Object> expression) {
        return (item -> !expression.apply(item).equals(null));
    }
}
