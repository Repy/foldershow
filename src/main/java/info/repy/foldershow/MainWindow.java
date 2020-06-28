package info.repy.foldershow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class MainWindow {
    Frame frame = new Frame();
    ImageView view = new ImageView();
    private final List<Path> list = new LinkedList<Path>();

    public Path rote() {
        Path o = list.remove(0);
        list.add(o);
        return o;
    }


    public MainWindow(List<Path> list) {
        this.list.addAll(list);
        Collections.shuffle(this.list);
        frame.setSize(20, 50);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
        view.setSize(frame.getWidth() - frame.getInsets().left - frame.getInsets().right, frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.this.close();
            }
        });
        frame.add(view);
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.this.close();
            }
        });
        next();
    }

    private final java.util.Timer timer = new java.util.Timer();

    public void next() {
        try {
            Path item = rote();
            System.out.printf("%s\n", item.toAbsolutePath());
            BufferedImage bufferedImage = ImageIO.read(item.toFile());
            showImage(bufferedImage);
            timer.purge();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    next();
                }
            }, 1000);
        } catch (IOException e) {
            this.close();
        }

    }

    private void showImage(java.awt.image.BufferedImage image) {
        view.showImage(image);
        view.repaint();
    }

    public void close() {
        timer.cancel();
        frame.dispose();
    }
}
