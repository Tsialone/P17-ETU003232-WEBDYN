package connection;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UtilDb {

    public static Connection getCon()  throws Exception {
        String url = "jdbc:mysql://localhost:3306/db_s2_ETU003232";
        String user = "root";
        String password = "secret";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connexion MySQL établie !");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return conn;
    }
}
