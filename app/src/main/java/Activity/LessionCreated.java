package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tutorial_v1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.LessionAdapter;
import Model.UserAccount;
import Model.lessionItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class LessionCreated extends AppCompatActivity {

    String courseID;
    SharedPreferences sharedPreferences;
    UserAccount userAccount;
    RecyclerView rv;
    ArrayList<lessionItem> lessionItems = new ArrayList<>();
    Adapter.LessionAdapter lessionAdapter;
    RelativeLayout rl;
    ImageButton ib;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lession_list);

        courseID = (String) getIntent().getSerializableExtra("id");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));
        String img = (String) getIntent().getSerializableExtra("image");

        findViewByIds();
        Picasso.get().load(img).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv);

        lessionAdapter = new LessionAdapter(1, lessionItems, LessionCreated.this);
        rv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(lessionAdapter);
        loadLession(courseID);



        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessionCreated.this, LessionCreate.class);
                intent.putExtra("idCourse", courseID);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        lessionItems.clear();
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

                                //String id = jo.getString("idCourse");
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

                    }
                });
    }

    private void findViewByIds()
    {
        rl = findViewById(R.id.relative);
        rv = findViewById(R.id.rvLession);
        ib = findViewById(R.id.create_lession);
        iv = findViewById(R.id.imageLession);

        ib.setVisibility(View.VISIBLE);
        rl.setVisibility(View.GONE);

    }
}