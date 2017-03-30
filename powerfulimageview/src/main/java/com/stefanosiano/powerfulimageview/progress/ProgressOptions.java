package com.stefanosiano.powerfulimageview.progress;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.WeakReference;

/**
 * Class that helps managing the options that will be used by the progress drawers.
 */

public final class ProgressOptions implements Parcelable {

    //Options used directly by drawers
    
    /** If the determinate drawer should update its progress with an animation */
    private boolean mDeterminateAnimationEnabled;

    /** Width of the progress indicator */
    private int mBorderWidth;

    /** Width of the progress indicator as percentage of the progress indicator size */
    private float mBorderWidthPercent;

    /** Percentage value of the progress indicator, used by determinate drawers */
    private float mValuePercent;

    /** Front color of the indicator, used by determinate drawers */
    private int mFrontColor;

    /** Back color of the indicator, used by determinate drawers */
    private int mBackColor;

    /** Color of the indicator, used by indeterminate drawers */
    private int mIndeterminateColor;

    /** If should show a wedge, used by circular determinate drawer */
    private boolean mDrawWedge;

    /** If should show a shadow */
    private boolean mShadowEnabled;

    /** Shadow color of the indicator */
    private int mShadowColor;

    /** Padding of the indicator relative to its shadow */
    private int mShadowPadding;

    /** Padding of the indicator relative to its shadow, as a percentage of the whole shadow */
    private float mShadowPaddingPercent;

    //variables used to calculate bounds

    /** Size of the indicator */
    private int mSize;

    /** Padding of the indicator */
    private int mPadding;

    /** Size of the indicator, as a percentage of the whole View */
    private float mSizePercent;

    /** Gravity of the indicator */
    private PivProgressGravity mGravity;

    /** Whether the view is using right to left layout (used for gravity option) */
    private boolean mIsRtl;

    /** Whether the view should use or ignore right to left layout (used for gravity option) */
    private boolean mRtlDisabled;




    // ************** Calculated fields *****************

    /** Calculated size of the indicator, base on mSize, mSizePercent and View size */
    private int mCalculatedSize;

    /** Calculated padding of the indicator shadow */
    private int mCalculatedShadowPadding;

    /** Calculated width of the progress indicator, base on mBorderWidth, mBorderWidthPercent and mSize */
    private int mCalculatedBorderWidth;

    //bounds of the progress indicator
    /** Left bound calculated */
    private float mCalculatedLeft;

    /** Top bound calculated */
    private float mCalculatedTop;

    /** Right bound calculated */
    private float mCalculatedRight;

    /** Bottom bound calculated */
    private float mCalculatedBottom;

    //bounds of the progress indicator shadow
    /** Left shadow bound calculated */
    private float mCalculatedShadowLeft;

    /** Top shadow bound calculated */
    private float mCalculatedShadowTop;

    /** Right shadow bound calculated */
    private float mCalculatedShadowRight;

    /** Bottom shadow bound calculated */
    private float mCalculatedShadowBottom;


    //last calculated width and height
    /** Last width calculated. Used when changing programmatically the options, so bounds can be calculated directly */
    private int mCalculatedLastW;

    /** Last height calculated. Used when changing programmatically the options, so bounds can be calculated directly */
    private int mCalculatedLastH;

    /** Last progress mode used. Used when changing programmatically the options, so bounds can be calculated directly */
    private PivProgressMode mCalculatedLastMode;

    /** Listener that will update the progress drawers on changes, with a weak reference to be sure to not leak memory */
    private WeakReference<ProgressOptionsListener> listener;

    /**
     * Creates the object that will be used by progress drawers:
     *
     * @param determinateAnimationEnabled If the determinate drawer should update its progress with an animation
     * @param borderWidth Width of the progress indicator. If it's 0 or negative, it will be automatically adjusted based on the size
     * @param borderWidthPercent Width of the progress indicator as a percentage of the progress indicator size
     * @param size Size of the progress indicator
     * @param padding Padding of the progress indicator
     * @param sizePercent Size of the progress indicator as a percentage of the whole View. If it's 0 or more, it applies and overrides "size" parameter
     * @param valuePercent Percentage value of the progress indicator, used by determinate drawers
     * @param frontColor Front color of the indicator, used by determinate drawers
     * @param backColor Back color of the indicator, used by determinate drawers
     * @param indeterminateColor Color of the indicator, used by indeterminate drawers
     * @param gravity Gravity of the indicator
     * @param rtl Whether the view should use right to left layout (used for gravity option)
     * @param disableRtlSupport If true, rtl attribute will be ignored (start will always be treated as left)
     * @param drawWedge If should show a wedge, used by circular determinate drawer
     * @param shadowEnabled If should show a shadow under progress indicator
     * @param shadowColor Color of the shadow
     * @param shadowPadding Padding of the progress indicator, relative to its shadow
     * @param shadowPaddingPercent Padding of the progress indicator, relative to its shadow, as a percentage of the shadow
     */
    public ProgressOptions(boolean determinateAnimationEnabled, int borderWidth, float borderWidthPercent, int size, int padding, float sizePercent, float valuePercent,
                           int frontColor, int backColor, int indeterminateColor, int gravity, boolean rtl, boolean disableRtlSupport, boolean drawWedge,
                           boolean shadowEnabled, int shadowColor, int shadowPadding, float shadowPaddingPercent) {
        this.mDeterminateAnimationEnabled = determinateAnimationEnabled;
        this.mBorderWidth = borderWidth;
        this.mBorderWidthPercent = borderWidthPercent;
        if(this.mBorderWidthPercent > 100)
            this.mBorderWidthPercent = this.mBorderWidthPercent % 100;
        this.mSize = size;
        this.mPadding = padding;
        this.mSizePercent = sizePercent;
        this.mValuePercent = valuePercent;
        this.mFrontColor = frontColor;
        this.mBackColor = backColor;
        this.mIndeterminateColor = indeterminateColor;
        this.mGravity = PivProgressGravity.fromValue(gravity);
        this.mIsRtl = rtl;
        this.mRtlDisabled = disableRtlSupport;
        this.mDrawWedge = drawWedge;
        this.mShadowEnabled = shadowEnabled;
        this.mShadowColor = shadowColor;
        this.mShadowPadding = shadowPadding;
        this.mShadowPaddingPercent = shadowPaddingPercent;

        //initialization of private fields used for calculations
        this.mCalculatedSize = 0;
        this.mCalculatedShadowPadding = 0;
        this.mCalculatedBorderWidth = 0;
        this.mCalculatedLastW = 0;
        this.mCalculatedLastH = 0;
        this.mCalculatedLeft = 0;
        this.mCalculatedTop = 0;
        this.mCalculatedRight = 0;
        this.mCalculatedBottom = 0;
        this.mCalculatedLastMode = PivProgressMode.NONE;
    }

    public void setOptions (ProgressOptions other) {
        this.mDeterminateAnimationEnabled = other.mDeterminateAnimationEnabled;
        this.mBorderWidth = other.mBorderWidth;
        this.mBorderWidthPercent = other.mBorderWidthPercent;
        this.mValuePercent = other.mValuePercent;
        this.mFrontColor = other.mFrontColor;
        this.mBackColor = other.mBackColor;
        this.mIndeterminateColor = other.mIndeterminateColor;
        this.mDrawWedge = other.mDrawWedge;
        this.mShadowEnabled = other.mShadowEnabled;
        this.mShadowColor = other.mShadowColor;
        this.mShadowPadding = other.mShadowPadding;
        this.mShadowPaddingPercent = other.mShadowPaddingPercent;
        this.mSize = other.mSize;
        this.mPadding = other.mPadding;
        this.mSizePercent = other.mSizePercent;
        this.mGravity = other.mGravity;
        this.mIsRtl = other.mIsRtl;
        this.mRtlDisabled = other.mRtlDisabled;
        this.mCalculatedSize = other.mCalculatedSize;
        this.mCalculatedShadowPadding = other.mCalculatedShadowPadding;
        this.mCalculatedBorderWidth = other.mCalculatedBorderWidth;
        this.mCalculatedLeft = other.mCalculatedLeft;
        this.mCalculatedTop = other.mCalculatedTop;
        this.mCalculatedRight = other.mCalculatedRight;
        this.mCalculatedBottom = other.mCalculatedBottom;
        this.mCalculatedShadowLeft = other.mCalculatedShadowLeft;
        this.mCalculatedShadowTop = other.mCalculatedShadowTop;
        this.mCalculatedShadowRight = other.mCalculatedShadowRight;
        this.mCalculatedShadowBottom = other.mCalculatedShadowBottom;
        this.mCalculatedLastW = other.mCalculatedLastW;
        this.mCalculatedLastH = other.mCalculatedLastH;
        this.mCalculatedLastMode = other.mCalculatedLastMode;
        this.listener = other.listener;
    }

    /**
     * Calculates the bounds of the progress indicator, based on progress options and mode.
     * Calculated bounds are accessible after this call through getLeft(), getTop(), getRight() and getBottom() methods.
     *
     * @param w Width of the View
     * @param h Height of the View
     * @param mode Mode of the progress indicator
     */
    public final void calculateBounds(int w, int h, PivProgressMode mode){
        //todo switch dimension/dimensionPercent priority! - if dimension is defined it's used, otherwise percent value is used

        //saving last width and height, so i can later call this function from this class
        mCalculatedLastW = w;
        mCalculatedLastH = h;
        mCalculatedLastMode = mode;
        mCalculatedSize = mSize;
        mCalculatedShadowPadding = mShadowPadding;
        mCalculatedBorderWidth = mBorderWidth;

        if(mode == PivProgressMode.NONE){
            mCalculatedLeft = 0;
            mCalculatedRight = 0;
            mCalculatedTop = 0;
            mCalculatedBottom = 0;
            mCalculatedShadowLeft = 0;
            mCalculatedShadowRight = 0;
            mCalculatedShadowTop = 0;
            mCalculatedShadowBottom = 0;
            return;
        }

        //calculate the maximum possible size of the progress indicator
        int maxSize = w < h ? w : h;
        maxSize = maxSize - mPadding - mPadding;

        //if mSizePercent is 0 or more, it overrides mSize parameter
        if(mSizePercent >= 0){
            mCalculatedSize = (int) (maxSize * mSizePercent / 100);
        }
        //the progress indicator cannot be bigger than the view (minus padding)
        if(mCalculatedSize > maxSize)
            mCalculatedSize = maxSize;

        //if mShadowPaddingPercent is 0 or more, it overrides mShadowPadding parameter
        if(mShadowPaddingPercent >= 0){
            mCalculatedShadowPadding = (int) (mCalculatedSize * mShadowPaddingPercent / 100);
        }
        //if shadow is not enabled, shadow padding is set to 0
        if(!mShadowEnabled)
            mCalculatedShadowPadding = 0;

        //if border width was not been defined, it gets calculated based on the size of the indicator
        if(mBorderWidthPercent >= 0){
            mCalculatedBorderWidth = Math.round(mCalculatedSize * mBorderWidthPercent/100);
        }
        //width of the border should be at least 1 px
        if(mCalculatedBorderWidth < 1)
            mCalculatedBorderWidth = 1;

        //calculation of bounds
        switch(mode){

            //calculation of circular bounds
            case DETERMINATE:
            case INDETERMINATE:

                //horizontal gravity
                if(mGravity.isGravityLeft(mIsRtl && !mRtlDisabled)){
                    mCalculatedShadowLeft = mPadding;
                    mCalculatedShadowRight = mCalculatedSize + mPadding;
                } else if(mGravity.isGravityRight(mIsRtl && !mRtlDisabled)){
                    mCalculatedShadowLeft = w - mCalculatedSize - mPadding;
                    mCalculatedShadowRight = w - mPadding;
                } else {
                    mCalculatedShadowLeft = (w - mCalculatedSize) /2;
                    mCalculatedShadowRight = (w + mCalculatedSize) /2;
                }

                //vertical gravity
                if(mGravity.isGravityTop()) {
                    mCalculatedShadowTop = mPadding;
                    mCalculatedShadowBottom = mCalculatedSize + mPadding;
                } else if (mGravity.isGravityBottom()) {
                    mCalculatedShadowTop = h - mCalculatedSize - mPadding;
                    mCalculatedShadowBottom = h - mPadding;
                } else {
                    mCalculatedShadowTop = (h - mCalculatedSize) / 2;
                    mCalculatedShadowBottom = (h + mCalculatedSize) / 2;
                }
                mCalculatedLeft = mCalculatedShadowLeft + mCalculatedShadowPadding + mCalculatedBorderWidth / 2;
                mCalculatedRight = mCalculatedShadowRight - mCalculatedShadowPadding - mCalculatedBorderWidth / 2;
                mCalculatedTop = mCalculatedShadowTop + mCalculatedShadowPadding + mCalculatedBorderWidth / 2;
                mCalculatedBottom = mCalculatedShadowBottom - mCalculatedShadowPadding - mCalculatedBorderWidth / 2;
                break;

            //calculation of horizontal bounds
            case HORIZONTAL_DETERMINATE:
            case HORIZONTAL_INDETERMINATE:

                //horizontal gravity
                if(mGravity.isGravityLeft(mIsRtl && !mRtlDisabled)){
                    mCalculatedShadowLeft = mPadding;
                    mCalculatedShadowRight = mCalculatedSize + mPadding;
                } else if(mGravity.isGravityRight(mIsRtl && !mRtlDisabled)){
                    mCalculatedShadowLeft = w - mCalculatedSize - mPadding;
                    mCalculatedShadowRight = w - mPadding;
                } else {
                    mCalculatedShadowLeft = (w - mCalculatedSize)/2;
                    mCalculatedShadowRight = (w + mCalculatedSize)/2;
                }

                //vertical gravity
                if(mGravity.isGravityTop()) {
                    mCalculatedShadowTop = mPadding;
                    mCalculatedShadowBottom = mCalculatedBorderWidth + mPadding;
                } else if (mGravity.isGravityBottom()) {
                    mCalculatedShadowTop = h - mCalculatedBorderWidth -  mPadding;
                    mCalculatedShadowBottom = h - mPadding;
                } else {
                    mCalculatedShadowTop = (h - mCalculatedBorderWidth)/2;
                    mCalculatedShadowBottom = (h + mCalculatedBorderWidth)/2;
                }
                mCalculatedLeft = mCalculatedShadowLeft + mCalculatedShadowPadding;
                mCalculatedRight = mCalculatedShadowRight - mCalculatedShadowPadding;
                mCalculatedTop = mCalculatedShadowTop + mCalculatedShadowPadding;
                mCalculatedBottom = mCalculatedShadowBottom - mCalculatedShadowPadding;
                break;

            //if everything goes right, it should never come here. Just a precaution
            case NONE:
            default:
                mCalculatedShadowLeft = 0;
                mCalculatedShadowRight = 0;
                mCalculatedShadowTop = 0;
                mCalculatedShadowBottom = 0;
                mCalculatedLeft = 0;
                mCalculatedRight = 0;
                mCalculatedTop = 0;
                mCalculatedBottom = 0;
                break;
        }
    }

    /** Returns the left bound calculated. Be sure to call calculateBounds() before this! */
    public final float getLeft() {
        return mCalculatedLeft;
    }

    /** Returns the top bound calculated. Be sure to call calculateBounds() before this! */
    public final float getTop() {
        return mCalculatedTop;
    }

    /** Returns the right bound calculated. Be sure to call calculateBounds() before this! */
    public final float getRight() {
        return mCalculatedRight;
    }

    /** Returns the bottom bound calculated. Be sure to call calculateBounds() before this! */
    public final float getBottom() {
        return mCalculatedBottom;
    }

    /** Returns the shadow left bound calculated. Be sure to call calculateBounds() before this! */
    public final float getShadowLeft() {
        return mCalculatedShadowLeft;
    }

    /** Returns the shadow top bound calculated. Be sure to call calculateBounds() before this! */
    public final float getShadowTop() {
        return mCalculatedShadowTop;
    }

    /** Returns the shadow right bound calculated. Be sure to call calculateBounds() before this! */
    public final float getShadowRight() {
        return mCalculatedShadowRight;
    }

    /** Returns the shadow bottom bound calculated. Be sure to call calculateBounds() before this! */
    public final float getShadowBottom() {
        return mCalculatedShadowBottom;
    }




    /**
     * Set whether the determinate drawer should update its progress with an animation.
     * If the drawer is not determinate or horizontal_determinate it's ignored.
     *
     * @param determinateAnimationEnabled If true it updates its progress with an animation, otherwise it will update instantly
     */
    public void setDeterminateAnimationEnabled(boolean determinateAnimationEnabled) {
        this.mDeterminateAnimationEnabled = determinateAnimationEnabled;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }

    /**
     * Width of the progress indicator.
     * It's used only if it's higher than 0 and borderWidthPercent is less than 0.
     * If you want to use dp, set value using TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderWidth, getResources().getDisplayMetrics())
     *
     * @param borderWidth Width of the progress indicator
     */
    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Width of the progress indicator as percentage of the progress indicator size.
     * Overrides border width set through setBorderWidth().
     * If the percentage is higher than 100, it is treated as (value % 100).
     * If the percentage is lower than 0, it is ignored.
     *
     * @param borderWidthPercent Percentage of the progress indicator size, as a float from 0 to 100
     */
    public void setBorderWidthPercent(float borderWidthPercent) {
        if(borderWidthPercent > 100)
            borderWidthPercent = borderWidthPercent % 100;
        this.mBorderWidthPercent = borderWidthPercent;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Percentage value of the progress indicator, used by determinate drawers.
     * If the percentage is higher than 100, it is treated as (value % 100).
     * If the percentage is lower than 0, it is treated as 0.
     * If the drawer is not determinate or horizontal_determinate it's ignored.
     * Note: multiplies of 100 (e.g. 200, 300, ...) will be treated as 0!
     *
     * @param valuePercent Percentage of the progress indicator, as a float from 0 to 100
     */
    public void setValuePercent(float valuePercent) {
        if(valuePercent > 100)
            valuePercent = valuePercent % 100;
        if(valuePercent < 0)
            valuePercent = 0;
        this.mValuePercent = valuePercent;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }

    /**
     * Set the front color of the indicator, used by determinate drawers.
     * If the drawer is not determinate or horizontal_determinate it's ignored.
     *
     * Note that the color is an int containing alpha as well as r,g,b. This 32bit value is not
     * premultiplied, meaning that its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param frontColor Color to use.
     */
    public void setFrontColor(int frontColor) {
        this.mFrontColor = frontColor;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }


    /**
     * Set the back color of the indicator, used by determinate drawers.
     * If the drawer is not determinate or horizontal_determinate it's ignored.
     *
     * Note that the color is an int containing alpha as well as r,g,b. This 32bit value is not
     * premultiplied, meaning that its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param backColor Color to use.
     */
    public void setBackColor(int backColor) {
        this.mBackColor = backColor;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }


    /**
     * Set the front color of the indicator, used by indeterminate drawers.
     * If the drawer is not indeterminate or horizontal_indeterminate it's ignored.
     *
     * Note that the color is an int containing alpha as well as r,g,b. This 32bit value is not
     * premultiplied, meaning that its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param indeterminateColor Color to use.
     */
    public void setIndeterminateColor(int indeterminateColor) {
        this.mIndeterminateColor = indeterminateColor;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }

    /**
     * Set whether to show a wedge or a circle, used by circular determinate drawer.
     * If the drawer is not determinate it's ignored.
     *
     * @param mDrawWedge If true, a wedge is drawn, otherwise a circle will be drawn
     */
    public void setDrawWedge(boolean mDrawWedge) {
        this.mDrawWedge = mDrawWedge;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }

    /**
     * Size of the progress indicator.
     *
     * It's used only if progressSizePercent is less than 0.
     * Note that it may be different from the actual size used to draw the progress, since it is
     *      calculated based on the View size, on the sizePercent option and on the padding option.
     * If you want to use dp, set value using TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderWidth, getResources().getDisplayMetrics())
     *
     * @param size Size of the progress indicator
     */
    public void setSize(int size) {
        this.mSize = size;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }


    /**
     * Set the padding of the progress indicator.
     *
     * If you want to use dp, set value using TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderWidth, getResources().getDisplayMetrics())
     *
     * @param padding Padding of the progress indicator
     */
    public void setPadding(int padding) {
        this.mPadding = padding;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set the size of the progress indicator.
     *
     * Overrides size set through setSize().
     * If the percentage is higher than 100, it is treated as (value % 100).
     * If the percentage is lower than 0, it is ignored.
     *
     * @param sizePercent Progress indicator size as a percentage of the whole View, as a float from 0 to 100
     */
    public void setSizePercent(float sizePercent) {
        if(sizePercent > 100)
            sizePercent = sizePercent % 100;
        this.mSizePercent = sizePercent;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set the gravity of the indicator.
     * It will follow the right to left layout (on api 17+), if not disabled.
     *
     * @param mGravity Gravity of the indicator
     */
    public void setGravity(PivProgressGravity mGravity) {
        this.mGravity = mGravity;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set whether the view should use right to left layout (used for gravity option)
     *
     * @param rtlDisabled If true, start will always be treated as left and end as right.
     *                      If false, on api 17+, gravity will be treated accordingly to rtl rules.
     */
    public void setRtlDisabled(boolean rtlDisabled) {
        this.mRtlDisabled = rtlDisabled;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set the shadow color of the indicator, used by drawers.
     *
     * Note that the color is an int containing alpha as well as r,g,b. This 32bit value is not
     * premultiplied, meaning that its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param shadowColor Color to use.
     */
    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
        if(listener.get() != null)
            listener.get().onOptionsUpdated(this);
    }

    /**
     * Set whether to show a progress shadow.
     *
     * @param shadowEnabled If true, the shadow is drawn
     */
    public void setShadowEnabled(boolean shadowEnabled) {
        this.mShadowEnabled = shadowEnabled;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set the padding of the progress indicator relative to its shadow.
     *
     * @param padding Padding of the progress indicator shadow
     */
    public void setShadowPadding(int padding) {
        this.mShadowPadding = padding;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }

    /**
     * Set the padding of the progress indicator relative to its shadow.
     * Overrides padding set through setShadowPadding().
     * If the percentage is higher than 100, it is treated as (value % 100).
     * If the percentage is lower than 0, it is ignored. -->
     *
     * @param paddingPercent Progress indicator shadow padding as a percentage of the whole shadow, as a float from 0 to 100
     */
    public void setShadowPaddingPercent(float paddingPercent) {
        if(paddingPercent > 100)
            mShadowPaddingPercent = paddingPercent % 100;
        calculateBounds(mCalculatedLastW, mCalculatedLastH, mCalculatedLastMode);
        if(listener.get() != null)
            listener.get().onSizeUpdated(this);
    }



    /**
     * Returns the width percentage of the progress indicator size.
     * If you want to get the real width used to show the progress, call getCalculatedBorderWidth().
     * @return Border width percentage of the progress indicator size
     */
    public float getBorderWidthPercent() {
        return mBorderWidthPercent;
    }

    /**
     * Returns the width of the progress indicator
     * If you want to get the real width used to show the progress, call getCalculatedBorderWidth().
     * @return Width of the progress indicator
     */
    public final int getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * Returns the size of the progress indicator.
     * If you want to get the real size used to show the progress, call getCalculatedSize().
     * @return Returns the size of the progress indicator
     */
    public int getSize() {
        return mSize;
    }

    /**
     * Returns the size percentage of the whole View size
     * If you want to get the real size used to show the progress, call getCalculatedSize().
     * @return Size percentage of the whole View size
     */
    public float getSizePercent() {
        return mSizePercent;
    }

    /**
     * @return Padding of the progress indicator
     */
    public int getPadding() {
        return mPadding;
    }

    /**
     * @return Gravity of the progress indicator size
     */
    public PivProgressGravity getGravity() {
        return mGravity;
    }

    /**
     * @return Wheter rtl support is disabled
     */
    public boolean isRtlDisabled() {
        return mRtlDisabled;
    }

    /**
     * Padding of the progress indicator shadow.
     * If you want to get the real padding used to show the shadow, call getCalculatedShadowPadding().
     *
     * @return Padding of the progress indicator shadow
     */
    public int getShadowPadding() {
        return this.mShadowPadding;
    }

    /**
     * Padding of the progress indicator shadow, as a percentage of the whole shadow.
     * If you want to get the real padding used to show the shadow, call getCalculatedShadowPadding().
     *
     * @return Padding of the progress indicator shadow, as a percentage of the whole shadow
     */
    public float getShadowPaddingPercent() {
        return this.mShadowPaddingPercent;
    }

    /**
     * The size of the progress indicator, after calculations.
     * This will return the real value used by the progress indicator to show.
     *
     * @return The size of the progress indicator, after calculations
     */
    public int getCalculatedSize() {
        return mCalculatedSize;
    }

    /**
     * Padding of the progress indicator shadow, after calculations.
     * This will return the real value used by the progress shadow to show.
     *
     * @return Padding of the progress indicator shadow, after calculations
     */
    public float getCalculatedShadowPadding() {
        return this.mCalculatedShadowPadding;
    }





// *************** Fields used by drawers ****************

    /**
     * If the determinate drawer should update its progress with an animation
     *
     * @return true to use animation, false otherwise
     */
    public final boolean isDeterminateAnimationEnabled() {
        return mDeterminateAnimationEnabled;
    }

    /**
     * @return Percentage value of the progress indicator of determinate drawers
     */
    public final float getValuePercent() {
        return mValuePercent;
    }

    /**
     * The border width of the progress indicator, after calculations.
     * This will return the real value used by the progress indicator to show.
     *
     * @return The border width of the progress indicator, after calculations.
     */
    public int getCalculatedBorderWidth() {
        return mCalculatedBorderWidth;
    }

    /**
     * @return Front color of the indicator of determinate drawers
     */
    public final int getFrontColor() {
        return mFrontColor;
    }

    /**
     * @return  Back color of the indicator of determinate drawers
     */
    public final int getBackColor() {
        return mBackColor;
    }

    /**
     * @return Color of the indicator of indeterminate drawers
     */
    public final int getIndeterminateColor() {
        return mIndeterminateColor;
    }

    /** If should show a wedge on the circular determinate drawer
     * @return If true shows a wedge, otherwise shows a circle
     */
    public final boolean isDrawWedge() {
        return mDrawWedge;
    }

    /**
     * Set the listener that will update the progress drawers on changes
     *
     * @param listener Listener that will update the progress drawers on changes
     */
    public void setListener(ProgressOptionsListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    /**
     * @return Color of the indicator shadow
     */
    public int getShadowColor() {
        return this.mShadowColor;
    }

    /**
     * @return whether to show a progress shadow
     */
    public boolean isShadowEnabled() {
        return this.mShadowEnabled;
    }









    //Parcelable stuff

    public static final Creator<ProgressOptions> CREATOR = new Creator<ProgressOptions>() {
        @Override
        public ProgressOptions createFromParcel(Parcel in) {
            return new ProgressOptions(in);
        }

        @Override
        public ProgressOptions[] newArray(int size) {
            return new ProgressOptions[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    protected ProgressOptions(Parcel in) {
        mDeterminateAnimationEnabled = in.readByte() != 0;
        mBorderWidth = in.readInt();
        mBorderWidthPercent = in.readFloat();
        mValuePercent = in.readFloat();
        mFrontColor = in.readInt();
        mBackColor = in.readInt();
        mIndeterminateColor = in.readInt();
        mDrawWedge = in.readByte() != 0;
        mSize = in.readInt();
        mPadding = in.readInt();
        mSizePercent = in.readFloat();
        mIsRtl = in.readByte() != 0;
        mRtlDisabled = in.readByte() != 0;
        mCalculatedSize = in.readInt();
        mCalculatedBorderWidth = in.readInt();
        mCalculatedLeft = in.readFloat();
        mCalculatedTop = in.readFloat();
        mCalculatedRight = in.readFloat();
        mCalculatedBottom = in.readFloat();
        mCalculatedLastW = in.readInt();
        mCalculatedLastH = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeByte((byte) (mDeterminateAnimationEnabled ? 1 : 0));
        dest.writeInt(mBorderWidth);
        dest.writeFloat(mBorderWidthPercent);
        dest.writeFloat(mValuePercent);
        dest.writeInt(mFrontColor);
        dest.writeInt(mBackColor);
        dest.writeInt(mIndeterminateColor);
        dest.writeByte((byte) (mDrawWedge ? 1 : 0));
        dest.writeInt(mSize);
        dest.writeInt(mPadding);
        dest.writeFloat(mSizePercent);
        dest.writeByte((byte) (mIsRtl ? 1 : 0));
        dest.writeByte((byte) (mRtlDisabled ? 1 : 0));
        dest.writeInt(mCalculatedSize);
        dest.writeInt(mCalculatedBorderWidth);
        dest.writeFloat(mCalculatedLeft);
        dest.writeFloat(mCalculatedTop);
        dest.writeFloat(mCalculatedRight);
        dest.writeFloat(mCalculatedBottom);
        dest.writeInt(mCalculatedLastW);
        dest.writeInt(mCalculatedLastH);
    }



    public interface ProgressOptionsListener{
        void onOptionsUpdated(ProgressOptions options);
        void onSizeUpdated(ProgressOptions options);
    }

}
