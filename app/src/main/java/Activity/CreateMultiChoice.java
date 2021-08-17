package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import org.json.JSONException;
import org.json.JSONObject;

import Model.UserAccount;
import Model.lessionItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import Retrofit.*;

public class CreateMultiChoice extends AppCompatActivity {

    EditText question, A, B, C, D, Answer;
    Button btn;
    String ques, a, b, c, d, answer, Id;
    JSONObject sendJO = new JSONObject();
    JSONObject jo = new JSONObject();
    SharedPreferences sharedPreferences;
    UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_multi_choice);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));
        Id = (String) getIntent().getSerializableExtra("ID");

        Toast.makeText(CreateMultiChoice.this, Id, Toast.LENGTH_SHORT).show();

        findViewByIds();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckValidInput()){
                    Update();
                }
            }
        });
    }

    private void Update()
    {
        System.out.println("ok");
        System.out.println(ques);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(answer);

        try {
            sendJO.put("A", a);
            sendJO.put("B", b);
            sendJO.put("C", c);
            sendJO.put("D", d);
            sendJO.put("answer", answer);
            sendJO.put("question", ques);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jo.put("multipleChoices", sendJO);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jo.toString());
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        iMyService.updateMultipleChoice("http://149.28.24.98:9000/lesson/add-list-multiple-choice/" + Id, body, userAccount.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("rep");
                        System.out.println(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(CreateMultiChoice.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void setData()
    {

    }

    private Boolean CheckValidInput()
    {
        ques = question.getText().toString();
        a = A.getText().toString();
        b = B.getText().toString();
        c = C.getText().toString();
        d = D.getText().toString();
        answer = Answer.getText().toString();
        return true;
    }

    private void findViewByIds()
    {
        question = findViewById(R.id.CauHoi);
        A = findViewById(R.id.Caua);
        B = findViewById(R.id.Caub);
        C = findViewById(R.id.Cauc);
        D = findViewById(R.id.CauD);
        Answer = findViewById(R.id.answer);
        btn = findViewById(R.id.createMTC);
    }
}