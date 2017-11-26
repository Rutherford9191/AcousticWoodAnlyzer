package com.tie.peter.Acoustics.ViolinTuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;


public abstract class AbstractCentView extends android.support.v7.widget.AppCompatImageView {
    protected Context context;
    protected Paint paint;
    protected int width, height;
    protected float cents;
    protected float animCents;
    protected int needleColor;

    protected final Handler mHandler = new Handler();
    protected int animDuration;
    protected long animStartMs, animEndMs;
    protected float animStartCents, animEndCents;
    protected boolean animating;

    protected final Runnable animate = new Runnable() {
        public void run() {
            long now = System.currentTimeMillis();
            float timePos = (float) (now - animStartMs) / (animEndMs - animStartMs);
            animCents = animStartCents + (animEndCents - animStartCents) * timePos;
            invalidate();

            if (now < animEndMs)
                mHandler.postDelayed(animate, 15);
            else
                endAnim();
        }
    };

    public AbstractCentView(Context c) {
        super(c);
        context = c;
        initialize();
    }

    /* This and the next constructor are necessary for inflating from XML */
    public AbstractCentView(Context c, android.util.AttributeSet attrs) {
        super(c, attrs);
        context = c;
        initialize();
    }

    public AbstractCentView(Context c, android.util.AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        context = c;
        initialize();
    }

    protected void initialize() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);

        setCents(0);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        drawStatic(canvas);
        drawNeedle(canvas); // Draw second, so it's on top
    }

    protected abstract void drawStatic(Canvas canvas);
    protected abstract void drawNeedle(Canvas canvas);

    protected void startAnim(float c) {
        endAnim();
        animating = true;
        animStartMs = System.currentTimeMillis();
        animEndMs = animStartMs + animDuration;
        animStartCents = cents;
        animEndCents = c;
        animCents = cents; // In case View is invalidated before animate.run() is called
        mHandler.post(animate);
    }

    protected void endAnim() {
        animating = false;
        mHandler.removeCallbacks(animate);
    }

    public void setCents(float c) {
        if (c == cents) return;

        if (animDuration <= 0)
            invalidate();
        else
            startAnim(c);

        // Don't set until after calling startAnim(); it needs old value of cents
        cents = c;
    }

    public void setAnimationDuration(int ms) {
        animDuration = ms;
    }

    public void setNeedleColor(int c) {
        needleColor = c;
        invalidate();
    }
}
