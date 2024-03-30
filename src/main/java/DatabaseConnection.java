import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection connection = null;
    private static Statement statement = null;

    public static void initialize() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + Config.getInstance().database);
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
    }

    public static Connection getConnection(){
        return connection;
    }

    public static Statement getStatement(){
        return statement;
    }

    public static void terminate() throws SQLException {
        statement.close();
        connection.close();
    }
}
