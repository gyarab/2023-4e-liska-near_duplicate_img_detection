import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class ImgHashesTableConnection implements AutoCloseable{
    private Connection connection;
    private Statement statement;
    private Config config;
    //String imgHashesTable = "imgHashesTable";
    //String collisionsTable = "collisionsTable";
    private String imgHashesTable = "imgHashesTable";

    public static ImgHashesTableConnection getInstance(){
        ImgHashesTableConnection conn = new ImgHashesTableConnection();
        conn.adapt2Config();
        return conn;
    }

    public void add(String path, String methodName, String hash){
        try {
            statement.executeUpdate("INSERT INTO imgHashesTable (path, "+methodName+")"+
                    "VALUES ('"+path+"', '"+hash+"');");

            ResultSet rs = statement.executeQuery("SELECT * FROM imgHashesTable;");
            while (rs.next()) {
                System.out.println("path: " + rs.getString("path"));
                System.out.println(config.hashMethods.get(config.def)[0]+ ": " + rs.getString(config.hashMethods.get(config.def)[0]));
                // Add more print statements for additional columns if needed
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int idx){
        try {//rm file
            ResultSet rs = statement.executeQuery("SELECT path FROM imgHashesTable WHERE idx="+idx+";");
            String path = rs.getString("path");
            Files.deleteIfExists(Paths.get(path));
            //rm form table
            statement.executeUpdate("DELETE FROM imgHashesTable WHERE idx="+idx+";");

            //ResultSet rs2 = statement.executeQuery("SELECT * FROM "+ tableName +";");
            //System.out.println(rs2.getString("path"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath(int idx){
        try {
            ResultSet rs = statement.executeQuery("SELECT path FROM imgHashesTable WHERE idx="+idx+";");
            return rs.getString("path");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void setIgnore(int idx){
        try {
            ResultSet rs = statement.executeQuery("SELECT ignore FROM imgHashesTable WHERE idx="+idx+";");
            int ignore = rs.getInt("ignore");
            ignore = (ignore == 0) ? 1 : 0;
            statement.executeUpdate("UPDATE imgHashesTable SET ignore="+ignore+",WHERE idx="+idx+";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isIgnore(int idx){
        try {
            ResultSet rs = statement.executeQuery("SELECT ignore FROM imgHashesTable WHERE idx="+idx+";");
            return (rs.getInt("ignore") == 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getIdxsOFCollision(String methodName, String hash){
        try {
            ResultSet rs = statement.executeQuery("SELECT idx FROM imgHashesTable WHERE "+methodName+"='"+hash+"';");

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
            statement.executeUpdate("create table if not exists imgHashesTable " +
                    "(idx integer NOT NULL, " +
                    "path string NOT NULL, " +
                    "ignore boolean DEFAULT 0 NOT NULL, " +
                    "PRIMARY KEY (idx))");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < config.hashMethods.size(); i++) {
            try { //if not exists
                ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS num_columns\n" +
                        "FROM pragma_table_info('imgHashesTable')\n" +
                        "WHERE name = '"+ config.hashMethods.get(i)[0] +"';");
                int count = -1; // Default value if no result is found
                if (rs.next()) {
                    count = rs.getInt("num_columns");
                    if(count != 0) continue;
                }//add that column
                statement.executeUpdate("ALTER TABLE imgHashesTable " +
                        " ADD COLUMN " + config.hashMethods.get(i)[0] + " string;");
                statement.executeUpdate("CREATE INDEX " + config.hashMethods.get(i)[0]+"IDX" +
                        " ON imgHashesTable ("+config.hashMethods.get(i)[0]+")");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


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

    private ImgHashesTableConnection() {
        this.config = Config.getInstance();
        try {
            // create a database connection
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+config.database);
            this.statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
