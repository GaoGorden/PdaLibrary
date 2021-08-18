package p.gorden.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.tableLayout.TableAdapter;
import p.gorden.pdalibrary.tableLayout.TableLayout;


/**
 * Created by Gordenyou on 2019/8/29.
 * 自定义表格控件
 */

public class TableView extends LinearLayout {

    private String[] headers; //标题行

    private int HANGSHU; //一页展示的行数

    private int yema = 1;

    private ArrayList<String[]> table_emptylist = new ArrayList<>();
    private ArrayList<String[]> temp_list = new ArrayList<>();

    TextView tv_tablename; //表的标题
    TextView tv_content; //表的内容
    TableLayout table;
    TextView tv_yema; //当前页码
    TextView tv_yemasum; //总页码
    Button bt_shangyiye; //上一页按钮
    Button bt_xiayiye; //下一页按钮

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_table, this);
        tv_tablename = view.findViewById(R.id.tb_tablename);
        tv_content = view.findViewById(R.id.tb_tv_content);
        table = view.findViewById(R.id.tb_table);
        tv_yema = view.findViewById(R.id.tb_yema);
        tv_yemasum = view.findViewById(R.id.tb_yemasum);
        bt_shangyiye = view.findViewById(R.id.tb_shangyiye);
        bt_xiayiye = view.findViewById(R.id.tb_xiayiye);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        tv_tablename.setText(a.getText(R.styleable.TableView_title));//设置标题
        HANGSHU = a.getInt(R.styleable.TableView_columnnumber, 6);

//        view.setMinimumHeight(65 + R.dimen.table_row_height * (HANGSHU + 1));
        bt_shangyiye.setOnClickListener(view1 -> {
            if (yema - 1 >= 1) {
                BuildTableAndPage(temp_list, --yema);
            }
        });

        bt_xiayiye.setOnClickListener(view12 -> {
            if (yema + 1 <= Integer.parseInt(tv_yemasum.getText().toString())) {
                BuildTableAndPage(temp_list, ++yema);
            }
        });

        a.recycle();
    }

    /**
     * 设置表格内容
     * @param content 内容
     */
    public void setContent(String content){
        tv_content.setText(content);
    }

    /**
     * 绘制表格
     *
     * @param headers  表格中文列标题
     * @param Columns  json数据中每一列的键值
     * @param data     json数据
     * @param jsonname json数据键值
     */
    public void initTableView(String[] headers, String[] Columns, String data, String jsonname) {
        this.headers = headers;
        try {
            JSONArray jsonArray = new JSONObject(data).getJSONArray(jsonname);
            temp_list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] temp = new String[Columns.length];
                int j = 0;
                for (String column : Columns) {
                    temp[j] = jsonArray.optJSONObject(i).getString(column);
                    j++;
                }
                temp_list.add(temp);
            }
            yema = 1;
            BuildTableAndPage(temp_list, yema);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制表格
     *
     * @param headers 表格中文列标题
     * @param list    数据
     */
    public void initTableView(String[] headers, ArrayList<String[]> list) {
        this.temp_list = list;
        this.headers = headers;
        yema = 1;
        BuildTableAndPage(temp_list, yema);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        BuildTableAndPage(temp_list, yema);
    }

    /**
     * 绘制表格细节
     *
     * @param data_list 数据列表
     */
    private void firstRowAsTitle(ArrayList<String[]> data_list) {
        final ArrayList<String[]> list = data_list;
        table.setAdapter(new TableAdapter() {
            @Override
            public int getColumnCount() {
                return headers.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = list.size() + 1;
                String[] contents = new String[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    //设置标题栏
                    if (i == 0) {
                        contents[i] = headers[position];
                    } else {
                        String[] temp = list.get(i - 1);
                        contents[i] = temp[position];
                    }
                }
                return contents;
            }
        });
    }

    /**
     * 清空表格数据
     */
    public void clearData() {
        tv_content.setText("");
        headers = new String[]{};
        firstRowAsTitle(table_emptylist);
    }

    /**
     * 一页一页的绘制
     *
     * @param temp_list 表格数据
     * @param yema      具体页码
     */
    private void BuildTableAndPage(ArrayList<String[]> temp_list, int yema) {
        int qishi = yema * HANGSHU < temp_list.size() ? yema * HANGSHU : temp_list.size();
        int jiewei = (yema - 1 > 0 ? yema - 1 : 0) * HANGSHU;
        ArrayList<String[]> list = new ArrayList<>();
        for (int i = qishi - 1; i >= jiewei; i--) {
            list.add(temp_list.get(i));
        }
        firstRowAsTitle(list);
        tv_yema.setVisibility(View.VISIBLE);
        tv_yemasum.setVisibility(View.VISIBLE);
        tv_yema.setText(String.valueOf(yema));
        if (temp_list.size() % HANGSHU == 0) {
            tv_yemasum.setText(String.valueOf(temp_list.size() / HANGSHU));
        } else {
            tv_yemasum.setText(String.valueOf((temp_list.size() / HANGSHU) + 1));
        }
    }
}
