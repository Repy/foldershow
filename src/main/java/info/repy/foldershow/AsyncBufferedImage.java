package info.repy.foldershow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AsyncBufferedImage {
    private final Path path;
    private BufferedImage image = null;
    public AsyncBufferedImage(Path path){
        this.path = path;
        start();
    }
    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long time = System.currentTimeMillis();
                    image = ImageIO.read(path.toFile());
                    System.out.printf("time:%d \n",System.currentTimeMillis()-time);
                } catch (IOException e) {
                    //
                }
            }
        }).start();
    }
    public BufferedImage get(){
        for (int i = 0; i < 100; i++) {
            if(image != null) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //
            }
        }
        return image;
    }
}
