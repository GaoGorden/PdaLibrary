package p.gorden.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.adapter.GridAdapter;

/**
 * Created by gorden on 2020/2/1.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public class GridView extends LinearLayout {

    private TextView testView;
    private RecyclerView recyclerView;
    private Context context;

    private GridAdapter gridAdapter;
    public interface OnGridItemClickListener{
        void onItemClick(String text);
    }

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_grid, this);
        testView = view.findViewById(R.id.grid_title);
        recyclerView = view.findViewById(R.id.grid_list);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridView);
        testView.setText(a.getText(R.styleable.GridView_grid_title));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, a.getInteger(R.styleable.GridView_grid_row_item_number, 3));
        recyclerView.setLayoutManager(layoutManager);

        a.recycle();
    }

    public void setGridAdapter(GridAdapter gridAdapter) {
        recyclerView.setAdapter(gridAdapter);
    }
}