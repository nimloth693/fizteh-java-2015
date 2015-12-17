package ru.fizteh.fivt.students.okalitova.collectionsql.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by nimloth on 16.12.15.
 */
public class Max<T, R extends Comparable<R>> implements Aggregator<T, R> {

    private Function<T, R> function;
    public Max(Function<T, R> expression) {
        this.function = expression;
    }

    @Override
    public R apply(List<T> elements) {
        return elements
                .stream()
                .map(function)
                .max(R::compareTo)
                .get();
    }

    @Override
    public R apply(T t) {
        return null;
    }
}
