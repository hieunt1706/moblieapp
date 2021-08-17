package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import Adapter.LessionAdapter;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.courseAdapter;
import Model.UserAccount;
import Model.courseItem;
import Model.lessionItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.Retrofit;
import Retrofit.*;

public class LessionList extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    UserAccount userAccount;
    RecyclerView rv;
    ArrayList<lessionItem> lessionItems = new ArrayList<>();
    Adapter.LessionAdapter lessionAdapter;
    ImageView imageView;
    ProgressBar pb;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lession_list);

        String courseID = (String) getIntent().getSerializableExtra("id");
        String img = (String) getIntent().getSerializableExtra("image");
        int percent = (int) getIntent().getSerializableExtra("percent");


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));

        pb = findViewById(R.id.pb_percent);
        tv = findViewById(R.id.tv_percent);
        pb.setProgress(percent);
        tv.setText("" + percent + "%");

        rv = findViewById(R.id.rvLession);
        imageView = findViewById(R.id.imageLession);
        Picasso.get().load(img).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        lessionAdapter = new LessionAdapter(0, lessionItems, LessionList.this);

        rv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(lessionAdapter);

        loadLession(courseID);
    }

    private void loadLession(String id)
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);

        iMyService.getLessionByID("http://149.28.24.98:9000/lesson/get-lesson-by-id-course/" + id, userAccount.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try
                        {
                            JSONArray ja = new JSONArray(s);
                            for(int i = 0; i < ja.length(); i++)
                            {
                                JSONObject jo = ja.getJSONObject(i);
                                lessionItems.add(new lessionItem(jo.getString("title"), jo.getString("order"), jo.getString("video"), jo.getString("_id"), jo.getString("doc"), jo.getString("idCourse")));
                                lessionAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e)
                        {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete() {
                        if  (lessionItems.isEmpty()){
                            Toast.makeText(LessionList.this, "Không có bài giảng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}