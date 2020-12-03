package p.gordenyou.pdalibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import p.gordenyou.pdalibrary.R;
import p.gordenyou.pdalibrary.unity.FunList;
import p.gordenyou.pdalibrary.view.GridView;

/**
 * Created by GORDENyou on 2020/2/1.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private Context context;
    private ArrayList<FunList> list_function;

    private GridView.OnGridItemClickListener clickListener;

    static class GridViewHolder extends RecyclerView.ViewHolder{
        TextView item_text;
        ImageView item_image;

        GridViewHolder(View itemView) {
            super(itemView);
            item_text = itemView.findViewById(R.id.item_text);
            item_image = itemView.findViewById(R.id.item_image);
        }
    }

    public GridAdapter(Context context, ArrayList<FunList> list_function){
        this.context = context;
        this.list_function = list_function;
    }

    public void setList_function(ArrayList<FunList> list_function){
        this.list_function = list_function;
    }

    public void setClickListener(GridView.OnGridItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.view_grid_item, parent, false);
        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.item_text.setText(list_function.get(position).GetFunName());
        holder.item_image.setImageResource(list_function.get(position).GetImagetId());

        View itemView = holder.itemView;

        if(clickListener != null){
            itemView.setOnClickListener(view -> clickListener.onItemClick(holder.item_text.getText().toString()));
        }
    }

    @Override
    public int getItemCount() {
        return list_function.size();
    }

}
