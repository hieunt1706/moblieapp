package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import Model.cmtItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<cmtItem> items;

    public commentAdapter(ArrayList<cmtItem> items, Context context)
    {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new commentAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.cmt_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getName());
        holder.tvContent.setText(items.get(position).getContentcmt());
        holder.tvCreate.setText(items.get(position).getCreateat());
        Picasso.get().load(items.get(position).getImage()).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.iv);


    }

    @Override
    public int getItemCount() {return items.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv, img;
        TextView tvName, tvContent, tvCreate;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivCmt);
            tvName = itemView.findViewById(R.id.nameCmt);
            tvContent = itemView.findViewById(R.id.contentCmt);
            tvCreate = itemView.findViewById(R.id.createCmt);
            img = itemView.findViewById(R.id.img_cmt);
        }
    }
}
