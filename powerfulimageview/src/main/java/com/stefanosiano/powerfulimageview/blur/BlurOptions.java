package com.stefanosiano.powerfulimageview.blur;

import java.lang.ref.WeakReference;

/**
 * Class that helps managing the options that will be used to blur the image
 */

public final class BlurOptions {

    /** Rate to downSample the image width and height, based on the view size */
    private float mDownSamplingRate;

    /** Whether the original bitmap should be kept in memory. If false, trying to blur a second time won't have effect */
    private boolean mKeepOriginal;

    /** Whether the image should be blurred with a java equivalent of the renderscript algorithm if an error occurs */
    private boolean mUseRsFallback;

    /** Listener that will update the blur manager on changes, with a weak reference to be sure to not leak memory */
    private WeakReference<BlurOptionsListener> listener;

    /**
     * Creates the object that will manage the blur options
     *
     * @param downSamplingRate Rate to downSample the image width and height, based on the view size. Cannot be less than 1
     * @param keepOriginal Whether the original bitmap should be kept in memory. If false, trying to blur a second time won't have effect
     * @param useRsFallback Whether the image should be blurred with a java equivalent of the renderscript algorithm if an error occurs
     */
    public BlurOptions(float downSamplingRate, boolean keepOriginal, boolean useRsFallback) {
        this.mDownSamplingRate = downSamplingRate;
        if(mDownSamplingRate < 1) mDownSamplingRate = 1;
        this.mKeepOriginal = keepOriginal;
        this.mUseRsFallback = useRsFallback;
    }

    public interface BlurOptionsListener{
        void onKeepOriginalChanged();
        void onDownsamplingRateChanged();
    }

    public void setListener(BlurOptionsListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    /**
     * @return The rate to downSample the image width and height, based on the view size.
     * Bitmap is downsampled to be no more than the view size divided by this rate.
     */
    public float getDownSamplingRate() {
        return mDownSamplingRate;
    }


    /**
     * Sets the rate to downSample the image width and height, based on the view size.
     * If a value less than 1 is passed, downSampling rate 1 is used.
     * Bitmap is downsampled to be no more than the view size divided by this rate.
     */
    public void setDownSamplingRate(float downSamplingRate) {
        this.mDownSamplingRate = downSamplingRate;
        if(mDownSamplingRate < 1) mDownSamplingRate = 1;
        if(this.listener.get() != null)
            this.listener.get().onDownsamplingRateChanged();
    }

    /**
     * @return If the original bitmap should be kept in memory
     */
    public boolean isKeepOriginal() {
        return mKeepOriginal;
    }

    /**
     * @param keepOriginal If the original bitmap should be kept in memory. If the image is already blurred
     *                     and false is passed to this function, original bitmap memory will be released.
     */
    public void setKeepOriginal(boolean keepOriginal) {
        this.mKeepOriginal = keepOriginal;
        if(this.listener.get() != null)
            this.listener.get().onKeepOriginalChanged();
    }

    /**
     * @return Whether the image should be blurred with the Java equivalent function of renderscript
     *          one used. Used only if a renderscript mode is selected.
     */
    public boolean isUseRsFallback() {
        return mUseRsFallback;
    }

    /**
     * @param useRsFallback Whether the image should be blurred with the Java equivalent function
     *                      of renderscript one used. Used only if a renderscript mode is selected.
     */
    public void setUseRsFallback(boolean useRsFallback) {
        this.mUseRsFallback = useRsFallback;
    }


}
