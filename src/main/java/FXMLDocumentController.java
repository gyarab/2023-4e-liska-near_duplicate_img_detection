
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class FXMLDocumentController implements Initializable {
    Config config;
    
    @FXML
    private GridPane grid;
    @FXML
    private TextField enterDirectory, enterPubRepo, enterHashMethod;
    //public static String enteredDir;

    @FXML
    private void searchDirectory(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Window theStage = source.getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(theStage);
        System.out.println(selectedDirectory);

        if(selectedDirectory == null){
            //No Directory selected
        }else{
            String s = selectedDirectory.getAbsolutePath();
            enterDirectory.setText(s);
            //enteredDir = s;
            //FileWriter myWriter = new FileWriter("dirName.txt");
            //myWriter.write(s);
            //myWriter.close();
        }
        //Paths.get(first, more)
    }

    @FXML
    public void runNDID(ActionEvent event){
        System.out.println("runndid");
        GatedQuerry gatedQuerry = new GatedQuerry(enterDirectory.getText(), enterHashMethod.getText(), enterPubRepo.getText());
        gatedQuerry.run();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = Config.getInstance();
        //System.out.println("config.pubRepo = " + config.pubRepo);
        enterHashMethod.setText(config.hashMethods.get(config.def)[0]);
        enterPubRepo.setText(config.pubRepo);

        enterDirectory.setText("C:\\Users\\foxjo\\Documents\\4.E\\dup_image_datasets\\Airbnb Data\\Training Data\\house-exterior");

        //grid.setGridLinesVisible(true);
        //Piece k = new Piece("Knight");
        //grid.add(k, 4, 4);
    }    
    
}
