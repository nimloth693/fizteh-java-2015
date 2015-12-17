package ru.fizteh.fivt.students.collectionsqltest;

import org.junit.Test;
import ru.fizteh.fivt.students.okalitova.collectionsql.CollectionsQL;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static ru.fizteh.fivt.students.okalitova.collectionsql.Aggregates.count;
import static ru.fizteh.fivt.students.okalitova.collectionsql.Aggregates.max;
import static ru.fizteh.fivt.students.okalitova.collectionsql.CollectionsQL.Student.student;
import static ru.fizteh.fivt.students.okalitova.collectionsql.Conditions.rlike;
import static ru.fizteh.fivt.students.okalitova.collectionsql.From.from;
import static ru.fizteh.fivt.students.okalitova.collectionsql.OrderByConditions.asc;
import static ru.fizteh.fivt.students.okalitova.collectionsql.OrderByConditions.desc;
import static ru.fizteh.fivt.students.okalitova.collectionsql.Sources.list;

/**
 * Created by nimloth on 17.12.15.
 */
public class SelectTest {

    @Test
    public void selectTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<CollectionsQL.Statistics> statistics =
        from(list(
                student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                student("smith", LocalDate.parse("1986-08-06"), "495"),
                student("petrov", LocalDate.parse("2006-08-06"), "494")))
                .select(CollectionsQL.Statistics.class,
                        CollectionsQL.Student::getGroup,
                        CollectionsQL.Student::age,
                        CollectionsQL.Student::age)
                .execute();
        CollectionsQL.Statistics item = new CollectionsQL.Statistics("495", Long.parseLong("29"),
                Long.parseLong("29"));
        assertThat(statistics, hasSize(4));
        assertThat(statistics, hasItem(item));
    }

    @Test
    public void whereTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<CollectionsQL.Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"), "494")))
                        .select(CollectionsQL.Statistics.class,
                                CollectionsQL.Student::getGroup,
                                CollectionsQL.Student::age,
                                CollectionsQL.Student::age)
                                .where(rlike(CollectionsQL.Student::getName, ".*ov").and(s -> s.age() > 20))
                        .execute();
        CollectionsQL.Statistics item = new CollectionsQL.Statistics("495", Long.parseLong("29"),
                Long.parseLong("29"));
        assertThat(statistics, hasSize(2));
        assertThat(statistics, hasItem(item));
    }

    @Test
    public void groupByTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<CollectionsQL.Statistics> statistics =
        from(list(
                student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                student("smith", LocalDate.parse("1986-08-06"), "495"),
                student("petrov", LocalDate.parse("2006-08-06"), "494"),
                student("pechatnikov", LocalDate.parse("1996-08-04"), "495")))
                .select(CollectionsQL.Statistics.class,
                        CollectionsQL.Student::getGroup,
                        max(CollectionsQL.Student::age),
                        count(CollectionsQL.Student::getName))
                        .groupBy(CollectionsQL.Student::getGroup)
                .execute();
        CollectionsQL.Statistics item = new CollectionsQL.Statistics("495", Long.parseLong("29"),
                Long.parseLong("3"));
        assertThat(statistics, hasSize(2));
        assertThat(statistics, hasItem(item));
        item = new CollectionsQL.Statistics("494", Long.parseLong("30"),
                Long.parseLong("2"));
        assertThat(statistics, hasItem(item));
    }

    @Test
    public void executeTest() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<CollectionsQL.Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"), "494"),
                        student("pechatnikov", LocalDate.parse("1996-08-04"), "495")))
                        .select(CollectionsQL.Statistics.class,
                                CollectionsQL.Student::getGroup,
                                max(CollectionsQL.Student::age),
                                count(CollectionsQL.Student::getName))
                        .where(rlike(CollectionsQL.Student::getName, ".*ov"))
                        .groupBy(CollectionsQL.Student::getGroup)
                        .having(s -> s.getGroup() == "495")
                        .orderBy(asc(CollectionsQL.Statistics::getGroup), desc(CollectionsQL.Statistics::getCount))
                        .limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .select(CollectionsQL.Statistics.class,
                                CollectionsQL.Student::getGroup,
                                (CollectionsQL.Student::age),
                                (CollectionsQL.Student::age))
                        .execute();
        assertThat(statistics, hasSize(2));
        CollectionsQL.Statistics item = new CollectionsQL.Statistics("495", Long.parseLong("29"),
                Long.parseLong("2"));
        assertThat(statistics, hasItem(item));
        item = new CollectionsQL.Statistics("494", Long.parseLong("30"),
                Long.parseLong("30"));
        assertThat(statistics, hasItem(item));
    }
}
