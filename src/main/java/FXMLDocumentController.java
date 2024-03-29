
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class FXMLDocumentController implements Initializable {
    Config config;
    
    @FXML
    private FlowPane boardOfDuplicates;
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
        GatedQuerry gatedQuerry = new GatedQuerry(enterDirectory.getText(), enterHashMethod.getText(), enterPubRepo.getText(), this);
        gatedQuerry.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = Config.getInstance();
        //System.out.println("config.pubRepo = " + config.pubRepo);
        enterHashMethod.setText(config.hashMethods.get(config.def)[0]);
        enterPubRepo.setText(config.pubRepo);

        enterDirectory.setText("C:\\Users\\foxjo\\Documents\\4.E\\dup_image_datasets\\Airbnb Data\\Training Data\\entrance");

        //grid.setGridLinesVisible(true);
        //Piece k = new Piece("Knight");
        //grid.add(k, 4, 4);
    }

    @FXML
    public void viewNextCollision() throws Exception {
        CollisionHandler ch = CollisionHandler.getInstance();
        ImgViewMaker.addAll(boardOfDuplicates, ch.getIdxsOfNextCollision());
        ch.close();
    }

    @FXML
    public void deleteImg(ActionEvent event) {
        ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();

        Node node = (Node) event.getSource();
        Parent parent = node.getParent();
        Label idxLabel = (Label) parent.lookup("#idxLabel");
        int idx = Integer.getInteger(idxLabel.getText());
        conn.remove(idx);

        Button button = (Button) parent.lookup("#delete");
        String str = button.getText();
        button.setText("DELETED");

        File file = new File(conn.getPath(idx));
        if (file.exists()) file.delete();

        conn.close();
    }

    @FXML
    public void ignoreImg(ActionEvent event) {
        ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();

        Node node = (Node) event.getSource();
        Parent parent = node.getParent();
        Label idx = (Label) parent.lookup("#idxLabel");
        conn.setIgnore(Integer.getInteger(idx.getText()));

        Button button = (Button) parent.lookup("#ignore");
        String str = button.getText();
        button.setText(str.equals("Ignore") ? "IGNORED" : "Ignore");

        conn.close();
    }
}
