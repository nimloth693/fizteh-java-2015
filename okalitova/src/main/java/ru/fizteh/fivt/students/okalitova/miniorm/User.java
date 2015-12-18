package ru.fizteh.fivt.students.okalitova.miniorm;


import java.util.List;

/**
 * Created by nimloth on 18.12.15.
 */

public class User {
    @Table(name = "TESTTABLE")
    static class Tab {
        @PrimaryKey
        @Column(name = "ID")
        private Integer a;

        @Column(name = "STRING")
        private String s;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Tab tab = (Tab) o;

            if (a != null) {
                if (!a.equals(tab.a)) {
                    return false;
                }
            } else {
                if (tab.a != null) {
                    return false;
                }
            }
            if (s != null) {
                return !!s.equals(tab.s);
            } else {
                return !(tab.s != null);
            }

        }

        @Override
        public int hashCode() {
            int result;
            if (a != null) {
                result = a.hashCode();
            } else {
                result = 0;
            }
            if (s != null) {
                result = 31 * result + s.hashCode();
            } else {
                result = 31 * result + 0;
            }
            return result;
        }

        Tab(Object a, Object s) {
            this.a = (Integer) a;
            this.s = (String) s;
        }

        Tab() {
            this.a = 0;
            this.s = "";
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Id = ").append(a).append(", String = ").append(s);
            return result.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        DatabaseService<Tab> bd = new DatabaseService<>(Tab.class);
        //try {
            bd.createTable();
            bd.insert(new Tab(1, "one"));
            List<Tab> all = bd.queryForAll();
            all.forEach(System.out::println);
            //bd.insert(new Tab(2, "two"));
            //bd.insert(new Tab(3, "three"));
            //bd.insert(new Tab(4, "four"));
            //Tab elem = bd.queryById(3);
            //System.out.println(elem);
            //all = bd.queryForAll();
            //all.forEach(System.out::println);
        //} finally {
            //bd.dropTable();
        //}
    }
}
