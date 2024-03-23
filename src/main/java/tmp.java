import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

public class tmp {
    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir").replace("\\", "/"));

        /*
        //ProcessBuilder pb = new ProcessBuilder("C:/Program Files/Git/bin/bash.exe", "-c", "$(pwd)/lsTest.sh").inheritIO();
        ProcessBuilder pb = new ProcessBuilder("C:/Program Files/Git/bin/bash.exe", "-c", "src/hash_methods/AzureVision/analyze-img.txt").inheritIO();
        //ProcessBuilder pb = new ProcessBuilder("C:/Program Files/Git/usr/bin/bash.exe", "-c", "pwd");
        //pb.directory(new File("../Derpin/testOtherCode"));
        Process process = pb.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String result = builder.toString();
        System.out.println(result);
        */

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
