package info.repy.foldershow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1");
        int sec = Integer.parseInt(args[0]);
        String dir = args[1];
        start(sec,dir);
    }

    public static void start(int sec,String root) {
        try {
            new MainWindow(sec,Files.walk(Paths.get(root)).filter(p -> p.getFileName().toString().toLowerCase().endsWith(".jpg")).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}