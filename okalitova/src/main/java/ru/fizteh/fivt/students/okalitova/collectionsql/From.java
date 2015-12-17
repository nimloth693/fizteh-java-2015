package ru.fizteh.fivt.students.okalitova.collectionsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Created by nimloth on 16.12.15.
 */
public class From<T> {
    private List<T> elements = new ArrayList<>();

    public From(Iterable<T> initElements) {
        for (T elem : initElements) {
            elements.add(elem);
        }
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
    public final <F, S> Select<T, Tuple<F, S>> select(Function<T, F> first, Function<T, S> second) {
        return new Select<>(elements, false, first, second);
    }


    public <J> Join<T, J> join(Iterable<J> iterable) {
        return new Join<>(elements, iterable);
    }

    public class Join<T, J> {
        private List<T> firstElements = new ArrayList<>();
        private List<J> secondElements = new ArrayList<>();
        private List<Tuple<T, J>> elements = new ArrayList<>();
        Join(List<T> initElements, Iterable<J> initIterable) {
            for (T elem : initElements) {
                firstElements.add(elem);
            }
            for (J it : initIterable) {
                secondElements.add(it);
            }
        }
        public From<Tuple<T, J>> on(BiPredicate<T, J> predicate) {
            for (T first : firstElements) {
                for (J second : secondElements) {
                    if (predicate.test(first, second)) {
                        elements.add(new Tuple<>(first, second));
                    }
                }
            }
            return new From<>(elements);
        }

        public <K extends Comparable<?>> From<Tuple<T, J>> on(
                Function<T, K> leftKey,
                Function<J, K> rightKey) {
            HashMap<K, List<J>> map = new HashMap<>();
            for (J element : secondElements) {
                K key = rightKey.apply(element);
                if (!map.containsKey(key)) {
                    map.put(key, new ArrayList<>());
                }
                map.get(key).add(element);
            }
            for (T first : firstElements) {
                K key = leftKey.apply(first);
                if (map.containsKey(key)) {
                    List<J> second = map.get(key);
                    second.forEach(s -> elements.add(new Tuple<>(first, s)));
                }
            }
            return new From<>(elements);
        }
    }
}
