
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class FXMLDocumentController implements Initializable {
    Config config;
    
    @FXML
    private FlowPane boardOfDuplicates;
    @FXML
    private TextField enterDirectory, enterMiddlePoint, enterHashMethod;

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
        }
    }

    @FXML
    public void runNDID(ActionEvent event){
        System.out.println("runndid");
        GatedQuerry gatedQuerry = new GatedQuerry(enterDirectory.getText(), enterHashMethod.getText(), enterMiddlePoint.getText(), this);
        gatedQuerry.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = Config.getInstance();

        enterHashMethod.setText(config.hashMethods.get(config.def)[0]);
        enterMiddlePoint.setText(config.middlePoint);

        enterDirectory.setText("C:\\Users\\foxjo\\Documents\\4.E\\dup_image_datasets\\jaj - kopie");
    }

    @FXML
    public void viewNextCollision() throws Exception {
        CollisionHandler ch = CollisionHandler.getInstance();
        boardOfDuplicates.getChildren().clear();
        ImgViewMaker.addAll(boardOfDuplicates, ch.getIdxsOfNextCollision());
        //ch.close();
    }

}
