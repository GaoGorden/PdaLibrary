package p.gordenyou.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import p.gordenyou.pdalibrary.R;


/**
 * Created by Gordenyou on 2018/3/2.
 */

public class SpinnerView extends LinearLayout {

    TextView tv_title;
    Spinner spinner;


    public SpinnerView(Context context) {
        this(context, null);
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_spinner, this);
        tv_title = findViewById(R.id.spinner_title);
        spinner = findViewById(R.id.spinner_spinner);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView);
        tv_title.setText(a.getText(R.styleable.SpinnerView_spinnertitle));

        a.recycle();
    }


    public void setSpinnerList(Context context, String[] list) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(context, R.layout.spinner_style, list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        spinner.setAdapter(spinnerAdapter);

        //设置spinner的默认值：
        spinner.setSelection(0);
    }

    public void setSpinnerList(Context context, int id) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(context, R.layout.spinner_style, context.getResources().getStringArray(id));
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setDefault(String str, int id) {
        String[] list = this.getResources().getStringArray(id);
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(str)) {
                spinner.setSelection(i);
            }
        }

    }

    public Spinner getSpinner() {
        return spinner;
    }

    public String getID(){
        if(spinner.getSelectedItemId() != -1){
            return spinner.getSelectedItem().toString().split("_")[0];
        }else{
            return "";
        }
    }

    public String getName(){
        if(spinner.getSelectedItemId() != -1){
            return spinner.getSelectedItem().toString().split("_")[1];
        }else{
            return "";
        }
    }
}
