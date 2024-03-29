import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ImgViewMaker {
    public static void addAll(FlowPane board, int[] idxs){
        ImgHashesTableConnection conn = ImgHashesTableConnection.getInstance();

        try {
            for (int i = 0; i < idxs.length; i++) {
                Pane p = FXMLLoader.load(ImgViewMaker.class.getResource("ImgView.fxml"));
                board.getChildren().add(p);
                int idx = idxs[i];
                //set imgview
                Image image = new Image("file:" + conn.getPath(idx));
                ImageView imgView = (ImageView) p.lookup("#imgView");
                imgView.setImage(image);
                //set filename
                Label fileName = (Label) p.lookup("#fileName");
                fileName.setText(conn.getPath(idx).replaceAll(".*[\\/]([^\\/]*$)", "\1"));
                //set idx
                Label idxLabel = (Label) p.lookup("#idx");
                idxLabel.setText(""+idx);
                //set delete
                Button delete = (Button) p.lookup("#delete");
                delete.setText("Delete");
                //set ignore
                Button ignore = (Button) p.lookup("#ignore");
                delete.setText(conn.isIgnore(idx) ? "IGNORED" : "Ignore");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        conn.close();
    }
}
