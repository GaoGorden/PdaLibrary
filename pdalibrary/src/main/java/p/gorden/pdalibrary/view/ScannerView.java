package p.gorden.pdalibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.config.PdaConfig;


/**
 * Created by gorden on 2018/3/2.
 */
@InverseBindingMethods({
        @InverseBindingMethod(type = ScannerView.class
                , attribute = "scanner_edit_text"
                , event = "editTextAttrChanged")
})
public class ScannerView extends LinearLayout {
    private AutoCompleteTextView editText;
    private TextView title;
    private Button button;

    private int length = 0;
    private String[] list;

    public ScannerView(Context context) {
        this(context, null);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public ScannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_scanner, this);
        editText = view.findViewById(R.id.scanner_edittext);
        title = view.findViewById(R.id.scanner_title);
        button = view.findViewById(R.id.scanner_button);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScannerView);

        title.setText(a.getText(R.styleable.ScannerView_scannertitle));
        editText.setHint(a.getText(R.styleable.ScannerView_scannerhint));
        editText.setText(a.getText(R.styleable.ScannerView_scanner_edit_text));
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
//        if (a.getBoolean(R.styleable.ScannerView_editable, false)){
//            editText.setFocusable(true);
//        }else{
//            editText.setFocusable(false);
//        }
        if (a.getText(R.styleable.ScannerView_scannerbuttonname) != null && !a.getText(R.styleable.ScannerView_scannerbuttonname).equals("")) {
            button.setText(a.getText(R.styleable.ScannerView_scannerbuttonname));
        }
        if (a.getBoolean(R.styleable.ScannerView_buttonvisable, false)) {
            button.setVisibility(VISIBLE);
        } else {
            button.setVisibility(GONE);
        }

        int inputType = a.getInt(R.styleable.ScannerView_inputType, 0);
        switch (inputType) {
            case 0:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case 3:
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                break;
            case 4:
                disableShowSoftInput();
                break;
        }

        if (!a.getBoolean(R.styleable.ScannerView_textChange, true)) {
            editText.setFocusable(false);
        }

        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    view.showDropDown();
                }
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && v.getWindowToken() != null) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    view.showDropDown();
                }
            }
        });

//        button.setOnClickListener(v -> button.requestFocus());

//        editText.setOnTouchListener((v, event) -> {
////                Log.e(TAG,"触摸监听被触发"+System.currentTimeMillis());
////            editText.requestFocus();
//            editText.setSelection(editText.getText().length());
//            return false;
//        });


        a.recycle();
    }

    public void setTitle(String strTitle) {
        title.setText(strTitle);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public AutoCompleteTextView getEditText() {
        return editText;
    }

    public Button getButton() {
        return button;
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    /**
     * 设置自动补全
     */
    @Deprecated
    public void setAutoComplete(Context context, String[] list) {
        this.list = list;
        ArrayAdapter<String> adapter = new FilterAdapter<String>(context, Arrays.asList(list));
//        Class clazz = adapter.getClass();
//        try {
//            Field mFilter = clazz.getDeclaredField("mFilter");
//            mFilter.set(adapter, new MyFilter());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        editText.setAdapter(adapter);
    }

    @Deprecated
    public void setAutoComplete(Context context, ArrayList<?> arrayList) {
        this.list = (String[]) arrayList.toArray(new String[0]);
        ArrayAdapter<?> adapter = new FilterAdapter<>(context, arrayList);
//        Class clazz = adapter.getClass();
//        try {
//            Field mFilter = clazz.getDeclaredField("mFilter");
//            mFilter.set(adapter, new MyFilter());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        editText.setAdapter(adapter);
    }

    public void setAutoComplete(ArrayList<?> arrayList) {
        this.list = (String[]) arrayList.toArray(new String[0]);
        ArrayAdapter<?> adapter = new FilterAdapter<>(getContext(), arrayList);
//        Class clazz = adapter.getClass();
//        try {
//            Field mFilter = clazz.getDeclaredField("mFilter");
//            mFilter.set(adapter, new MyFilter());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        editText.setAdapter(adapter);
    }

    public void cleanAutoComplete() {
        editText.setAdapter(null);
    }

    public void setScanner_edit_text(String text) {
        editText.setText(text);
    }

    public String getScanner_edit_text() {
        return editText.getText().toString();
    }

    public void setEditTextAttrChanged(final InverseBindingListener listener) {
        if (listener != null) {
            editText.setOnFocusChangeListener((view, b) -> listener.onChange());
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    listener.onChange();
//                }
//            });
        }
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<AutoCompleteTextView> cls = AutoCompleteTextView.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }

    public class FilterAdapter<T> extends ArrayAdapter<T> implements Filterable {

        private List<T> fData;//过路器（MyFilter）数据源
        LayoutInflater con;
        private List<T> data;//作为参数的数据源
        private final Object mLock = new Object();
        private MyFilter mFilter = null;

        public FilterAdapter(Context con, List<T> data) {
            super(con, R.layout.autocomplete_item, R.id.text1, data);
            this.con = LayoutInflater.from(con);
            mFilter = new MyFilter();
            this.data = new ArrayList<T>(data);
        }

        public int getCount() {
            return data.size();
        }

        public T getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public View getView(int peisition, View conview, ViewGroup arg2) {
            View view = null;
            ViewHoder vh = null;
            if (conview == null) {
                vh = new ViewHoder();

                view = con.inflate(R.layout.autocomplete_item, null);
                vh.te = (TextView) view.findViewById(R.id.text1);
                view.setTag(vh);

            } else {

                view = conview;
                vh = (ViewHoder) view.getTag();
            }
            vh.te.setText((CharSequence) data.get(peisition));

            return view;
        }

        class ViewHoder {
            TextView te;
        }

        @Override
        public Filter getFilter() {

            return mFilter;
        }

        private class MyFilter extends Filter {

            /**
             * 过滤autoCompleteEditext中的字 改成包含
             */
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if (fData == null) {
                    synchronized (mLock) {
                        fData = new ArrayList<T>(data);
                    }
                }
                int count = fData.size();
                ArrayList<T> values = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    T value = fData.get(i);
                    String valueText = value.toString();
                    if (null != valueText && null != constraint
                            && valueText.contains(constraint)) {
                        // Log.d("--==tag", "--==tag--" + value);
                        values.add(value);
                    }
                }
                results.values = values;
                results.count = values.size();
                return results;
            }

            /**
             * 在FilterResults方法中把过滤好的数据传入此方法中 results过滤好的数据源重新给data赋值显示新的适配内容
             * 并刷新适配器
             */
            @Override
            protected void publishResults(CharSequence arg0, FilterResults results) {

                data = (List<T>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }

        }

    }

}
