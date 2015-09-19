package com.aumentia.vs.helloMotion;

/**
 * com.aumentia.vs.helloVisualSearch
 * VisualSearchSDK
 * <p/>
 * Created by Pablo GM on 17/09/15.
 * Copyright (c) 2015 Aumentia Technologies. All rights reserved.
 */

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aumentia.vs.visualsearchsdk.API.OnMotionDetected;
import com.aumentia.vs.visualsearchsdk.API.VSAumentia;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class HelloMotion extends Activity implements OnMotionDetected
{

    //--- GLOBAL VARIABLES -------------------------------------------------------------------------

    // Layout to place the camera
    private FrameLayout frame;

    // VS instance
    private VSAumentia vsAumentia;

    // TextView to display the "pressed" button
    private TextView textView    = null;

    // App API_KEY
    private static final String     API_KEY     = "a6a4026a1523a975dbe0a84e1666c851d85d8ae4";

    // Log debug tag
    public static final String     HELLO_TAG    = "HelloMotionDetection";

    int PreviewSizeWidth, PreviewSizeHeight;


    //----------------------------------------------------------------------------------------------

    // *********************************
    // * Life Cycle                    *
    // *********************************

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Get full screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        PreviewSizeWidth   = dm.widthPixels;
        PreviewSizeHeight  = dm.heightPixels;

        // FrameLayout where we will add the camera view
        frame = (FrameLayout) findViewById(R.id.cameraFrameId);

        // Get singleton
        vsAumentia = VSAumentia.getmInstance();

        // Init Visual Search engine
        vsAumentia.init(this, API_KEY, VSAumentia.SCREEN_ORIENTATION_PORTRAIT, PreviewSizeWidth, PreviewSizeHeight, true, ImageFormat.NV21, frame, VSAumentia.MOTION_DETECTION_ENGINE);

        // Set the period the buttons will be inactive once one is clicked
        vsAumentia.setInactivePeriod(5);

        // Don't save frame
        vsAumentia.setSaveMotionFrameToSD(false);

        // Set callback
        vsAumentia.setMotionDetectionCallback(this);

        // Add text view to show the "pressed" button
        addTextView();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Log.d(HELLO_TAG, "*** onStart() *** ");
    }

    @Override
    public void onResume()
    {
        super.onResume();

        vsAumentia.start();

        Log.d(HELLO_TAG, "*** onResume() *** ");

        // Add virtual buttons
        addButtons();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        Log.d(HELLO_TAG, "*** onStop() ***");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // UnRegister callback
        vsAumentia.setMotionDetectionCallback(null);

        vsAumentia.stop();

        vsAumentia.release();

        System.gc();

        Log.d(HELLO_TAG, "*** onDestroy() ***");
    }

    @Override
    public void onPause()
    {
        super.onPause();

        vsAumentia.stop();

        Log.d(HELLO_TAG, "*** onPause() ***");
    }

    // *********************************
    // * Add / Remove ROIs             *
    // *********************************

    private void addButtons()
    {
        // Button 1
        //
        Rect rect   = new Rect();
        rect.top    = 5;
        rect.left   = 5;
        rect.bottom = 25;
        rect.right  = 25;

        if ( vsAumentia.addButtonWithRect(rect) == -1 )
        {
            Log.d(HELLO_TAG, "Error adding button 1.");
        }
        else
        {
            drawButton(rect);
        }

        // Button 2
        //
        Rect rect2  = new Rect();
        rect2.top    = 80;
        rect2.left   = 80;
        rect2.bottom = 95;
        rect2.right  = 95;

        if ( vsAumentia.addButtonWithRect(rect2) == -1 )
        {
            Log.d(HELLO_TAG, "Error adding button 2.");
        }
        else
        {
            drawButton(rect2);
        }
    }

    private void drawButton(Rect rect)
    {
        Rect r = new Rect();
        r.left      = Math.round(rect.left / 100.0f * PreviewSizeWidth);
        r.top       = Math.round(rect.top / 100.0f * PreviewSizeHeight);
        r.right     = Math.round(rect.right / 100.0f * PreviewSizeWidth);
        r.bottom    = Math.round(rect.bottom / 100.0f * PreviewSizeHeight);

        ROI roi = new ROI(this, r);
        roi.setDrawingCacheBackgroundColor(Color.BLUE);
        addContentView(roi, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    // *********************************
    // * MOTION CALLBACKs              *
    // *********************************

    @Override
    public void onButtonClicked(final int buttonId)
    {
        Log.i(HELLO_TAG, "Clicked button " + buttonId);

        textView.setText("Pressed button " + buttonId);
    }

    @Override
    public void buttonsActive(boolean isActive)
    {
        if ( !isActive )
        {
            Log.i(HELLO_TAG, "Buttons are inactive!");
        }
    }

    // *********************************
    // * SHOW RESULTS                  *
    // *********************************

    private void addTextView()
    {
        textView = new TextView(this);

        textView.setTextColor(Color.LTGRAY);

        textView.setTextSize(30);

        FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        addContentView(textView,frame);
    }
}
