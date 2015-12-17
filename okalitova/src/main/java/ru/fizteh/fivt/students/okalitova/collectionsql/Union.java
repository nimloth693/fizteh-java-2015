package ru.fizteh.fivt.students.okalitova.collectionsql;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by nimloth on 17.12.15.
 */
public class Union<T, R> {
    private List<Select<T, R>> selects = new ArrayList<>();
    private Select<T, R> current;
    private From<T> curFrom;
    public Union(Select<T, R> initSelect) {
        selects.add(initSelect);
    }

    public Union<T, R> from(Iterable<T> iterable) {
        curFrom = new From(iterable);
        return this;
    }

    public Union<T, R> select(Class<R> resultClass,
                                  Function<T, ?>... constructorFunctions)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        current = curFrom.select(resultClass, constructorFunctions);
        return this;
    }

    public Union<T, R> selectDistinct(Class<R> resultClass,
                                          Function<T, ?>... constructorFunctions) {
        current = curFrom.selectDistinct(resultClass, constructorFunctions);
        return this;
    }

    public Union<T, R> orderBy(Comparator<R>... comparators) {
        current = current.orderBy(comparators);
        return this;
    }

    public Union<T, R> limit(int initLimit) {
        current = current.limit(initLimit);
        return this;
    }

    public Union<T, R> where(Predicate<T> predicate) {
        current = current.where(predicate);
        return this;
    }

    public Union<T, R> having(Predicate<R> predicate) {
        current = current.having(predicate);
        return this;
    }

    public Union<T, R> groupBy(Function<T, ?>... groupByFunctions) {
        current = current.groupBy(groupByFunctions);
        return this;
    }

    public Union<T, R> union() {
        selects.add(current);
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        List<R> result = new ArrayList<>();
        selects.add(current);
        for (Select<T, R> select : selects) {
            result.addAll(select.execute());
        }
        return result;
    }
}
