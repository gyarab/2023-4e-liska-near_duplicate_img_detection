import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class tmp {
    public static void main(String[] args) throws IOException {
        //GatedQuerry gatedQuerry = new GatedQuerry("C:/Users/foxjo/Pictures");
        //gatedQuerry.run();

        //GatedQuerry.a = 2;
        //System.out.println(GatedQuerry.a);

        /*
        // Create an instance of the Config class
        Config config = Config.getInstance();
        System.out.println(config.def);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();


        // Set placeholder values
        config.database = "example_database";
        config.hashMethods = new ArrayList<String[]>();
        config.hashMethods.add(new String[]{"SHA-256", "/path/to/sha256"});
        config.hashMethods.add(new String[]{"MD5","/path/to/md5"});
        config.def = 42; // Placeholder value

        System.out.println("----");
        String str = gson.toJson(config);

        FileWriter myWriter = new FileWriter("config.txt");
        myWriter.write(str);
        myWriter.close();

        System.out.println(str);
        */
    }
}
