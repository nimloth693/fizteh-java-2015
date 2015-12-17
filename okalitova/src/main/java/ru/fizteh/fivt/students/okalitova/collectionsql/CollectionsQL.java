package ru.fizteh.fivt.students.okalitova.collectionsql;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by nimloth on 16.12.15.
 */
public class CollectionsQL {

        public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
                InstantiationException, IllegalAccessException {
        }


        public static class Student {
            private final String name;

            private final LocalDate dateOfBith;

            private final String group;

            public String getName() {
                return name;
            }

            public Student(String name, LocalDate dateOfBith, String group) {
                this.name = name;
                this.dateOfBith = dateOfBith;
                this.group = group;
            }

            public LocalDate getDateOfBith() {
                return dateOfBith;
            }

            public String getGroup() {
                return group;
            }

            public long age() {
                return ChronoUnit.YEARS.between(getDateOfBith(), LocalDateTime.now());
            }

            public static Student student(String name, LocalDate dateOfBith, String group) {
                return new Student(name, dateOfBith, group);
            }

            @Override
            public String toString() {
                return "Student{"
                        + "name='" + name + '\''
                        + ", dateOfBith=" + dateOfBith
                        + ", group=" + group
                        + '}';
            }



            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Student student = (Student) o;

                if (name != null) {
                    if (!name.equals(student.name)) {
                        return false;
                    }
                } else {
                    if (student.name != null) {
                        return false;
                    }
                }
                if (dateOfBith != null) {
                    if (!dateOfBith.equals(student.dateOfBith)) {
                        return false;
                    }
                } else {
                    if (student.dateOfBith != null) {
                        return false;
                    }
                }
                if (group != null) {
                    return !!group.equals(student.group);
                } else {
                    return !(student.group != null);
                }

            }

            @Override
            public int hashCode() {
                int result;
                if (name != null) {
                    result = name.hashCode();
                } else {
                    result = 0;
                }
                if (dateOfBith != null) {
                    result = 31 * result + dateOfBith.hashCode();
                } else {
                    result = 31 * result + 0;
                }
                if (group != null) {
                    result = 31 * result + group.hashCode();
                } else {
                    result = 31 * result + 0;
                }
                return result;
            }
        }

        public static class Group {
            private final String group;
            private final String mentor;

            public Group(String group, String mentor) {
                this.group = group;
                this.mentor = mentor;
            }

            public String getGroup() {
                return group;
            }

            public String getMentor() {
                return mentor;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Group group1 = (Group) o;

                if (group != null) {
                    if (!group.equals(group1.group)) {
                        return false;
                    }
                } else {
                    if (group1.group != null) {
                        return false;
                    }
                }
                if (mentor != null) {
                    return !!mentor.equals(group1.mentor);
                } else {
                    return !(group1.mentor != null);
                }

            }

            @Override
            public int hashCode() {
                int result;
                if (group != null) {
                    result = group.hashCode();
                } else {
                    result = 0;
                }
                if (mentor != null) {
                    result = 31 * result + mentor.hashCode();
                } else {
                    result = 31 * result + 0;
                }
                return result;
            }
        }


        public static class Statistics {

            private final String group;
            private Long count = Long.valueOf(0);
            private final Long age;

            public String getGroup() {
                return group;
            }

            public Long getCount() {
                return count;
            }

            public Long getAge() {
                return age;
            }

            public Statistics(String group, Long age, Long count) {
                this.group = group;
                this.count = count;
                this.age = age;
            }

            @Override
            public String toString() {
                return "Statistics{"
                        + "group='" + group + '\''
                        + ", count=" + count
                        + ", age=" + age
                        + '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Statistics that = (Statistics) o;

                if (group != null) {
                    if (!group.equals(that.group)) {
                        return false;
                    }
                } else {
                    if (that.group != null) {
                        return false;
                    }
                }
                if (count != null) {
                    if (!count.equals(that.count)) {
                        return false;
                    }
                } else {
                    if (that.count != null) {
                        return false;
                    }
                }
                if (age != null) {
                    return !!age.equals(that.age);
                } else {
                    return !(that.age != null);
                }

            }

            @Override
            public int hashCode() {
                int result;
                if (group != null) {
                    result = group.hashCode();
                } else {
                    result = 0;
                }
                if (count != null) {
                    result = 31 * result + count.hashCode();
                } else {
                    result = 31 * result + 0;
                }
                if (age != null) {
                    result = 31 * result + age.hashCode();
                } else {
                    result = 31 * result + 0;
                }
                return result;
            }
        }

}
