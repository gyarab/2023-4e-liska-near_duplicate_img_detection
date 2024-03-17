import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

public class Img2Hash {
    public static int getHashUsing(String imgPath, String hashMethodName){
        /*
        String filePath = "src/main/Sample.java";
        File file = new File("src/main/java/Sample.java");
        System.out.println(file.exists());
        URLConnection connection = null;
        try {
            connection = file.toURI().toURL().openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mimeType = connection.getContentType();
        System.out.println(mimeType);
        */


        return 1;
    }

    public static void main(String[] args) {
        getHashUsing("","");
    }
}
