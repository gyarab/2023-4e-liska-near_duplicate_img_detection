import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLimageViewController implements Initializable {
    @FXML
    private Button delete, ignore;
    @FXML
    private Label fileName, idx;

    @FXML
    public void deleteImg(ActionEvent event) {
        ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();
        int id = Integer.parseInt(idx.getText());
        conn.remove(id);
        delete.setText("DELETED");
    }

    @FXML
    public void ignoreImg(ActionEvent event) {
        ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();
        int id = Integer.parseInt(idx.getText());
        String str = ignore.getText();
        ignore.setText(str.equals("Ignore") ? "IGNORED" : "Ignore");
        conn.setIgnore(id);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("imgview ininitialization");
    }
}
