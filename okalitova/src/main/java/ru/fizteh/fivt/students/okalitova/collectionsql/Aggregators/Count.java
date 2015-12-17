package ru.fizteh.fivt.students.okalitova.collectionsql.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by nimloth on 17.12.15.
 */
public class Count<T> implements Aggregator<T, Long> {

    private Function<T, ?> function;
    public Count(Function<T, ?> expression) {
        this.function = expression;
    }

    @Override
    public Long apply(List<T> elements) {
        Long longAns = elements
                .stream()
                .map(function)
                .distinct()
                .count();
        return longAns;
    }

    @Override
    public Long apply(T t) {
        return null;
    }
}
