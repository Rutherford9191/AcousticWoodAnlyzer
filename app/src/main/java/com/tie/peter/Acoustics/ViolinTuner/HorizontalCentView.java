
package com.tie.peter.Acoustics.ViolinTuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class HorizontalCentView extends AbstractCentView {
    public HorizontalCentView(Context c) {
        super(c);
    }

    /* This and the next constructor are necessary for inflating from XML */
    public HorizontalCentView(Context c, android.util.AttributeSet attrs) {
        super(c, attrs);
    }

    public HorizontalCentView(Context c, android.util.AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
    }

    @Override
    protected void drawNeedle(Canvas canvas) {
        float c;
        if (animating)
            c = animCents;
        else
            c = cents;

        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);

        float x = width / 2;
        x += c / 100 * width;

        canvas.drawLine(x, 0 + 15, x, height - 15, paint);
    }

    @Override
    protected void drawStatic(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);

        canvas.drawLine(width / 2, 0 + 10, width / 2, height - 10, paint);
    }
}
