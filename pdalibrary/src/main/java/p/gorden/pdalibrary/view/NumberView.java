package p.gorden.pdalibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import p.gorden.pdalibrary.R;


/**
 * Created by gorden on 2018/3/8.
 */
@InverseBindingMethods({
        @InverseBindingMethod(type = NumberView.class
                , attribute = "number_edit_text"
                , event = "editTextAttrChanged")
})
public class NumberView extends LinearLayout {
    private TextView title;
    private EditText content;

    private double defultnumber;

    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_number_wide, this);
        title = view.findViewById(R.id.number_title);
        content = view.findViewById(R.id.number_content);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberView);

        title.setText(a.getText(R.styleable.NumberView_numbertitle));
        if (a.getText(R.styleable.NumberView_numberdefult) != null) {
            defultnumber = Double.parseDouble(a.getText(R.styleable.NumberView_numberdefult).toString());
            content.setText(String.valueOf(defultnumber));
        }

        content.setHint(a.getText(R.styleable.NumberView_numberhint));

        content.setOnTouchListener((v, event) -> {
//                Log.e(TAG,"触摸监听被触发"+System.currentTimeMillis());
            content.requestFocus();
            content.setSelection(content.getText().length());
            return false;
        });
        a.recycle();
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public EditText getEdittext() {
        return content;
    }

    public double getNumber() {
        if (!content.getText().toString().equals("")) {
            return Double.parseDouble(content.getText().toString());
        } else {
            return defultnumber;
        }
    }

    public void setNumber(String number){
        content.setText(number);
    }

    public void setNumber_edit_text(double number) {
        content.setText(String.valueOf(number));
    }

    public double getNumber_edit_text() {
        return Double.parseDouble(content.getText().toString());
    }

    public void setEditTextAttrChanged(final InverseBindingListener listener) {
        if (listener != null) {
            content.setOnFocusChangeListener((view, b) -> listener.onChange());
        }
    }



}
