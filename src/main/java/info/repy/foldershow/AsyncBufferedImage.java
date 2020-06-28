package info.repy.foldershow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class AsyncBufferedImage {
    private boolean finish = false;
    private BufferedImage output = null;

    public AsyncBufferedImage(final Path path, final int viewWidth, final int viewHeight) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long time = System.currentTimeMillis();
                    output = copyImage(path, viewWidth, viewHeight);
                    System.out.printf("time:%d \n",System.currentTimeMillis()-time);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish = true;
            }
        }).start();
    }

    public BufferedImage get() {
        for (int i = 0; i < 100; i++) {
            if (finish) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    public static BufferedImage copyImage(final Path path, final int viewWidth, final int viewHeight) throws IOException {
        BufferedImage input = ImageIO.read(path.toFile());

        int imageWidth = input.getWidth();
        int imageHeight = input.getHeight();
        if (viewWidth / 1.0 / imageWidth < viewHeight / 1.0 / imageHeight) {
            // 幅に合わせるので画像サイズを viewWidth / imageWidth 倍 にする。
            imageHeight = imageHeight * viewWidth / imageWidth;
            imageWidth = viewWidth; // imageWidth * viewWidth / imageWidth = viewWidth
        } else {
            //高さに合わせるので画像サイズを viewHeight / imageHeight 倍 にする。
            imageWidth = imageWidth * viewHeight / imageHeight;
            imageHeight = viewHeight; // imageHeight * viewHeight / imageHeight = viewHeight
        }

        BufferedImage output = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D outputGraphics = output.createGraphics();
        outputGraphics.setColor(Color.BLACK);
        outputGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        outputGraphics.fillRect(0, 0, viewWidth, viewHeight);
        outputGraphics.drawImage(input, (viewWidth - imageWidth) / 2, (viewHeight - imageHeight) / 2, imageWidth, imageHeight, null);
        return output;
    }
}
