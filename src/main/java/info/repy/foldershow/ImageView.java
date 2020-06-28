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
            g.setColor(Color.BLACK);

            int viewWidth = this.getWidth();
            int viewHeight = this.getHeight();

            int imageWidth = this.image.getWidth();
            int imageHeight = this.image.getHeight();
            if(viewWidth / 1.0 / imageWidth < viewHeight / 1.0 / imageHeight){
                // 幅に合わせるので画像サイズを viewWidth / imageWidth 倍 にする。
                imageHeight =imageHeight * viewWidth / imageWidth;
                imageWidth = viewWidth; // imageWidth * viewWidth / imageWidth = viewWidth
            }else{
                //高さに合わせるので画像サイズを viewHeight / imageHeight 倍 にする。
                imageWidth = imageWidth * viewHeight / imageHeight;
                imageHeight =viewHeight ; // imageHeight * viewHeight / imageHeight = viewHeight
            }
            if(g instanceof Graphics2D){
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            }
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(this.image, (viewWidth-imageWidth)/2, (viewHeight-imageHeight)/2, imageWidth, imageHeight, this);
        }
    }
}
