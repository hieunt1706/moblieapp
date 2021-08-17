package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import Activity.LessionCreate;
import Activity.UpdateLession;
import Activity.streamVideo;

import Model.lessionItem;

import java.util.ArrayList;

public class LessionAdapter extends RecyclerView.Adapter<LessionAdapter.CustomViewHolder> {

    private ArrayList<lessionItem> items;
    private Context context;
    private int a;

    public LessionAdapter(int a, ArrayList<lessionItem> items, Context context) {
        this.items = items;
        this.context = context;
        this.a = a;
    }
    @NonNull
    @Override
    public LessionAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessionAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.lession_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LessionAdapter.CustomViewHolder holder, int position) {
        holder.tvNum.setText(items.get(position).GetNum());
        holder.tvName.setText(items.get(position).GetName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
       TextView tvNum, tvName;
       ImageButton ibLession;
       ImageView iv;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNum = itemView.findViewById(R.id.numLession);
            tvName = itemView.findViewById(R.id.TenBaiGiang);
            ibLession = itemView.findViewById(R.id.btnLession);
            iv = itemView.findViewById(R.id.updateLession);

            if(a == 1) { iv.setVisibility(View.VISIBLE); }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateLession.class);
                    intent.putExtra("lession",items.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            ibLession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, streamVideo.class);
                    intent.putExtra("lession",items.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
