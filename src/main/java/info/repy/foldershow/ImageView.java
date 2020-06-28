package info.repy.foldershow;

import java.awt.*;

public class ImageView extends Canvas {
    private java.awt.image.BufferedImage image = null;

    public void showImage(java.awt.image.BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        if (this.image != null) {
            g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
