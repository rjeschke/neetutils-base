package com.github.rjeschke.neetutils.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ImageFrame implements WindowListener, KeyListener
{
    private final Frame  frame;
    private final Canvas canvas;
    Image                image    = null;
    volatile boolean     isClosed = false;

    public ImageFrame(final String title, final int width, final int height, final boolean resizable)
    {
        this.frame = new Frame(title);
        final Canvas c = new ImageCanvas();
        final Dimension dim = new Dimension(width, height);
        c.setMinimumSize(dim);
        c.setMaximumSize(dim);
        c.setPreferredSize(dim);

        if (resizable)
        {
            final ScrollPane pane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
            pane.add(c);
            this.frame.add(pane);
        }
        else
        {
            this.frame.add(c);
        }

        this.frame.pack();
        this.frame.setResizable(resizable);
        this.canvas = c;

        this.frame.addWindowListener(this);
        this.frame.addKeyListener(this);
        this.canvas.addKeyListener(this);
    }

    public ImageFrame(final String title, final int width, final int height)
    {
        this(title, width, height, false);
    }

    public void setTitle(final String title)
    {
        this.frame.setTitle(title);
    }

    public static void display(final String title, final Image image)
    {
        new ImageFrame(title, image.getWidth(null), image.getHeight(null)).setVisible(true).setImage(image);
    }

    public static void display(final String title, final WrappedImage image)
    {
        new ImageFrame(title, image.getWidth(), image.getHeight()).setVisible(true).setImage(image);
    }

    @SuppressWarnings("unchecked")
    public <T extends ImageFrame> T setVisible(final boolean visible)
    {
        this.frame.setVisible(visible);
        return (T)this;
    }

    public boolean isClosed()
    {
        return this.isClosed;
    }

    @SuppressWarnings("unchecked")
    public <T extends ImageFrame> T setImage(final Image image)
    {
        this.image = image;
        this.canvas.repaint();
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ImageFrame> T setImage(final WrappedImage image)
    {
        this.image = image.getImage();
        this.canvas.repaint();
        return (T)this;
    }

    @Override
    public void windowOpened(final WindowEvent e)
    {
        //
    }

    @Override
    public void windowClosing(final WindowEvent e)
    {
        this.isClosed = true;
        this.frame.dispose();
    }

    @Override
    public void windowClosed(final WindowEvent e)
    {
        //
    }

    @Override
    public void windowIconified(final WindowEvent e)
    {
        //
    }

    @Override
    public void windowDeiconified(final WindowEvent e)
    {
        //
    }

    @Override
    public void windowActivated(final WindowEvent e)
    {
        //
    }

    @Override
    public void windowDeactivated(final WindowEvent e)
    {
        //
    }

    private class ImageCanvas extends Canvas
    {
        private static final long serialVersionUID = -4066767503350157062L;

        public ImageCanvas()
        {
            //
        }

        @Override
        public void update(final java.awt.Graphics g)
        {
            this.paint(g);
        }

        @Override
        public void paint(final java.awt.Graphics g)
        {
            final Image img = ImageFrame.this.image;
            if (img != null)
            {
                g.drawImage(img, 0, 0, null);
            }
        }
    }

    @Override
    public void keyTyped(final KeyEvent e)
    {
        // to be overwritten
    }

    @Override
    public void keyPressed(final KeyEvent e)
    {
        // to be overwritten
    }

    @Override
    public void keyReleased(final KeyEvent e)
    {
        // to be overwritten
    }
}
