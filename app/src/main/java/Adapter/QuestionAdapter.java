package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Activity.Question;
import Activity.streamVideo;
import Model.QuestionItem;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.CustomViewHolder> {
    ArrayList<QuestionItem> items;
    Context context;
    QuestionItem question;
    JSONArray resultQuestion=new JSONArray();
    SharedPreferences sharedPreferences;

    public QuestionAdapter(ArrayList<QuestionItem> items, Context context)
    {
        this.context = context;
        this.items = items;


    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.question_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //holder.tvquest.setText(items.get(position).get);

        holder.tvquest.setText(items.get(position).getCauhoi());
        holder.tvA.setText(items.get(position).getA());
        holder.tvB.setText(items.get(position).getB());
        holder.tvC.setText(items.get(position).getC());
        holder.tvD.setText(items.get(position).getD());
        try {
            Picasso.get().load(items.get(position).getImageQues()).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.iv);
        } catch (Exception e){}
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        holder.tvA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(holder.tvA.isChecked())
                {
                    String stringFromJSONArray=resultQuestion.toString();
                    JSONObject jo = new JSONObject();
                    if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                    { System.out.println("1");
                        for(int i = 0;i < resultQuestion.length();i++)
                        {
                            if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                            {
                                resultQuestion.remove(i);
                            }
                        }
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        System.out.println("2");
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    unCheck(holder.tvB, holder.tvC, holder.tvD);

                    resultQuestion.put(jo);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("resultQuestion", resultQuestion.toString());
                    editor.apply();

                }
            }
        });

        holder.tvB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.tvB.isChecked()) {
                    String stringFromJSONArray=resultQuestion.toString();
                    JSONObject jo = new JSONObject();
                    if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                    {
                        for(int i = 0;i < resultQuestion.length();i++)
                        {
                            if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                            {
                                resultQuestion.remove(i);
                            }
                        }
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","B");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","B");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    resultQuestion.put(jo);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("resultQuestion", resultQuestion.toString());
                    editor.apply();
                    unCheck(holder.tvA, holder.tvC, holder.tvD);}
            }
        });

        holder.tvC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.tvC.isChecked())
                {
                    String stringFromJSONArray=resultQuestion.toString();
                    JSONObject jo = new JSONObject();
                    if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                    {
                        for(int i = 0;i < resultQuestion.length();i++)
                        {
                            if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                            {
                                resultQuestion.remove(i);
                            }
                        }
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    resultQuestion.put(jo);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("resultQuestion", resultQuestion.toString());
                    editor.apply();
                    unCheck(holder.tvA, holder.tvB, holder.tvD);

                }
            }
        });

        holder.tvD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.tvD.isChecked())

                {  String stringFromJSONArray=resultQuestion.toString();
                    JSONObject jo = new JSONObject();
                    if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                    {
                        for(int i = 0;i < resultQuestion.length();i++)
                        {
                            if(stringFromJSONArray.contains(items.get(position).get_idMultichoice()))
                            {
                                resultQuestion.remove(i);
                            }
                        }
                        try {
                            jo.put("answer","D");
                            jo.put("_id",items.get(position).get_idMultichoice());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            jo.put("_id",items.get(position).get_idMultichoice());
                            jo.put("answer","D");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    resultQuestion.put(jo);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("resultQuestion", resultQuestion.toString());
                    editor.apply();
                    unCheck(holder.tvB, holder.tvC, holder.tvA);
                }

            }
        });
    }

    private void unCheck(CheckBox a, CheckBox b , CheckBox c)
    {
        a.setChecked(false);
        b.setChecked(false);
        c.setChecked(false);
    }

    private void Quit()
    {
        Intent intent = new Intent(context, streamVideo.class);
        context.startActivity(intent);
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
        TextView tvquest;
        CheckBox tvA, tvB, tvC, tvD;
        ImageView iv;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.question);
            tvA = itemView.findViewById(R.id.a);
            tvB = itemView.findViewById(R.id.b);
            tvC = itemView.findViewById(R.id.c);
            tvD = itemView.findViewById(R.id.d);
            iv = itemView.findViewById(R.id.image_question);
        }
    }
}
