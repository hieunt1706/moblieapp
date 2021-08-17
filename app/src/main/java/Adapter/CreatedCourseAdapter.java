package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import Activity.LessionCreated;
import Activity.UpdateCourse;
import Model.courseItem;

import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class CreatedCourseAdapter extends RecyclerView.Adapter<CreatedCourseAdapter.CustomViewHolder> {
    private ArrayList<courseItem> items;
    private Context context;
    private String token;

    public CreatedCourseAdapter(String token, ArrayList<courseItem> items, Context context)
    {
        this.items = items;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreatedCourseAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_course_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        Picasso.get().load(items.get(position).getUrl()).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView);
        NumberFormat formatter = new DecimalFormat("#,###");
        double price=(double)items.get(position).getPrice();
        String formattedNumber1 = formatter.format(price);
        if(formattedNumber1.equals("0")) holder.price.setText("Miễn phí");
        else
            holder.price.setText(formattedNumber1+" đ");
        if(items.get(position).getDiscount()!=0)
        {
            holder.discount.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            price=price-(price*items.get(position).getDiscount())/100;
            String formattedNumber2 = formatter.format(price);
            holder.price.setText(formattedNumber2+" đ");
            holder.discount.setText(formattedNumber1);
        }
        else holder.discount.setVisibility(View.GONE);
        holder.deleteFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog= new AlertDialog.Builder(context)
                        .setTitle("Xóa khóa học")
                        .setMessage("Bạn có chắc muốn xóa khóa học này")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Remove(position);
                            }
                        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateCourse.class);
                intent.putExtra("course", items.get(position));
                context.startActivity(intent);
            }
        });


    }

    private void Remove(int p)
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        iMyService.DeleteCourse("http://149.28.24.98:9000/course/delete/" + items.get(p).getID(), token).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        items.remove(p);
                        notifyItemRemoved(p);
                        notifyItemRangeChanged(p, items.size());
                    }
                });

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
        private TextView title, price, discount, author;
        private ImageView imageView, deleteFromCart, update;

        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.cartCourseName);

            imageView = view.findViewById(R.id.cartCourseImage);
            price = view.findViewById(R.id.cartCoursePrice);
            discount = view.findViewById(R.id.cartCourseDiscount);
            deleteFromCart = view.findViewById(R.id.deleteFromCart);
            author=view.findViewById(R.id.cartCourseAuthor);
            update = view.findViewById(R.id.update);
            update.setVisibility(View.VISIBLE);
            author.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LessionCreated.class);
                    intent.putExtra("id", items.get(getAdapterPosition()).getID());
                    intent.putExtra("image", items.get(getAdapterPosition()).getUrl());
                    context.startActivity(intent);
                }
            });
        }
    }

}
