package com.fb.jjyyzjy.androidtvutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.fb.jjyyzjy.lib.focus.FocusScaleUtils;
import com.fb.jjyyzjy.lib.focus.FocusUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View view;
    private FocusUtils focusUtils;
    private View btnTop;
    private View btnLeft;
    private View btnRight;
    private View btnBottom;
    private View view5;
    private View view6;
    private View view7;
    private FocusScaleUtils scaleUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        view.setFocusable(false);
        setContentView(view);
        initVIew();
        initListener();
        initFocus();
    }

    private void initFocus() {
        focusUtils = new FocusUtils(this, view, R.drawable.image_focus);
        scaleUtils = new FocusScaleUtils(1.4f);
        View view1 = findViewById(R.id.view1);
        view1.post(view1::requestFocus);
    }

    private void initVIew() {
        btnTop = findViewById(R.id.btn_t);
        btnLeft = findViewById(R.id.btn_l);
        btnRight = findViewById(R.id.btn_r);
        btnBottom = findViewById(R.id.btn_b);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
    }

    private void initListener() {
        btnTop.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnBottom.setOnClickListener(this);
        view.getViewTreeObserver().addOnGlobalFocusChangeListener((oldFocus, newFocus) -> {

            if (newFocus == view5 || newFocus == view6 || newFocus == view7) {
                scaleUtils.scaleToNormal(oldFocus);
                scaleUtils.scaleToLarge(newFocus);
                focusUtils.startMoveFocus(newFocus,true,scaleUtils.getScale());
            } else {
                scaleUtils.scaleToNormal();
                focusUtils.startMoveFocus(newFocus);
            }


        });
    }

    @Override
    public void onClick(View v) {
        View nextFocus = view.findFocus().focusSearch(getDirection(v));
        if (nextFocus != null) {
            nextFocus.requestFocus();
        }
    }

    private int getDirection(View v) {
        if (v == btnTop) {
            return View.FOCUS_UP;
        } else if (v == btnLeft) {
            return View.FOCUS_LEFT;
        } else if (v == btnRight) {
            return View.FOCUS_RIGHT;
        } else if (v == btnBottom) {
            return View.FOCUS_DOWN;
        }
        return View.FOCUS_RIGHT;
    }
}
