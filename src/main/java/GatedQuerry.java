import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GatedQuerry extends Thread {
    private static boolean anyRunning = false;

    private boolean uploadSuccessful = false;
    private final Queue<String> fileQueue;
    private String directoryPath, pubRepo;
    private String[] hashMethodSpecs;
    private final int LIMIT_PER_MINUTE = 15;
    private final Config config;
    private boolean remoteSelected = false;
    private FXMLDocumentController controller;


    public GatedQuerry(String directoryPath, String hashMethod, String pubRepo, FXMLDocumentController controller) {
        this.directoryPath = directoryPath;
        this.pubRepo = pubRepo;
        this.fileQueue = new LinkedList<>();
        this.controller = controller;

        this.config = Config.getInstance();
        int hashMethodNumber = config.def;
        for (int i = 0; i < config.hashMethods.size(); i++) {
            if(config.hashMethods.get(i)[0].equalsIgnoreCase(hashMethod)){
                hashMethodNumber = i;
            }
        }
        this.hashMethodSpecs = config.hashMethods.get(hashMethodNumber);
    }

    public synchronized void fillQueue() {
        //upload_dir
        // = directoryPath.replaceAll(".*/data/imgs/","/data/imgs/");
        System.out.println(directoryPath.replace("\\", "/"));
        if (remoteSelected){
            try{
                ProcessBuilder pb = new ProcessBuilder("C:/Program Files/Git/bin/bash.exe", "-c", "./upload_dir2.sh "
                        + directoryPath.replace("\\", "/").replace(" ", "đ") + " " + pubRepo).inheritIO();
                //pb.directory(new File("./"));
                Process process = pb.start();
                process.waitFor();
                if(process.exitValue() == 0) uploadSuccessful = true;
            }catch (Exception e){
                System.out.println(e);
                System.out.println("could not ulpoad directory");
            }
        }
        try {
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if(remoteSelected) fileQueue.offer(file.getParentFile().getName() + "/" + file.getName());
                        if(!remoteSelected) fileQueue.offer(file.getAbsolutePath().replace("\\", "/").replace(" ", "đ"));
                        System.out.println(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("no directory specified");
        }

    }

    public synchronized void pop() throws IOException {
        //String directoryName = directoryPath.replaceAll(".*/","/");
        if(fileQueue.isEmpty()) {
            return;
        }
        String filepath = fileQueue.poll();
        ProcessBuilder pb = new ProcessBuilder(hashMethodSpecs[2], hashMethodSpecs[3],
                hashMethodSpecs[1] + " " + filepath + " master/data/imgs " + pubRepo).inheritIO();
        //System.out.println(hashMethodSpecs[2]+" "+ hashMethodSpecs[3]+" "+
        //        hashMethodSpecs[1] + " " + filepath + " master/data/imgs " + pubRepo);
        //ProcessBuilder pb = new ProcessBuilder("C:/Program Files/Git/usr/bin/bash.exe", "-c", "pwd");
        pb.directory(new File("./"));
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        System.out.println("pop");
        String result = builder.toString();
        System.out.println(result);

        try (CollisionHandler ch = CollisionHandler.getInstance()){
            ch.addPossibleCollision(directoryPath+"/"+filepath, hashMethodSpecs[0], result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        if(anyRunning == false) anyRunning = true;
        else {
            System.out.println("A GatedQuerry is already running");
            return;
        }

        fillQueue();
        //wait for it to also upload
            long startTime = System.currentTimeMillis();
            long timeout = 3000; // Timeout of 3 seconds
            long elapsedTime = 0;
            while (!uploadSuccessful && elapsedTime < timeout) {
                try {
                    Thread.sleep(1000);
                    elapsedTime += 1000;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        while (!fileQueue.isEmpty()) {
            // Call pop method, but not more than LIMIT times during the last minute
            for (int i = 0; i < LIMIT_PER_MINUTE && !fileQueue.isEmpty(); i++) {
                try {
                    pop();
                } catch (IOException e) {
                    //System.out.println("couldnt convert to hash");
                    System.out.println(e);
                }
            }
            try {
                Thread.sleep(60000); // Sleep for one minute
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            controller.viewNextCollision();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        anyRunning = false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //File myObj = new File("dirName.txt");
        //Scanner sc = new Scanner(myObj);
        /*
        String s = FXMLDocumentController.enteredDir;
        GatedQuerry gatedQuerry = new GatedQuerry(s);
        System.out.println("nya " + s);
        Thread thread = new Thread(gatedQuerry);
        thread.start();*/
    }
}