import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class StartApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        DatabaseConnection.initialize();

        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        try {
            DatabaseConnection.terminate();
        } catch (SQLException e) {
            System.out.println("could not close database connection");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}