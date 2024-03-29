import java.sql.*;
import java.util.ArrayList;

public class CollisionHandler implements AutoCloseable{
    Connection connection;
    Statement statement;
    ImgHashesTableConnection conn;


    public void addPossibleCollision(String path, String methodName, String hash){
        try {
            ResultSet rs = statement.executeQuery("SELECT count(*) AS count FROM collisionsTable " +
                    "WHERE hashMethod='"+methodName+"' AND hash='"+hash+"';");
            int count = rs.getInt("count");
            if(conn.getIdxsOFCollision(methodName, hash).length > 0 && count == 0){
                statement.executeUpdate("INSERT INTO collisionsTable (hashMethod, hash)"+
                        "VALUES ('"+methodName+"', '"+hash+"');");
            }
            conn.add(path, methodName, hash);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int[] getIdxsOfNextCollision() {
        try {
            int[] arr = new int[0];
            ResultSet rs;
            ArrayList<Integer> integerList = new ArrayList<>();
            boolean allIgnored = true;

            rs = statement.executeQuery("SELECT count(*) AS count FROM collisionsTable;");
            if(rs.getInt("count") == 0) allIgnored = false;//will not search through empty

            while (allIgnored){
                rs = statement.executeQuery("SELECT * FROM collisionsTable LIMIT 1;");
                String methodName = rs.getString("hashMethod");
                String hash = rs.getString("hash");
                arr = conn.getIdxsOFCollision(methodName, hash);

                for (int i = 0; i < arr.length; i++) {
                    if(!conn.isIgnore(arr[i])) allIgnored=false;
                }
                if (allIgnored) statement.executeUpdate("DELETE FROM collisionsTable " +
                        "WHERE hashMethod="+methodName+" AND hash="+hash+";");
            }
            return arr;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CollisionHandler getInstance(){
        Config config = Config.getInstance();
        try{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+config.database);
            Statement statement = connection.createStatement();
            ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();
            statement.setQueryTimeout(30);

            //create if not exists
            ResultSet rs = statement.executeQuery("SELECT count(*) AS count FROM sqlite_master " +
                    "WHERE type='table' AND name='collisionsTable';");
            int count = rs.getInt("count");
            if(count == 0){
                statement.executeUpdate("create table collisionsTable (hashMethod string NOT NULL, hash string NOT NULL);");
                statement.executeUpdate("CREATE INDEX index_name ON collisionsTable(hashMethod, hash);");
            }
            CollisionHandler ch = new CollisionHandler(connection, statement, conn);
            return ch;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CollisionHandler(Connection connection, Statement statement, ImgHashesTableConnection conn) {
        this.connection = connection;
        this.statement = statement;
        this.conn = conn;
    }

    @Override
    public void close() throws Exception {
        conn.close();
        statement.close();
        connection.close();
    }
}
