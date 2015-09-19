package com.aumentia.vs.helloMotion;

/**
 * com.aumentia.vs.helloVisualSearch
 * VisualSearchSDK
 * <p/>
 * Created by Pablo GM on 17/09/15.
 * Copyright (c) 2015 Aumentia Technologies. All rights reserved.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ROI extends View
{
    Paint paint = new Paint();
    Rect rect = new Rect();

    public ROI(Context context)
    {
        super(context);
    }

    public ROI(Context context, Rect rect)
    {
        super(context);

        this.rect.top       = rect.top;
        this.rect.left      = rect.left;
        this.rect.bottom    = rect.bottom;
        this.rect.right     = rect.right;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setColor(Color.rgb(0, 158, 224));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
    }

}