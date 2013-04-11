package com.github.rjeschke.neetutils.graphics;

import java.util.Arrays;

public class FilterKernel
{
    final float[]    xf;
    final float[]    yf;
    final float[]    xyf;
    public final int width;
    public final int height;
    final int        offsx;
    final int        offsy;
    final boolean    isSingle;

    public FilterKernel(float[] kernel, int width, int height, int offsx, int offsy)
    {
        this.xf = null;
        this.yf = null;
        this.xyf = Arrays.copyOf(kernel, width * height);
        this.width = width;
        this.height = height;
        this.offsx = offsx;
        this.offsy = offsy;
        this.isSingle = true;
    }

    public FilterKernel(float[] kernelx, float[] kernely, int width, int height, int offsx, int offsy)
    {
        this.xf = Arrays.copyOf(kernelx, width);
        this.yf = Arrays.copyOf(kernely, height);
        this.xyf = null;
        this.width = width;
        this.height = height;
        this.offsx = offsx;
        this.offsy = offsy;
        this.isSingle = false;
    }

    public FilterKernel normalize()
    {
        if (this.isSingle)
        {
            double sum = 0;
            for (int i = 0; i < this.xyf.length; i++)
                sum += this.xyf[i];
            if (sum != 0)
            {
                for (int i = 0; i < this.xyf.length; i++)
                    this.xyf[i] /= sum;
            }
        }
        else
        {
            double sum = 0;
            for (int y = 0; y < this.height; y++)
            {
                for (int x = 0; x < this.width; x++)
                {
                    sum += this.xf[x] * this.yf[y];
                }
            }
            if (sum != 0)
            {
                for (int y = 0; y < this.height; y++)
                    this.yf[y] /= sum;
                for (int x = 0; x < this.width; x++)
                    this.xf[x] /= sum;
            }
        }

        return this;
    }
}
