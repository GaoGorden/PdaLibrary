package p.gorden.pdalibrary.tableLayout;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import p.gorden.pdalibrary.R;


/**
 * Created by Smartown on 2017/7/19.
 */
public class TableColumn extends LinearLayout {

    private String[] content;
    private Callback callback;
    private int padding;
    private int margin;

    private float maxTextViewWidth;
    private String columnText;
    private int page;

    //构造函数，需要上下文，内容，和回调接口
    public TableColumn(Context context, int page, String[] content, Callback callback) {
        super(context);
        this.page = page;
        this.content = content;
        this.callback = callback;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (padding * 2 + maxTextViewWidth + margin * 2)
                , (callback.getTableLayout().getTableRowHeight() + margin * 2) * getChildCount());
    }

    private void init() {
        Log.e("TableColumn", "init");
        setOrientation(VERTICAL);

        initContent();
    }

    private void initContent() {
        padding = callback.getTableLayout().getTableColumnPadding();
        // 我们需要设置 margin 来解决分割线被挡住的问题。
        margin = callback.getTableLayout().getTableDividerSize();
        maxTextViewWidth = 0;
        //将textview一个个放入arraylist中。
        ArrayList<TextView> textViews = new ArrayList<>();

        int index = 0;

        for (String text : content) {
            //判断每项内容是否为空
            if (TextUtils.isEmpty(text)) {
                text = "";
            }

            TextView textView = new TextView(getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, callback.getTableLayout().getTableTextSize());
            textView.setTextColor(callback.getTableLayout().getTableTextColor());
            maxTextViewWidth = Math.max(maxTextViewWidth, Util.measureTextViewWidth(textView, text));
            textView.setGravity(getTextGravity(callback.getTableLayout().getTableTextGravity()));
            textView.setPadding(padding, 0, padding, 0);
            textView.setText(text);

            if (callback.getTableLayout().getTextColorCallback() != null && index > 0) {
                callback.getTableLayout().getTextColorCallback().setTextViewColor(textView, content[0]);
            }

            index++;

            textViews.add(textView);
        }
        LayoutParams layoutParams = new LayoutParams((int) (padding * 2 + maxTextViewWidth), callback.getTableLayout().getTableRowHeight());
        layoutParams.setMargins(margin, margin, margin, margin);
        int rowIndex = 0;
        for (TextView textView : textViews) {
            if ((textViews.indexOf(textView) == 0)) {
                //将第一行设为标题行
                textView.setTextColor(callback.getTableLayout().getHeaderColor());
                textView.setBackgroundColor(getResources().getColor(R.color.table_title_background_color));
            } else {
                if (rowIndex % 2 != 0) {
                    textView.setBackgroundColor(getResources().getColor(R.color.table_column_background_color));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.table_column_background_color2));
                }
            }

            if (callback.getTableLayout().getRowColorCallback() != null && rowIndex > 0) {
                callback.getTableLayout().getRowColorCallback().setTextViewColor(page, rowIndex, textView);
            }

            rowIndex++;
            addView(textView, layoutParams);
        }
    }

    private int getTextGravity(int tableTextGravity) {
        switch (tableTextGravity) {
            case 1:
                return Gravity.CENTER_VERTICAL;
            case 2:
                return Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        }
        return Gravity.CENTER;
    }

    public void onClick(float y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView textView = (TextView) getChildAt(i);
            if (textView.getBottom() >= y) {
                if (i == 0) {
                    return;
                }
                columnText = textView.getText().toString();
                textView.setSelected(!textView.isSelected());
                textView.setBackgroundColor(textView.isSelected() ? callback.getTableLayout().getBackgroundColorSelected() : (i % 2 != 0 ? getResources().getColor(R.color.table_column_background_color) : getResources().getColor(R.color.table_column_background_color2)));
                textView.setTextColor(textView.isSelected() ? callback.getTableLayout().getTableTextColorSelected() : callback.getTableLayout().getTableTextColor());
                return;
            }
        }
    }

    public String getColumnText() {
        return columnText;
    }


    public interface Callback {
        TableLayout getTableLayout();
    }

}
