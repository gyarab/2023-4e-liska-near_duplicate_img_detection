import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class ImgHashesTableConnection{
    private Config config;

    public static ImgHashesTableConnection getInstance(){
        ImgHashesTableConnection conn = new ImgHashesTableConnection();
        conn.adapt2Config();
        return conn;
    }

    public void add(String path, String methodName, String hash){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT count(*) AS count FROM imgHashesTable WHERE path='"+path+"';");){

            if(rs.getInt("count") == 0){
                DatabaseConnection.getStatement().executeUpdate(
                        "INSERT INTO imgHashesTable (path, " + methodName + ") VALUES ('" + path + "', '" + hash + "');");
            }
            else{
                DatabaseConnection.getStatement().executeUpdate(
                        "UPDATE imgHashesTable SET " + methodName + " = '" + hash + "' " +
                        "WHERE path = '" + path + "'");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int idx){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT * FROM imgHashesTable;");){
            //rm file
            String path = rs.getString("path");
            System.out.println("remove - path: " + path);
            System.out.println(Files.deleteIfExists(Paths.get(path)));

            //rm form table
            DatabaseConnection.getStatement().executeUpdate("DELETE FROM imgHashesTable WHERE idx=" + idx + ";");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath(int idx){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT path FROM imgHashesTable WHERE idx="+idx+";");){
            return  rs.getString("path");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void setIgnore(int idx){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT ignore FROM imgHashesTable WHERE idx="+idx+";");){
            int ignore = rs.getInt("ignore");
            ignore = (ignore == 0) ? 1 : 0;
            DatabaseConnection.getStatement().executeUpdate(
                    "UPDATE imgHashesTable SET ignore="+ignore+" WHERE idx="+idx+";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isIgnore(int idx){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT ignore FROM imgHashesTable WHERE idx="+idx+";");){
            return (rs.getInt("ignore") == 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getIdxsOFCollision(String methodName, String hash){
        try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                "SELECT idx FROM imgHashesTable WHERE "+methodName+"='"+hash+"';");){

            ArrayList<Integer> integerList = new ArrayList<>();
            while (rs.next()) {
                int value = rs.getInt("idx");
                integerList.add(value);
            }
            int[] arr = integerList.stream().mapToInt(Integer::intValue).toArray();
            return arr;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void adapt2Config(){
        try {
            /*
            //statement.executeUpdate("drop table if exists " + tableName);
            // Print column information
            ResultSet rs = statement.executeQuery("PRAGMA table_info(imgHashesTable)");
            System.out.println("Column Name\t\tData Type");
            System.out.println("--------------------------------------");
            while (rs.next()) {
                String columnName = rs.getString("name");
                String dataType = rs.getString("type");
                System.out.println(columnName + "\t\t\t" + dataType);
            }
            System.out.println("--------------------------------------");
            *////
            DatabaseConnection.getStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS imgHashesTable " +
                    "(idx integer NOT NULL, " +
                    "path string NOT NULL, " +
                    "ignore boolean DEFAULT 0 NOT NULL, " +
                    "PRIMARY KEY (idx));" +
                    "CREATE INDEX IF NOT EXISTS path_index ON imgHashesTable (path);");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < config.hashMethods.size(); i++) {
            try (ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(
                    "SELECT COUNT(*) AS num_columns\n" +
                    "FROM pragma_table_info('imgHashesTable') WHERE name = '"+ config.hashMethods.get(i)[0] +"';");){
                //if not exists
                int count = -1; // Default value if no result is found
                if (rs.next()) {
                    count = rs.getInt("num_columns");
                    if(count != 0) continue;
                }//add that column
                DatabaseConnection.getStatement().executeUpdate("ALTER TABLE imgHashesTable " +
                        " ADD COLUMN " + config.hashMethods.get(i)[0] + " string;");
                DatabaseConnection.getStatement().executeUpdate("CREATE INDEX " + config.hashMethods.get(i)[0]+"IDX" +
                        " ON imgHashesTable ("+config.hashMethods.get(i)[0]+")");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

/*
    public void close(){
        try {
            if(connection != null)
                statement.close();
                connection.close();
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
*/

    private ImgHashesTableConnection() {
        this.config = Config.getInstance();
        /*
        try {
            // create a database connection
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+config.database);
            this.statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
