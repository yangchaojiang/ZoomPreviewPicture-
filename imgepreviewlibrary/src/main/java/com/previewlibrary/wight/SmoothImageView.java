package com.previewlibrary.wight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.previewlibrary.R;

import java.lang.reflect.Field;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by yangc on 2017/4/26.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 缩放图片
***/
public class SmoothImageView extends PhotoView {

  public   enum Status {
        STATE_NORMAL,
        STATE_IN,
        STATE_OUT,
        STATE_MOVE,

    }

    private Status mStatus = Status.STATE_NORMAL;
    private static final int TRANSFORM_DURATION = 300;
    private Paint mPaint;
    private Matrix matrix;
    private Bitmap mBitmap;

    private Transform startTransform;
    private Transform endTransform;
    private Transform animTransform;
    private Rect thumbRect;
    private boolean transformStart;

    private class Transform implements Cloneable {
        float left, top, width, height;
        int alpha;
        float scale;

        public Transform clone() {
            Transform obj = null;
            try {
                obj = (Transform) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return obj;
        }
    }

    public SmoothImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSmoothImageView();
    }

    public SmoothImageView(Context context) {
        super(context);
        initSmoothImageView();
    }

    private void initSmoothImageView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF000000);
        matrix = new Matrix();
        setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public boolean checkMinScale() {
        if (getScale() != 1) {
            setScale(1, true);
            return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        if (mStatus == Status.STATE_OUT || mStatus == Status.STATE_IN) {
            if (startTransform == null || endTransform == null || animTransform == null) {
                initTransform();
            }

            if (animTransform == null) {
                super.onDraw(canvas);
                return;
            }

            mPaint.setAlpha(animTransform.alpha);
            canvas.drawPaint(mPaint);
            int saveCount = canvas.getSaveCount();
            matrix.setScale(animTransform.scale, animTransform.scale);
            float translateX = -(mBitmap.getWidth() * animTransform.scale - animTransform.width) / 2;
            float translateY = -(mBitmap.getHeight() * animTransform.scale - animTransform.height) / 2;
            matrix.postTranslate(translateX, translateY);

            canvas.translate(animTransform.left, animTransform.top);
            canvas.clipRect(0, 0, animTransform.width, animTransform.height);
            canvas.concat(matrix);
            getDrawable().draw(canvas);
            canvas.restoreToCount(saveCount);

            if (transformStart) {
                startTransform();
            }
        } else if (mStatus == Status.STATE_MOVE) {
            mPaint.setAlpha(0);
            canvas.drawPaint(mPaint);
            super.onDraw(canvas);
        } else {
            mPaint.setAlpha(255);
            canvas.drawPaint(mPaint);
            super.onDraw(canvas);
        }
    }

    private int downX, downY;
    private boolean isMoved = false;
    private boolean isDownPhoto = false;
    private int alpha = 0;
    private static final int MIN_TRANS_DEST = 5;
    private static final float MAX_TRANS_SCALE = 0.5f;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getScale() == 1) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                    if (markTransform == null) {
                        initTransform();
                    }
                    isDownPhoto = false;
                    if (markTransform != null) {
                        int startY = (int) markTransform.top;
                        int endY = (int) (markTransform.height + markTransform.top);
                        if (downY >= startY && endY >= downY) {
                            isDownPhoto = true;
                        }
                    }

                    isMoved = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isDownPhoto) {
                        break;
                    }

                    int mx = (int) event.getX();
                    int my = (int) event.getY();

                    int offsetX = mx - downX;
                    int offsetY = my - downY;

                    // 水平方向移动不予处理
                    if (!isMoved && (Math.abs(offsetX) > Math.abs(offsetY) || Math.abs(offsetY) < MIN_TRANS_DEST)) {
                        return super.dispatchTouchEvent(event);
                    } else {
                        // 一指滑动时，才对图片进行移动缩放处理
                        if (event.getPointerCount() == 1) {
                            mStatus = Status.STATE_MOVE;
                            offsetLeftAndRight(offsetX);
                            offsetTopAndBottom(offsetY);
                            float scale = moveScale();
                            float scaleXY = 1 - scale * 0.1f;
                            setScaleY(scaleXY);
                            setScaleX(scaleXY);
                            isMoved = true;
                            alpha = (int) (255 * (1 - scale * 0.5f));
                            invalidate();
                            if (alpha < 0) {
                                alpha = 0;
                            }
                            if (alphaChangeListener != null) {
                                alphaChangeListener.onAlphaChange(alpha);
                            }
                            return true;
                        }
                        // 多指滑动，直接屏蔽事件
                        else {
                            return true;
                        }
                    }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isMoved) {
                        if (moveScale() <= MAX_TRANS_SCALE) {
                            moveToOldPosition();
                        } else {
                            changeTransform();
                            if (transformOutListener != null) {
                                transformOutListener.onTransformOut();
                            }
                        }
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 未达到关闭的阈值松手时，返回到初始位置
     */
    private void moveToOldPosition() {
        ValueAnimator va = ValueAnimator.ofInt(getTop(), 0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int startValue = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (startValue != 0) {
                    offsetTopAndBottom(value - startValue);
                }
                startValue = value;
            }
        });

        ValueAnimator leftAnim = ValueAnimator.ofInt(getLeft(), 0);
        leftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int startValue = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (startValue != 0) {
                    offsetLeftAndRight(value - startValue);
                }
                startValue = value;
            }
        });

        ValueAnimator alphaAnim = ValueAnimator.ofInt(alpha, 255);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (alphaChangeListener != null) {
                    alphaChangeListener.onAlphaChange((Integer) animation.getAnimatedValue());
                }
            }
        });

        ValueAnimator scaleAnim = ValueAnimator.ofFloat(getScaleX(), 1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                setScaleX(scale);
                setScaleY(scale);
            }
        });

        AnimatorSet as = new AnimatorSet();
        as.setDuration(TRANSFORM_DURATION);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        as.playTogether(va, leftAnim, scaleAnim, alphaAnim);
        as.start();
    }

    private float moveScale() {
        if (markTransform == null) {
            initTransform();
        }
        return Math.abs(getTop() / markTransform.height);
    }

    private OnAlphaChangeListener alphaChangeListener;
    private OnTransformOutListener transformOutListener;


    public void setTransformOutListener(OnTransformOutListener transformOutListener) {
        this.transformOutListener = transformOutListener;
    }

    public void setAlphaChangeListener(OnAlphaChangeListener alphaChangeListener) {
        this.alphaChangeListener = alphaChangeListener;
    }

    public interface OnTransformOutListener {
        void onTransformOut();
    }

    public interface OnAlphaChangeListener {
        void onAlphaChange(int alpha);
    }

    private Transform markTransform;

    private void changeTransform() {
        if (markTransform != null) {
            Transform tempTransform = markTransform.clone();
            tempTransform.top = markTransform.top + getTop();
            tempTransform.left = markTransform.left + getLeft();
            tempTransform.alpha = alpha;
            tempTransform.scale = markTransform.scale - (1 - getScaleX()) * markTransform.scale;
            animTransform = tempTransform.clone();
            endTransform = tempTransform.clone();
        }
    }

    private void startTransform() {
        transformStart = false;
        if (animTransform == null) {
            return;
        }

        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(TRANSFORM_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (mStatus == Status.STATE_IN) {
            PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("animScale", startTransform.scale, endTransform.scale);
            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("animAlpha", startTransform.alpha, endTransform.alpha);
            PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("animLeft", startTransform.left, endTransform.left);
            PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("animTop", startTransform.top, endTransform.top);
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("animWidth", startTransform.width, endTransform.width);
            PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("animHeight", startTransform.height, endTransform.height);
            animator.setValues(scaleHolder, alphaHolder, leftHolder, topHolder, widthHolder, heightHolder);
        } else if (mStatus == Status.STATE_OUT) {
            PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("animScale", endTransform.scale, startTransform.scale);
            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("animAlpha", endTransform.alpha, startTransform.alpha);
            PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("animLeft", endTransform.left, startTransform.left);
            PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("animTop", endTransform.top, startTransform.top);
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("animWidth", endTransform.width, startTransform.width);
            PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("animHeight", endTransform.height, startTransform.height);
            animator.setValues(scaleHolder, alphaHolder, leftHolder, topHolder, widthHolder, heightHolder);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animTransform.alpha = (Integer) animation.getAnimatedValue("animAlpha");
                animTransform.scale = (float) animation.getAnimatedValue("animScale");
                animTransform.left = (float) animation.getAnimatedValue("animLeft");
                animTransform.top = (float) animation.getAnimatedValue("animTop");
                animTransform.width = (float) animation.getAnimatedValue("animWidth");
                animTransform.height = (float) animation.getAnimatedValue("animHeight");
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                /*
                 * 如果是进入的话，当然是希望最后停留在center_crop的区域。但是如果是out的话，就不应该是center_crop的位置了
				 * ， 而应该是最后变化的位置，因为当out的时候结束时，不回复视图是Normal，要不然会有一个突然闪动回去的bug
				 */
                if (onTransformListener != null) {
                    onTransformListener.onTransformCompleted(mStatus);
                }
                if (mStatus == Status.STATE_IN) {
                    mStatus = Status.STATE_NORMAL;
                }
            }
        });
        animator.start();

    }

    public void transformIn(onTransformListener listener) {
        setOnTransformListener(listener);
        transformStart = true;
        mStatus = Status.STATE_IN;
        invalidate();
    }

    public void transformOut(onTransformListener listener) {
        if (getTop() != 0) {
            offsetTopAndBottom(-getTop());
        }
        if (getLeft() != 0) {
            offsetLeftAndRight(-getLeft());
        }
        if (getScaleX() != 1) {
            setScaleX(1);
            setScaleY(1);
        }
        setOnTransformListener(listener);
        transformStart = true;
        mStatus = Status.STATE_OUT;
        invalidate();
    }

    /**
     * 设置起始位置图片的Rect
     *
     * @param thumbRect
     */
    public void setThumbRect(Rect thumbRect) {
        this.thumbRect = thumbRect;
    }

    private void initTransform() {
        if (getDrawable() == null) {
            return;
        }
        if (startTransform != null && endTransform != null && animTransform != null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (mBitmap == null) {
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        }

        startTransform = new Transform();
        startTransform.alpha = 0;
        if (thumbRect == null) {
            thumbRect = new Rect();
        }
        startTransform.left = thumbRect.left;
        startTransform.top = thumbRect.top - getStatusBarHeight(getContext());
        startTransform.width = thumbRect.width();
        startTransform.height = thumbRect.height();

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        //开始时以CenterCrop方式显示，缩放图片使图片的一边等于起始区域的一边，另一边大于起始区域
        float startScaleX = (float) thumbRect.width() / bitmapWidth;
        float startScaleY = (float) thumbRect.height() / bitmapHeight;
        startTransform.scale = startScaleX > startScaleY ? startScaleX : startScaleY;
        //结束时以fitCenter方式显示，缩放图片使图片的一边等于View的一边，另一边大于View
        float endScaleX = (float) getWidth() / bitmapWidth;
        float endScaleY = (float) getHeight() / bitmapHeight;

        endTransform = new Transform();
        endTransform.scale = endScaleX < endScaleY ? endScaleX : endScaleY;
        endTransform.alpha = 255;
        int endBitmapWidth = (int) (endTransform.scale * bitmapWidth);
        int endBitmapHeight = (int) (endTransform.scale * bitmapHeight);
        endTransform.left = (getWidth() - endBitmapWidth) / 2;
        endTransform.top = (getHeight() - endBitmapHeight) / 2;
        endTransform.width = endBitmapWidth;
        endTransform.height = endBitmapHeight;

        if (mStatus == Status.STATE_IN) {
            animTransform = startTransform.clone();
        } else if (mStatus == Status.STATE_OUT) {
            animTransform = endTransform.clone();
        }

        markTransform = endTransform;
    }

    public interface onTransformListener {
        void onTransformCompleted(Status status);

    }

    private onTransformListener onTransformListener;


    public void setOnTransformListener(SmoothImageView.onTransformListener onTransformListener) {
        this.onTransformListener = onTransformListener;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = context.getResources().getDimensionPixelSize(R.dimen.yms_dimens_50_0_px);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


}
