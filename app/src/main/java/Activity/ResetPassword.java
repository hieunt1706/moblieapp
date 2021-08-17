package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import Model.lessionItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class ResetPassword extends AppCompatActivity {

    private EditText etPass, etToken;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        String email = (String) getIntent().getSerializableExtra("email");

        System.out.println("email");
        System.out.println(email);


        etPass = findViewById(R.id.New_Pass);
        etToken = findViewById(R.id.Token);
        btn = findViewById(R.id.btnReset);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass.getText().toString().isEmpty() || etPass.getText().toString().length() <8 ||etPass.getText().toString().length()>16){
                    etPass.setError("Từ 8 đến 16 ký tự");
                }
                else {
                    IMyService iMyService;
                    Retrofit retrofitClient = RetrofitClient.getInstance();
                    iMyService = retrofitClient.create(IMyService.class);

                    iMyService.resetPassword(email, etToken.getText().toString(), etPass.getText().toString())
                            .subscribeOn(Schedulers.io())
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
                                    Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
            }
        });

    }
}