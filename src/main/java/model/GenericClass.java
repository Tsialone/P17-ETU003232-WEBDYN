package model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import connection.UtilDb;

public class GenericClass extends BaseObject {

    public GenericClass(int id) {
        super(id);
    }
    public GenericClass() {
        super();
    }

    @Override
    public List<Object> findAll() throws Exception {
        String className = this.getClass().getSimpleName();
        List<Object> liste = new ArrayList<>();

        String sql = "SELECT * FROM " + className;
        try (Connection con = UtilDb.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Object obj = this.getClass().getDeclaredConstructor().newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);

                    Field field = findFieldInClassHierarchy(obj.getClass(), columnName);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(obj, value);
                    } else {
                        System.out.println("Field not found: " + columnName);
                    }
                }

                liste.add(obj);
            }

        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            System.out.println("Erreur lors de la récupération des données : " + e.getMessage());
            e.printStackTrace();
        }

        return liste;
    }

    private Field findFieldInClassHierarchy(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return findFieldInClassHierarchy(superClass, fieldName);
            } else {
                return null;
            }
        }
    }

    @Override
    public void save() throws Exception {
        Class<?> hisClass = this.getClass();
        String className = hisClass.getSimpleName();

        List<Field> allFields = new ArrayList<>();

        while (hisClass != null) {
            Field[] fields = hisClass.getDeclaredFields();
            allFields.addAll(Arrays.asList(fields));
            hisClass = hisClass.getSuperclass();
        }
        System.out.println(allFields);

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        boolean first = true;
        for (Field field : allFields) {
            field.setAccessible(true);

            if ("id".equals(field.getName())) {
                if (getId() == 0) {
                    continue;
                }
            }

            if (!first) {
                columns.append(", ");
                placeholders.append(", ");
            }

            columns.append(field.getName());
            placeholders.append("?");
            first = false;
        }

        if (columns.length() == 0) {
            throw new SQLException("Aucune colonne à insérer (peut-être un problème avec les champs de la classe).");
        }

        String sql = "INSERT INTO " + className + " (" + columns + ") VALUES (" + placeholders + ")";
        System.out.println("Requête SQL générée : " + sql);

        try (Connection con = UtilDb.getCon(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            int paramIndex = 1;
            for (Field field : allFields) {
                field.setAccessible(true);

                if ("id".equals(field.getName())) {
                    if (getId() != 0) {
                        pstmt.setInt(paramIndex++, getId());
                    }
                    continue;
                }

                Object value = field.get(this);
                pstmt.setObject(paramIndex++, value);
            }

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(className + " ajouté avec succès !");
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Object getById(int id) throws Exception {
        String className = this.getClass().getSimpleName();
        Object obj = null;

        String sql = "SELECT * FROM " + className + " WHERE id = ?";
        System.out.println("Requête SQL générée : " + sql);

        try (Connection con = UtilDb.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    obj = this.getClass().getDeclaredConstructor().newInstance();

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);

                        Field field = findFieldInClassHierarchy(obj.getClass(), columnName);
                        if (field != null) {
                            field.setAccessible(true);
                            field.set(obj, value);
                        } else {
                            System.out.println("Champ non trouvé : " + columnName);
                        }
                    }
                } else {
                    System.out.println("Aucun résultat trouvé avec l'ID : " + id);
                }

            }

        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Erreur lors de la récupération par ID : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return obj;
    }

    @Override
    public void update() throws Exception {
        String className = this.getClass().getSimpleName();
        StringBuilder sql = new StringBuilder("UPDATE " + className + " SET ");
        List<Object> values = new ArrayList<>();

        Class<?> currentClass = this.getClass();
        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(this);

                if (fieldValue != null && !fieldName.equalsIgnoreCase("id")) {
                    sql.append(fieldName).append(" = ?, ");
                    values.add(fieldValue);
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        if (sql.toString().endsWith(", ")) {
            sql.delete(sql.length() - 2, sql.length());
        }
        sql.append(" WHERE id = ?");

        Field idField = findFieldInClassHierarchy(this.getClass(), "id");
        if (idField == null) {
            throw new NoSuchFieldException("Le champ 'id' n'a pas été trouvé dans la hiérarchie des classes.");
        }
        idField.setAccessible(true);
        Object idValue = idField.get(this);
        values.add(idValue);
        try (Connection con = UtilDb.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Aucune ligne mise à jour. Vérifiez l'ID.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete() throws Exception {
        Class<?> hisClass = this.getClass();
        String className = hisClass.getSimpleName();

        Field idField = findFieldInClassHierarchy(hisClass, "id");
        if (idField == null) {
            throw new NoSuchFieldException("Le champ 'id' n'a pas été trouvé dans la hiérarchie des classes.");
        }
        idField.setAccessible(true);
        int id = (int) idField.get(this);

        if (id == 0) {
            throw new SQLException("L'ID de l'objet n'est pas défini.");
        }
        String sql = "DELETE FROM " + className + " WHERE id = ?";
        System.out.println("Requête SQL générée : " + sql);

        try (Connection con = UtilDb.getCon(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println(className + " supprimé avec succès !");
            } else {
                System.out.println("Aucun " + className + " trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object> findAllPaginated(int index, int count) throws Exception {
        List<Object> resultList = new ArrayList<>();
        String className = this.getClass().getSimpleName();

        String sql = "SELECT * FROM " + className + " LIMIT ? OFFSET ?";
        System.out.println("Requête SQL générée : " + sql);

        try (Connection con = UtilDb.getCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, count);
            pstmt.setInt(2, index);

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Object obj = this.getClass().getDeclaredConstructor().newInstance();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);

                        Field field = findFieldInClassHierarchy(obj.getClass(), columnName);
                        if (field != null) {
                            field.setAccessible(true);
                            field.set(obj, value);
                        } else {
                            System.out.println("Field not found: " + columnName);
                        }
                    }

                    resultList.add(obj);
                }
            }

        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            System.out.println("Erreur lors de la récupération des données paginées : " + e.getMessage());
            throw e;
        }

        return resultList;
    }
}
