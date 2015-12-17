package ru.fizteh.fivt.students.okalitova.collectionsql;

import ru.fizteh.fivt.students.okalitova.collectionsql.Aggregators.Aggregator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by nimloth on 16.12.15.
 */
public class Select<T, R> {
    private Iterable<T> iterable;
    private Class<R> myClass;
    private Class joinClass;
    private boolean distinct;
    private boolean isJoin = false;
    private Function<T, ?>[] functions;

    private Predicate<T> wherePredicate;
    private Predicate<R> havingPredicate;

    private int limit = -1;
    private Function<T, ?>[] groupByFunctions;
    private Comparator<R>[] comporators;


    public Select(Iterable<T> initIterable,
                  Class<R> initClass,
                  boolean initDistinct,
                  Function<T, ?>[] initFunctions) {
        iterable = initIterable;
        myClass = initClass;
        distinct = initDistinct;
        functions = initFunctions;
    }

    public Select(List<T> initElements, boolean isDistinct, Function<T, ?> first, Function<T, ?> second) {
        iterable = initElements;
        joinClass = initElements.get(0).getClass();
        distinct = isDistinct;
        functions = new Function[]{first, second};
        isJoin = true;
    }

    public Select<T, R> where(Predicate<T> initPredicate) {
        wherePredicate = initPredicate;
        return this;
    }

    public Select<T, R> limit(int initLimit) {
        limit = initLimit;
        return this;
    }

    public Select<T, R> groupBy(Function<T, ?>... initGroupByFunctions) {
        groupByFunctions = initGroupByFunctions;
        distinct = true;
        return this;
    }

    public Select<T, R> having(Predicate<R> initHavingPredicate) {
        havingPredicate = initHavingPredicate;
        return this;
    }

    public Select<T, R> orderBy(Comparator<R>... initComparators) {
        comporators = initComparators;
        return this;
    }

    public List<R> execute() throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<T> elements = new ArrayList<>();
        for (T it : iterable) {
            elements.add(it);
        }

        if (wherePredicate != null) {
            elements = elements.stream().filter(wherePredicate).collect(Collectors.toList());
        }

        if (distinct) {
            elements = elements.stream().distinct().collect(Collectors.toList());
        }

        if (limit != -1) {
            elements = elements.stream().limit(limit).collect(Collectors.toList());
        }

        Map<Integer, List<T>> resultMap = new HashMap<>();
        if (groupByFunctions != null) {
            Map<Integer, List<T>> groupByResult = elements.stream().collect(Collectors.groupingBy((T elem) -> {
                List<Object> list = new ArrayList<Object>();
                for (int i = 0; i < groupByFunctions.length; ++i) {
                    list.add(groupByFunctions[i].apply(elem));
                }
                return list.hashCode();
            }));
            for (Integer key : groupByResult.keySet()) {
                resultMap.put(key, groupByResult.get(key));
            }
        } else {
            resultMap.put(0, elements);
        }

        List<R> result = new ArrayList<>();
        for (Integer key : resultMap.keySet()) {
            List<Object> currentResult = new ArrayList<>();
            if (groupByFunctions == null) {
                for (T element : resultMap.get(key)) {
                    for (int i = 0; i < functions.length; ++i) {
                        currentResult.add(functions[i].apply(element));
                    }
                }
            } else {
                for (int i = 0; i < functions.length; ++i) {
                    if (functions[i] instanceof Aggregator) {
                        currentResult.add(((Aggregator) functions[i]).apply(resultMap.get(key)));
                    } else {
                        currentResult.add(functions[i].apply(resultMap.get(key).get(0)));
                    }
                }
            }

            Class[] returnClasses = new Class[functions.length];
            for (int j = 0; j < currentResult.size()
                    / functions.length; ++j) {
                Object[] arguments = new Object[functions.length];
                for (int i = 0; i < arguments.length; ++i) {
                    arguments[i] = currentResult.get(j * arguments.length + i);
                    if (arguments[i] != null) {
                        returnClasses[i] = arguments[i].getClass();
                    } else {
                        throw new IllegalStateException("Null result of operation");
                    }
                }

                if (isJoin) {
                    isJoin = false;
               //     Tuple addItem = new joinClass.get(arguments[0], arguments[1]);
                 //   if (havingPredicate == null || havingPredicate.test((R) addItem)) {
                   //     result.add((R) addItem);
                   // }
                } else {

                    R addItem = null;
                    try {
                        addItem = myClass
                                .getConstructor(returnClasses)
                                .newInstance(arguments);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        e.getMessage();
                    }
                    if (havingPredicate == null || havingPredicate.test(addItem)) {
                        result.add(addItem);
                    }
                }
            }
        }
        if (comporators != null) {
            for (Comparator<R> compare : comporators) {
                result.sort(compare);
            }
        }
        return result;
    }

    public Union<T, R> union() {
        return new Union(this);
    }
}
