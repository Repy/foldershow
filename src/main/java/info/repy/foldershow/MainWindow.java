package info.repy.foldershow;

import javax.swing.*;
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
    private final int sec;
    private final JFrame frame = new JFrame();
    private final ImageView view = new ImageView();
    private final List<Path> list = new LinkedList<Path>();
    private AsyncBufferedImage next = null;

    public AsyncBufferedImage rote() throws IOException {
        AsyncBufferedImage now = next;
        Path o = list.remove(0);
        list.add(o);
        next = new AsyncBufferedImage(o, view.getWidth(), view.getHeight());
        return now;
    }


    public MainWindow(int sec, List<Path> list) {
        this.sec = sec;
        this.list.addAll(list);
        Collections.shuffle(this.list);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MainWindow.this.resize();
                        MainWindow.this.next();
                    }
                });
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
        frame.add(view);
        frame.setSize(600, 300);
        if (true) {
            frame.setUndecorated(true);
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            device.setFullScreenWindow(frame);
        }
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
            view.showImage(bufferedImage);
            view.repaint();
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

    public void resize() {
        view.setBounds(0, 0, frame.getWidth() - frame.getInsets().left - frame.getInsets().right, frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom);
    }

    public void close() {
        timer.cancel();
        frame.dispose();
    }
}
