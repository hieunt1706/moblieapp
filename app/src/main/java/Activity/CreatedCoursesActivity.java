package Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.CartCourseAdapter;
import Adapter.CreatedCourseAdapter;
import Adapter.courseAdapter;
import Model.UserAccount;
import Model.courseItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class CreatedCoursesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAccount userAccount;
    SharedPreferences sharedPreferences;
    ImageButton create;
    ArrayList<courseItem> courseItems = new ArrayList<>();
    Adapter.CreatedCourseAdapter createdCourseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_created);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setID(sharedPreferences.getString("id","default"));
        userAccount.setToken(sharedPreferences.getString("token", "default"));


        createdCourseAdapter = new CreatedCourseAdapter(userAccount.getToken(), courseItems, CreatedCoursesActivity.this);
        createdCourseAdapter.setHasStableIds(true);


        recyclerView = findViewById(R.id.my_created_course);

        recyclerView.setLayoutManager(new GridLayoutManager(CreatedCoursesActivity.this, 1, GridLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(createdCourseAdapter);



        create = findViewById(R.id.create_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatedCoursesActivity.this, TestCreateCourse.class);
                startActivity(intent);
            }
        });
        loadCourses();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        courseItems.clear();
        loadCourses();
    }

    private void loadCourses()
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);

        System.out.println("ID: " );
        System.out.println(userAccount.getID());

        iMyService.getCourseCreated("http://149.28.24.98:9000/course/getby-iduser/" + userAccount.getID()).
                subscribeOn(Schedulers.io())
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
                                courseItem courseItem = new courseItem("http://149.28.24.98:9000/upload/course_image/" + jo.getString("image"),jo.getString("name"), "",jo.getString("_id"),
                                        Float.valueOf(jo.getString("price")),Float.valueOf(jo.getString("discount")));
                                courseItem.setCategoryID(jo.getString("category"));
                                courseItem.setGoal(jo.getString("goal"));
                                courseItem.setDesription(jo.getString("description"));
                                courseItems.add(courseItem);
                                createdCourseAdapter.notifyDataSetChanged();
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CreatedCoursesActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}