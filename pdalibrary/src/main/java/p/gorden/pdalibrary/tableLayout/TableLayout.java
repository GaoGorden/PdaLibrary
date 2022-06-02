package p.gorden.pdalibrary.tableLayout;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.animation.Easing;

import p.gorden.pdalibrary.R;

/**
 * Created by Smartown on 2017/7/19.
 */
public class TableLayout extends LinearLayout implements TableColumn.Callback {

    private int tableMode;
    private int tableRowHeight;
    private int tableDividerSize;
    private int tableDividerColor;
    private int tableColumnPadding;
    private int tableTextGravity;
    private int tableTextSize;
    private int tableTextColor;
    private int tableTextColorSelected;
    private int backgroundColorSelected;
    private TableAdapter adapter;
    private int headerColor;
    private String[] headers;
    private int page;

    private Paint paint;
    protected ChartAnimator mAnimator;

    private ClickCallback clickCallback;
    private TextColorCallback textColorCallback;
    private RowColorCallback rowColorCallback;

    public TableLayout(Context context) {
        super(context);
        init(null);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setTableRowHeight(int tableRowHeight) {
        this.tableRowHeight = tableRowHeight;
        invalidate();
    }

    public void setTableTextSize(int tableTextSize) {
        this.tableTextSize = tableTextSize;
        invalidate();
    }

    public void setClickCallback(ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    public void setTableTextGravity(int tableTextGravity) {
        this.tableTextGravity = tableTextGravity;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private void init(AttributeSet attrs) {
        Log.e("TableLayout", "init");
        setOrientation(HORIZONTAL);
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);

        mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // ViewCompat.postInvalidateOnAnimation(Chart.this);
                postInvalidate();
            }
        });

        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.TableLayout);
            tableMode = typedArray.getInt(R.styleable.TableLayout_tableMode, 0);
            tableRowHeight = typedArray.getDimensionPixelSize(R.styleable.TableLayout_tableRowHeight, (int) Util.dip2px(getResources(), 36));
            tableDividerSize = typedArray.getDimensionPixelSize(R.styleable.TableLayout_tableDividerSize, 1);
            tableDividerColor = typedArray.getColor(R.styleable.TableLayout_tableDividerColor, Color.GRAY);
            tableColumnPadding = typedArray.getDimensionPixelSize(R.styleable.TableLayout_tableColumnPadding, 0);
            tableTextGravity = typedArray.getInt(R.styleable.TableLayout_tableTextGravity, 0);
            tableTextSize = typedArray.getDimensionPixelSize(R.styleable.TableLayout_tableTextSize, (int) Util.dip2px(getResources(), 12));
            tableTextColor = typedArray.getColor(R.styleable.TableLayout_tableTextColor, Color.GRAY);
            tableTextColorSelected = typedArray.getColor(R.styleable.TableLayout_tableTextColorSelected, Color.BLACK);
            backgroundColorSelected = typedArray.getColor(R.styleable.TableLayout_backgroundColorSelected, Color.TRANSPARENT);
            headerColor = typedArray.getColor(R.styleable.TableLayout_headerColor, Color.BLUE);
            typedArray.recycle();
        } else {
            tableMode = 0;
            tableRowHeight = (int) Util.dip2px(getResources(), 36);
            tableDividerSize = 1;
            tableDividerColor = Color.GRAY;
            tableColumnPadding = 0;
            tableTextGravity = 0;
            tableTextSize = (int) Util.dip2px(getResources(), 12);
            tableTextColor = Color.GRAY;
            tableTextColorSelected = Color.BLACK;
            backgroundColorSelected = Color.TRANSPARENT;
            headerColor = Color.WHITE;
        }
        if (isInEditMode()) {
            String[] content = {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa"};
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
            addView(new TableColumn(getContext(), 1, content, this));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TableLayout", "onMeasure");
        int width = 0;
        int height = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            width += child.getMeasuredWidth();
            height = Math.max(height, child.getMeasuredHeight());
        }
        setMeasuredDimension(width, height);
    }

    //拦截触碰屏幕事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TableLayout", "onDraw");
        Log.e("TableLayout", "onDraw,height: " + getHeight());
        paint.setColor(tableDividerColor);

        int drawnWidth = 0;
        int maxRowCount = 0;
        int childCount = getChildCount();
        // 绘制每列之间的Divider
        for (int i = 0; i < childCount; i++) {
            TableColumn column = (TableColumn) getChildAt(i);
            maxRowCount = Math.max(maxRowCount, column.getChildCount());
            if (i > 0) {
                if (tableDividerSize > 1) {
                    canvas.drawRect(drawnWidth - tableDividerSize / 2, 0, drawnWidth + tableDividerSize / 2, getHeight(), paint);
                } else {
                    canvas.drawRect(drawnWidth - tableDividerSize, 0, drawnWidth, getHeight(), paint);
                }
            }
            drawnWidth += column.getWidth();
        }

        // 绘制每行之间的Divider
        for (int i = 1; i < maxRowCount; i++) {
            float y = i * (tableRowHeight + tableDividerSize * 2);
            if (tableDividerSize > 1) {
                canvas.drawRect(0, y - tableDividerSize / 2, getWidth(), y + tableDividerSize / 2, paint);
            } else {
                canvas.drawRect(0, y - tableDividerSize, getWidth(), y, paint);
            }
        }

        // 绘制 Layout 之外的Divider
        canvas.drawRect(0, 0, tableDividerSize, getHeight(), paint); // 左
        canvas.drawRect(getWidth() - tableDividerSize, 0, getWidth(), getHeight(), paint); // 右
        canvas.drawRect(0, 0, getWidth(), tableDividerSize, paint); // 上
        canvas.drawRect(0, getHeight() - tableDividerSize, getWidth(), getHeight(), paint); // 下
    }

    @Override
    public TableLayout getTableLayout() {
        return this;
    }

    public int getTableMode() {
        return tableMode;
    }

    public int getTableRowHeight() {
        return tableRowHeight;
    }

    public int getTableDividerSize() {
        return tableDividerSize;
    }

    public int getTableDividerColor() {
        return tableDividerColor;
    }

    public int getTableColumnPadding() {
        return tableColumnPadding;
    }

    public int getTableTextGravity() {
        return tableTextGravity;
    }

    public int getTableTextSize() {
        return tableTextSize;
    }

    public String[] getHeaders() {
        return headers;
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public int getTableTextColor() {
        return tableTextColor;
    }

    public int getTableTextColorSelected() {
        return tableTextColorSelected;
    }

    public int getBackgroundColorSelected() {
        return backgroundColorSelected;
    }

    public void setAdapter(TableAdapter adapter) {
        Log.e("TableLayout", "setAdapter");
        this.adapter = adapter;
        useAdapter();
    }

    private void useAdapter() {
        removeAllViews();
        int count = adapter.getColumnCount();
        headers = adapter.getHeader();
        for (int i = 0; i < count; i++) {
            addView(new TableColumn(getContext(),adapter.getPage(), adapter.getColumnContent(i), this));
        }
    }

    public void onClick(float x, float y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TableColumn tableColumn = (TableColumn) getChildAt(i);
            if (tableColumn.getRight() >= x) {
                if (i == 0) {
                    tableColumn.onClick(y);
                    if (clickCallback != null) {
                        clickCallback.selectedContent(tableColumn.getColumnText());
                    }
                    return;
                }
            }
        }
    }

    public void setTextColorCallback(TextColorCallback textColorCallback) {
        this.textColorCallback = textColorCallback;
    }

    public TextColorCallback getTextColorCallback() {
        return textColorCallback;
    }

    public RowColorCallback getRowColorCallback() {
        return rowColorCallback;
    }

    public void setRowColorCallback(RowColorCallback rowColorCallback) {
        this.rowColorCallback = rowColorCallback;
    }

    public interface TextColorCallback {
        /**
         * 设置不同行的颜色
         *
         * @param textView 当前TextView
         * @param title    当前列的标题
         */
        void setTextViewColor(TextView textView, String title);
    }

    public interface RowColorCallback {
        /**
         * 设置不同行的颜色
         *
         * @param textView 当前TextView
         * @param rowIndex 当前行
         */
        void setTextViewColor(int page, int rowIndex, TextView textView);
    }

    public interface ClickCallback {
        void selectedContent(String content);
    }

}
