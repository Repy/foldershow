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
    private int sec;
    private final Frame frame = new Frame();
    private final ImageView view = new ImageView();
    private final List<Path> list = new LinkedList<Path>();
    private AsyncBufferedImage next = null;

    public AsyncBufferedImage rote() throws IOException {
        AsyncBufferedImage now = next;
        Path o = list.remove(0);
        list.add(o);
        next = new AsyncBufferedImage(o);
        return now;
    }


    public MainWindow(int sec, List<Path> list) {
        this.sec = sec;
        this.list.addAll(list);
        Collections.shuffle(this.list);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                device.setFullScreenWindow(frame);
                frame.add(view);
                MainWindow.this.next();
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                MainWindow.this.resize();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.this.close();
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                MainWindow.this.resize();
            }
        });
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow.this.close();
            }
        });
        frame.setVisible(true);
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
            }, this.sec * 1000L);
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
