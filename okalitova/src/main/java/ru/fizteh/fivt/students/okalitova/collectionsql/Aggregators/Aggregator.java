package ru.fizteh.fivt.students.okalitova.collectionsql.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by nimloth on 16.12.15.
 */
public interface Aggregator<T, R> extends Function<T, R> {
    R apply(List<T> elements);
}
