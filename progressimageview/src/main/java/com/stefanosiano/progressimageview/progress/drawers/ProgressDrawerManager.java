package com.stefanosiano.progressimageview.progress.drawers;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.stefanosiano.progressimageview.ProgressImageView;
import com.stefanosiano.progressimageview.progress.PivProgressMode;
import com.stefanosiano.progressimageview.progress.ProgressOptions;

import java.lang.ref.WeakReference;

/**
 * Manager class for progress drawers. Used to initialize and get the instances of the needed drawers.
 */

public final class ProgressDrawerManager implements ProgressOptions.ProgressOptionsListener {


    //Variables used to initialize drawers

    //Using a weakRefence to be sure to not leak memory
    private final WeakReference<ProgressImageView> mPiv;

    /** Bounds in which the progress indicator will be drawn */
    private final RectF mProgressBounds;

    //Drawers
    private DummyProgressDrawer mDummyProgressDrawer;
    private DeterminateProgressDrawer mDeterminateProgressDrawer;
    private DeterminateHorizontalProgressDrawer mDeterminateHorizontalProgressDrawer;
    private IndeterminateHorizontalProgressDrawer mIndeterminateHorizontalProgressDrawer;
    private IndeterminateProgressDrawer mIndeterminateProgressDrawer;

    /** Interface used to switch between its implementations, based on the progress mode selected. */
    private ProgressDrawer mProgressDrawer;

    /** Mode of the progress drawer */
    private PivProgressMode mProgressMode = null;

    /** Options used by progress drawers */
    private ProgressOptions mProgressOptions;

    /** Listener to handle things from drawers */
    private ProgressDrawerListener listener;


    /**
     * Manager class for progress drawers. Used to initialize and get the instances of the needed drawers.
     *
     * @param piv View to show progress indicator into
     */
    public ProgressDrawerManager(ProgressImageView piv, final ProgressOptions progressOptions){
        this.mPiv = new WeakReference<>(piv);
        this.mProgressBounds = new RectF();
        this.mProgressOptions = progressOptions;
        this.listener = new ProgressDrawerListener() {
            @Override
            public void onRequestInvalidate() {

                if(mPiv.get() != null) {
                    //invalidates only the area of the progress indicator, instead of the whole view. +1 e -1 are used to be sure to invalidate the whole progress indicator
                    //It is more efficient then just postInvalidate(): if something is drawn outside the bounds, it will not be calculated again!
                    mPiv.get().postInvalidate((int) mProgressBounds.left - 1, (int) mProgressBounds.top - 1, (int) mProgressBounds.right + 1, (int) mProgressBounds.bottom + 1);
                }
            }
        };
        this.mProgressOptions.setListener(this);
    }


    /**
     * Gets the instance of the progress drawer to use.
     * If the drawer doesn't exist, it will be instantiated and returned.
     *
     * @param progressMode Mode of the progress, used to choose the right drawer.
     * @return A ProgressDrawer chosen based on the mode. It will never return null.
     */
    private ProgressDrawer getDrawer(PivProgressMode progressMode){
        switch (progressMode){

            case INDETERMINATE:
                if(mIndeterminateProgressDrawer == null)
                    this.mIndeterminateProgressDrawer = new IndeterminateProgressDrawer();
                mProgressDrawer = mIndeterminateProgressDrawer;
                break;

            case DETERMINATE:
                if(mDeterminateProgressDrawer == null)
                    this.mDeterminateProgressDrawer = new DeterminateProgressDrawer();
                mProgressDrawer = mDeterminateProgressDrawer;
                break;

            case HORIZONTAL_DETERMINATE:
                if(mDeterminateHorizontalProgressDrawer == null)
                    this.mDeterminateHorizontalProgressDrawer = new DeterminateHorizontalProgressDrawer();
                mProgressDrawer = mDeterminateHorizontalProgressDrawer;
                break;

            case HORIZONTAL_INDETERMINATE:
                if(mIndeterminateHorizontalProgressDrawer == null)
                    this.mIndeterminateHorizontalProgressDrawer = new IndeterminateHorizontalProgressDrawer();
                mProgressDrawer = mIndeterminateHorizontalProgressDrawer;
                break;

            default:
            case NONE:
                if(mDummyProgressDrawer == null)
                    this.mDummyProgressDrawer = new DummyProgressDrawer();
                mProgressDrawer = mDummyProgressDrawer;
                break;
        }
        mProgressDrawer.setListener(listener);
        return mProgressDrawer;
    }

    /**
     * It calculates the bounds of the progress indicator.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     */
    public final void onSizeChanged(int w, int h) {
        mProgressOptions.calculateBounds(w, h, mProgressMode);
        //set calculated bounds to our progress bounds
        mProgressBounds.set(
                mProgressOptions.getLeft(),
                mProgressOptions.getTop(),
                mProgressOptions.getRight(),
                mProgressOptions.getBottom());

        mProgressDrawer.setup(mProgressOptions);
    }


    /**
     * Changes the progress mode of the indicator (e.g. passing from determinate to indeterminate).
     * It also starts animation of indeterminate progress indicator.
     *
     * @param progressMode mode to change the progress indicator into
     */
    public final void changeProgressMode(PivProgressMode progressMode){
        if(mProgressMode != null && mProgressMode == progressMode)
            return;

        if(mProgressDrawer != null)
            mProgressDrawer.stopIndeterminateAnimation();

        mProgressMode = progressMode;
        mProgressDrawer = getDrawer(mProgressMode);
        mProgressDrawer.setup(mProgressOptions);
        mProgressDrawer.startIndeterminateAnimation();
    }


    /** Draws the progress indicator */
    public final void onDraw(Canvas canvas) {
        mProgressDrawer.draw(canvas, mProgressBounds);
    }

    /**
     * @return The options of the progress indicator
     */
    public final ProgressOptions getProgressOptions() {
        return mProgressOptions;
    }


    interface ProgressDrawerListener{
        /** Request to invalidate the progress indicator bounds */
        void onRequestInvalidate();
    }


    /**
     * Called when an option is updated. It propagates the update to the progress drawers.
     */
    @Override
    public void onOptionsUpdated(ProgressOptions options) {
        mProgressDrawer.setup(options);
        mProgressOptions = options;
    }

    /**
     * Called when an option that changes the size of the progress indicator is updated.
     * The bounds are calculated again, and it propagates the update to the progress drawers.
     */
    @Override
    public void onSizeUpdated(ProgressOptions options) {
        mProgressOptions = options;
        //set calculated bounds to our progress bounds
        mProgressBounds.set(
                mProgressOptions.getLeft(),
                mProgressOptions.getTop(),
                mProgressOptions.getRight(),
                mProgressOptions.getBottom());

        mProgressDrawer.setup(mProgressOptions);
    }
}
