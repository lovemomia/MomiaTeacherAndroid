package com.youxing.sogoteacher.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/6/12.
 */
public class InputListItem extends LinearLayout {

    private TextView titleTv;
    private EditText inputEt;

    private InputChangeListener inputChangeListener;

    public InputListItem(Context context) {
        this(context, null);
    }

    public InputListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static InputListItem create(Context context) {
        return (InputListItem)LayoutInflater.from(context).inflate(R.layout.layout_list_item_input, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView)findViewById(R.id.title);
        inputEt = (EditText)findViewById(R.id.input);
        inputEt.addTextChangedListener(new InputTextWatcher(this));
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setInputHint(String hint) {
        inputEt.setHint(hint);
    }

    public void setInputText (String text) {
        inputEt.setText(text);
    }

    public void setInputChangeListener(InputChangeListener listener) {
        this.inputChangeListener = listener;
    }

    class InputTextWatcher implements TextWatcher {
        private InputListItem host;

        InputTextWatcher(InputListItem host) {
            this.host = host;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (inputChangeListener != null) {
                inputChangeListener.onInputChanged(host, s.toString());
            }
        }
    }

    public interface InputChangeListener {
        void onInputChanged(InputListItem inputListItem, String textNow);
    }
}
