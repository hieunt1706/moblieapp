package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tutorial_v1.R;

import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class ForgotPassword extends AppCompatActivity {

    EditText editText;
    Button btn;
    Toolbar userAvaTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userAvaTB=findViewById(R.id.ForgotTB);
        editText = findViewById(R.id.EmailEditText);
        btn = findViewById(R.id.btnForgot);

        ActionToolBar();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    editText.setError("Không được bỏ trống");
                } else {

                    IMyService iMyService;
                    Retrofit retrofitClient = RetrofitClient.getInstance();
                    iMyService = retrofitClient.create(IMyService.class);

                    iMyService.ForgotPassword(editText.getText().toString())
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
                                    Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
                                    intent.putExtra("email", editText.getText().toString());
                                    startActivity(intent);
                                }
                            });
                }
            }
        });

    }



    private void ActionToolBar() {
        setSupportActionBar(userAvaTB);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userAvaTB.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userAvaTB.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}