package ru.fizteh.fivt.students.okalitova.miniorm;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by nimloth on 18.12.15.
 */

public class DatabaseService<T> {
    private Class<T> tableClass;
    private List<Field> columns = new ArrayList<>();
    private String tableName;
    private Field primaryKey;
    private boolean hasTable;
    private static final String DATABASE_URL = "jdbc:h2:~/database";
    private StringBuilder request;

    public DatabaseService(Class<T> initTableClass) throws Exception {
        Class.forName("org.h2.Driver");
        this.tableClass = initTableClass;
        Table table = initTableClass.getAnnotation(Table.class);
        if (table == null) {
            throw new Exception();
        }
        tableName = table.name();
        boolean countKeys = false;
        for (Field field : tableClass.getFields()) {
            if (field.getAnnotation(Column.class) != null) {
                columns.add(field);
            }
            if (field.getAnnotation(PrimaryKey.class) != null) {
                if (field.getAnnotation(Column.class) == null || countKeys) {
                    throw new Exception();
                } else {
                    countKeys = true;
                    primaryKey = field;
                }
            }
        }
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null)) {
                if (resultSet.next()) {
                    hasTable = true;
                }
            }
        }
    }

    public void createTable() throws Exception {
        if (hasTable) {
            throw new Exception();
        }
        request = new StringBuilder("CREATE TABLE ").append(tableName);
        for (Field field : columns) {
            request.append(field.getAnnotation(Column.class).name()).append(" ");
            request.append(SqlType.getSqlType(field.getType()));
            if (field.equals(primaryKey)) {
                request.append(" PRIMARY KEY");
            }
            request.append(",");
        }
        System.out.println(request);
        request.deleteCharAt(request.lastIndexOf(","));
        request.append(");");
        execute();
    }

    public void dropTable() throws Exception {
        if (!hasTable) {
            throw new Exception();
        }
        request = new StringBuilder("DROP TABLE ").append(tableName).append(";");
    }

    void insert(T t) throws Exception {
        if (!hasTable) {
            throw new Exception();
        }
        request = new StringBuilder("INSERT INTO ").append(tableName).append(" ");
        request.append("VALUES (");
        for (Field field : columns) {
            request.append("?").append(",");
        }
        System.out.println(request);
        request.deleteCharAt(request.lastIndexOf(","));
        request.append(");");
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (PreparedStatement statement = connection.prepareStatement(request.toString())) {
                for (int i = 0; i < columns.size(); ++i) {
                    try {
                        statement.setObject(i + 1, columns.get(i).get(t));
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("bad argument for insert");
                    }
                }
                statement.execute();
            }
        }
    }


    public void update(T element) throws Exception, SQLException {
        if (!hasTable) {
            throw new Exception();
        }
        if (primaryKey == null) {
            throw new Exception();
        }
        request = new StringBuilder();
        request.append("UPDATE ").append(tableName).append(" SET ");
        for (Field field : columns) {
            request.append(field.getAnnotation(Column.class).name()).append(" = ?, ");
        }
        request.deleteCharAt(request.lastIndexOf(","));
        request.append(" WHERE ")
                .append(primaryKey.getAnnotation(Column.class).name()).append(" = ?");
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (PreparedStatement statement = connection.prepareStatement(request.toString())) {
                try {
                    for (int i = 0; i < columns.size(); ++i) {
                        statement.setObject(i + 1, columns.get(i).get(element));
                    }
                    statement.setObject(columns.size() + 1, primaryKey.get(element));
                } catch (IllegalAccessException e) {
                    throw new Exception();
                }
                statement.execute();
            }
        }
    }

    void delete(T t) throws Exception {
        if (!hasTable) {
            throw new Exception();
        }
        request = new StringBuilder("DELETE FROM ").append(tableName).append(" ");
        request.append(" WHERE ").append(primaryKey.getAnnotation(Column.class).name()).append("=?");
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (PreparedStatement statement = connection.prepareStatement(request.toString())) {
                statement.setObject(1, primaryKey.get(t));
                statement.execute();
            }
        }
    }

    public <K> T queryById(K key) throws SQLException, InstantiationException, IllegalAccessException {
        request = new StringBuilder().append("SELECT * FROM ").append(tableName).append(" WHERE ")
                .append(primaryKey.getAnnotation(Column.class).name()).append("=").append(key.toString());
        //System.out.println(query.toString());
        ResultSet resultSet;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (Statement statement = connection.createStatement()) {
                resultSet = statement.executeQuery(request.toString());
            }
        }
        if (resultSet.next()) {
            return getElement(resultSet);
        }
        return null;
    }

    public T getElement(ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException {
        T element = tableClass.newInstance();
        for (Field field : columns) {
            if (field.getType().isAssignableFrom(Number.class)) {
                Long currValue = resultSet.getLong(field.getAnnotation(Column.class).name());
                field.set(element, currValue);
            } else if (field.getType() == String.class) {
                String currValue = resultSet.getString(field.getAnnotation(Column.class).name());
                field.set(element, currValue);
            } else {
                Object currValue = resultSet.getObject(field.getAnnotation(Column.class).name());
                field.set(element, currValue);
            }
        }
        return element;
    }

    public <K> List<T> queryForAll() throws SQLException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();
        request = new StringBuilder().append("SELECT * FROM ").append(tableName);
        //System.out.println(query.toString());
        ResultSet resultSet;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (Statement statement = connection.createStatement()) {
                resultSet = statement.executeQuery(request.toString());
            }
        }
        while (resultSet.next()) {
            result.add(getElement(resultSet));
        }
        return result;
    }

    void execute() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(request.toString());
                hasTable = true;
            }
        }
    }
}

