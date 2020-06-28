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
    private Frame frame = new Frame();
    private ImageView view = new ImageView();
    private final List<Path> list = new LinkedList<Path>();
    private AsyncBufferedImage next = null;

    public AsyncBufferedImage rote() throws IOException {
        AsyncBufferedImage now = next;
        Path o = list.remove(0);
        list.add(o);
        next = new AsyncBufferedImage(o);
        return now;
    }


    public MainWindow(List<Path> list) {
        this.list.addAll(list);
        Collections.shuffle(this.list);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.this.close();
            }
        });
        frame.add(view);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.this.close();
            }
        });
        next();
        resize();
    }

    private final java.util.Timer timer = new java.util.Timer();

    public void next() {
        try {
            BufferedImage bufferedImage = null;
            for (int i = 0; i < 5; i++) {
                AsyncBufferedImage asyncBufferedImage = rote();
                if (asyncBufferedImage == null) continue;
                bufferedImage = asyncBufferedImage.get();
                if (bufferedImage == null) continue;
                break;
            }
            if (bufferedImage == null) throw new RuntimeException();
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

    public void resize() {
        view.setSize(frame.getWidth() - frame.getInsets().left - frame.getInsets().right, frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom);
    }

    public void close() {
        timer.cancel();
        frame.dispose();
    }
}
