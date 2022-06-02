package p.gorden.pdalibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.unity.UserInfo;


/**
 * Created by gorden on 2018/3/15.
 */

public class HeaderTitle extends LinearLayout {
    private TextView textView;
    private TextView buttonLeft;
    private TextView buttonRight;

    public HeaderTitle(Context context) {
        this(context, null);
    }

    public HeaderTitle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderTitle(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_header, this);
        textView = view.findViewById(R.id.header_title);
        buttonRight = view.findViewById(R.id.header_rightbutton);
        buttonLeft = view.findViewById(R.id.header_leftbutton);
        buttonLeft.setOnClickListener(view1 -> ((Activity) getContext()).finish());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderTitle);
        textView.setText(a.getText(R.styleable.HeaderTitle_headertitle));
        if (!UserInfo.USERNAME.equals("")) {
            buttonRight.setText("用户:" + UserInfo.USERNAME);
        }
//        buttonRight.setText(a.getText(R.styleable.HeaderTitle_headerbuttonname));
        if (a.getBoolean(R.styleable.HeaderTitle_headerbuttonvisable, true)) {
            buttonLeft.setVisibility(VISIBLE);
            buttonRight.setVisibility(VISIBLE);
        } else {
            buttonLeft.setVisibility(GONE);
            buttonRight.setVisibility(GONE);
        }
        a.recycle();
    }

    public void setHeaderTitle(String title) {
        textView.setText(title);
    }

    public String getHeaderTitle() {
        return textView.getText().toString();
    }

    public void setRightButtonText(String text) {
        buttonRight.setText(text);
    }

    public TextView getButtonLeft() {
        return buttonLeft;
    }

    public TextView getButtonRight() {
        return buttonRight;
    }

    public void setHeader_right_button_click(OnClickListener listener) {
        buttonRight.setOnClickListener(listener);
    }

    public void setHeader_right_button_name(String text) {
        buttonRight.setText(text);
    }
}
