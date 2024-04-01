import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Config {
    static Config config;
    String database;
    ArrayList<String[]> hashMethods;
    int def; //default method
    String middlePoint;


    public static Config getInstance() {
        Path path = Paths.get("config.txt");
        if(Config.config != null) return Config.config;
        try {
            String fileContents = new String(Files.readAllBytes(path), "UTF-8");

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Type configType = new TypeToken<Config>(){}.getType();

            Config.config = gson.fromJson(fileContents, configType);
            return Config.config;

        } catch (IOException e) {
            System.out.println("config.txt file at " + path.toString() + " not found.");
        }
        return null;
    }
}
