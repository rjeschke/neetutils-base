package com.github.rjeschke.neetutils.audio;

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class ResamplerFactory
{
    private ResamplerFactory()
    {
        // empty
    }

    public final static Resampler create(final double srcFs, final double targetFs, final double[] firFilter)
    {
        if (NMath.doubleEquals(srcFs, targetFs, 1e-5)) return new ResamplerNone();

        if (srcFs < targetFs) return new ResamplerUp(srcFs, targetFs, firFilter);

        return new ResamplerDown(srcFs, targetFs, firFilter);
    }

    public final static Resampler create(final double srcFs, final double targetFs, final int firOrder)
    {
        if (NMath.doubleEquals(srcFs, targetFs, 1e-5)) return new ResamplerNone();

        if (srcFs < targetFs)
        {
            return new ResamplerUp(srcFs, targetFs, FIRUtils.windowBlackman(FIRUtils.createLowpass(firOrder, srcFs * 0.45, targetFs)));
        }

        return new ResamplerDown(srcFs, targetFs, FIRUtils.windowBlackman(FIRUtils.createLowpass(firOrder, targetFs * 0.45, srcFs)));
    }

    public final static Resampler create(final double srcFs, final double targetFs, final double transitionWidth, final double attenuation)
    {
        if (NMath.doubleEquals(srcFs, targetFs, 1e-5)) return new ResamplerNone();

        double[] win;
        final double safeTw = NMath.clamp(transitionWidth, 1, 50) / 100.0;

        if (srcFs < targetFs)
        {
            final double tw = srcFs * 0.5 * safeTw;
            win = FIRUtils.windowKaiser(tw, attenuation, targetFs);
            return new ResamplerUp(srcFs, targetFs, FIRUtils.multiply(FIRUtils.createLowpass(win.length - 1, srcFs * 0.5 - tw, targetFs), win));
        }

        final double tw = targetFs * 0.5 * safeTw;
        win = FIRUtils.windowKaiser(tw, attenuation, srcFs);
        return new ResamplerDown(srcFs, targetFs, FIRUtils.multiply(FIRUtils.createLowpass(win.length - 1, targetFs * 0.5 - tw, srcFs), win));
    }
}
