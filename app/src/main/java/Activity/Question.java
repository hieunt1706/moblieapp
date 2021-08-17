package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.QuestionItem;
import Model.UserAccount;
import Model.lessionItem;

import Adapter.QuestionAdapter;

import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import Retrofit.*;

public class Question extends AppCompatActivity {

    String Id;
    SharedPreferences sharedPreferences;
    UserAccount userAccount;
    RecyclerView rv;
    QuestionAdapter questionAdapter;
    ArrayList<QuestionItem> questionItems = new ArrayList<>();
    Button btn, back;
    JSONArray resultQuestion=new JSONArray();
    QuestionItem question;
    JSONObject obj;
    TextView dung, sai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));

        Id = (String) getIntent().getSerializableExtra("id");

        rv = findViewById(R.id.rvQuest);
        int a = 0;
        questionAdapter = new QuestionAdapter(questionItems, Question.this);
        rv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(questionAdapter);

        loadMultiChoice();

        back = findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn = findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void loadMultiChoice()
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);

        iMyService.getChoice("http://149.28.24.98:9000/lesson/get-multiple-choice-for-test/" + Id, userAccount.getToken()).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            JSONArray ja = new JSONArray(s);
                            for (int i = 0; i < ja.length(); i++)
                            {
                                JSONObject jo = ja.getJSONObject(i);
                                String temp = jo.getString("multipleChoices");
                                JSONArray ja2 = new JSONArray(temp);

                                for(int j = 0; j < ja2.length(); j++)
                                {
                                    JSONObject jo2 = ja2.getJSONObject(i);
                                    String a = null;
                                    try {
                                        a = jo2.getString("image");
                                    }catch (Exception e){}
                                questionItems.add(new QuestionItem(a,
                                        jo2.getString("question"), jo2.getString("A"), jo2.getString("B"), jo2.getString("C"), jo2.getString("D"), "",jo2.getString("_id")));
                                questionAdapter.notifyDataSetChanged();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(Question.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                            if (questionItems.size() == 0)
                            {
                                Toast.makeText(Question.this, "Không có câu hỏi", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                    }
                });

    }


    private void Submit()
    {
        try {
            resultQuestion = new JSONArray(sharedPreferences.getString("resultQuestion",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        obj = new JSONObject();

        try {
            obj.put("idLesson", Id);
            obj.put("test", resultQuestion);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        question = new QuestionItem();


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        iMyService.submitTest(body, userAccount.getToken()).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            dung = findViewById(R.id.caudung);
                            sai = findViewById(R.id.causai);
                            dung.setVisibility(View.VISIBLE);
                            sai.setVisibility(View.VISIBLE);

                            dung.setText("Sô câu đúng: " + jo.getString("totalRight"));
                            sai.setText("Sô câu sai: " + jo.getString("totalWrong"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("okconcac");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Question.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}