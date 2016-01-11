package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class StepperView extends LinearLayout implements View.OnClickListener {

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 99;

    private ImageButton minusIv;
    private ImageButton plusIv;
    private TextView numberTv;

    private int min;
    private int max;

    private OnNumberChangedListener listener;

    public StepperView(Context context) {
        this(context, null);
    }

    public StepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static StepperView create(Context context) {
        StepperView view = (StepperView) LayoutInflater.from(context).inflate(R.layout.layout_stepper, null);
        return view;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setNumber(int number) {
        numberTv.setText(String.valueOf(number));
        update();
    }

    public int getNumber() {
        int num;
        try {
            num = Integer.valueOf(numberTv.getText().toString());
        } catch (Exception e) {
            num = 0;
        }
        return num;
    }

    public void setListener(OnNumberChangedListener l) {
        listener = l;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        minusIv = (ImageButton) findViewById(R.id.stepper_minus);
        plusIv = (ImageButton) findViewById(R.id.stepper_plus);
        numberTv = (TextView) findViewById(R.id.stepper_number);

        minusIv.setOnClickListener(this);
        plusIv.setOnClickListener(this);

        setMin(DEFAULT_MIN);
        setMax(DEFAULT_MAX);
        setNumber(0);
        update();
    }

    private void update() {
        int num = getNumber();
        if (num == min) {
            minusIv.setEnabled(false);
        } else if (num == max) {
            plusIv.setEnabled(false);
        } else {
            minusIv.setEnabled(true);
            plusIv.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stepper_minus) {
            setNumber(getNumber() - 1);
            update();

            if (listener != null) {
                listener.onNumberChanged(this);
            }

        } else if (v.getId() == R.id.stepper_plus) {
            setNumber(getNumber() + 1);
            update();

            if (listener != null) {
                listener.onNumberChanged(this);
            }
        }
    }

    public interface OnNumberChangedListener {
        void onNumberChanged(StepperView stepper);
    }
}
