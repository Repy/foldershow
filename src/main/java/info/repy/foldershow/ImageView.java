package info.repy.foldershow;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageView extends Canvas {
    private BufferedImage image = null;

    public void showImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void update(Graphics g) {
        if (this.image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
