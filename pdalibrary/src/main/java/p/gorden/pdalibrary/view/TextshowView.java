package p.gorden.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import p.gorden.pdalibrary.R;


/**
 * Created by Gordenyou on 2018/3/8.
 */
@InverseBindingMethods({
        @InverseBindingMethod(type = TextshowView.class
                , attribute = "show_edit_text"
                , event = "editTextAttrChanged")
})
public class TextshowView extends LinearLayout {
    private TextView title;
    private TextView content;
    public TextshowView(Context context) {
        this(context, null);
    }

    public TextshowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextshowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_textshow, this);
        title = view.findViewById(R.id.textshow_title);
        content = view.findViewById(R.id.textshow_content);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextshowView);

        title.setText(a.getText(R.styleable.TextshowView_textshowtitle));
        title.setHint(a.getText(R.styleable.TextshowView_textshowtitle));
        a.recycle();
    }

    public TextView getTextview(){
        return content;
    }

    public void setText(String text){
        content.setText(text);
    }

    public String getText(){
        return content.getText().toString();
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public void setShow_edit_text(String text) {
        content.setText(text);
    }

    public String getShow_edit_text() {
        return content.getText().toString();
    }

    public void setEditTextAttrChanged(final InverseBindingListener listener) {
        if (listener != null) {
            content.setOnFocusChangeListener((view, b) -> listener.onChange());
        }
    }

    public void setTitle(String text){
        title.setText(text);
    }
}
