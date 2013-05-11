package com.github.rjeschke.neetutils.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ImageFrame implements WindowListener
{
    private final Frame frame;
    Image               image    = null;
    Canvas              canvas   = null;
    volatile boolean    isClosed = false;

    public ImageFrame(final String title, final int width, final int height)
    {
        this.frame = new Frame(title);
        final Canvas c = new ImageCanvas();
        final Dimension dim = new Dimension(width, height);
        c.setMinimumSize(dim);
        c.setMaximumSize(dim);
        c.setPreferredSize(dim);
        this.frame.add(c);
        this.frame.addWindowListener(this);
        this.frame.pack();
        this.frame.setResizable(false);
        this.canvas = c;
    }

    public void setTitle(final String title)
    {
        this.frame.setTitle(title);
    }

    public ImageFrame setVisible(final boolean visible)
    {
        this.frame.setVisible(visible);
        return this;
    }

    public boolean isClosed()
    {
        return this.isClosed;
    }

    public ImageFrame setImage(final Image image)
    {
        this.image = image;
        this.canvas.repaint();
        return this;
    }

    public ImageFrame setImage(final WrappedImage image)
    {
        this.image = image.getImage();
        this.canvas.repaint();
        return this;
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
}
