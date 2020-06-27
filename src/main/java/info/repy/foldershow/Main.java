package info.repy.foldershow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        start(args[0]);
    }

    public static void start(String root) {
        try {
            new MainWindow(Files.walk(Paths.get(root)).filter(p-> p.getFileName().toString().toLowerCase().endsWith(".jpg")).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}