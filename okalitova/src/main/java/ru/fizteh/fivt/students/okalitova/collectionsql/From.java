package ru.fizteh.fivt.students.okalitova.collectionsql;

import java.util.function.Function;

/**
 * Created by nimloth on 16.12.15.
 */
public class From<T> {
    private Iterable<T> elements;

    public From(Iterable<T> initElements) {
        elements = initElements;
    }

    public static <T> From<T> from(Iterable<T> iterable) {
        return new From<>(iterable);
    }

    public <R> Select<T, R> select(Class<R> myClass,
                                       Function<T, ?>... functions) {
        return new Select<>(elements, myClass, false, functions);
    }

    public <R> Select<T, R> selectDistinct(Class<R> myClass,
                                   Function<T, ?>... functions) {
        return new Select<>(elements, myClass, true, functions);
    }
}
