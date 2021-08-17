package Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_v1.R;

import Adapter.commentAdapter;
import Model.UserAccount;
import Model.cmtItem;
import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import Retrofit.*;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Comment extends AppCompatActivity {

    Toolbar userInfoTB;
    RecyclerView rv;
    EditText et;
    ImageView iv;
    Adapter.commentAdapter commentAdapter;
    ArrayList<cmtItem> cmtItems = new ArrayList<>();
    ImageButton ib, send;
    String Id, idCourse;
    File file;
    SharedPreferences sharedPreferences;
    UserAccount userAccount;
    TextView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Id = (String) getIntent().getSerializableExtra("id");
        idCourse = (String) getIntent().getSerializableExtra("idCourse");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setID(sharedPreferences.getString("id","default"));

        findViewByIds();
        ActionToolBar();
        commentAdapter = new commentAdapter(cmtItems, Comment.this);
        rv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(commentAdapter);

        loadComment();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().isEmpty())
                {
                    Toast.makeText(Comment.this, "Phản hồi không được để trống", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Cmt();
                }
            }
        });

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, 1000);
                    }
                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else {
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }
            }
        });

    }

    private void ActionToolBar() {
        setSupportActionBar(userInfoTB);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userInfoTB.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userInfoTB.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1000:{


                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
            case 100:{
                //  Toast.makeText(this, "asd: "+ PackageManager.PERMISSION_GRANTED, Toast.LENGTH_SHORT).show();
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1001);
                }
                else
                {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1000 && data.getData() != null) {
            //set image to image view

            Uri path = data.getData();
            try {
                file = new File(getRealPathFromURI(path));
                img.setVisibility(View.VISIBLE);
                img.setText(getRealPathFromURI(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void Cmt()
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        iMyService.addComment(null, null, idCourse, et.getText().toString(), userAccount.getID(), Id).
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
                        img.setVisibility(View.GONE);
                        et.getText().clear();
                        closeKeyboard();
                        cmtItems.clear();
                        loadComment();
                    }
                });
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if(view !=null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void findViewByIds()
    {
        rv = findViewById(R.id.rvCmt);
        et = findViewById(R.id.etCmt);
        iv = findViewById(R.id.ivCmt);
        ib = findViewById(R.id.add_img);
        send = findViewById(R.id.sendCmt);
        img = findViewById(R.id.tv_img);
        userInfoTB = findViewById(R.id.tbCmt);
    }


    private void loadComment()
    {
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);

        iMyService.getCommentLession("  http://149.28.24.98:9000/comment/get-parent-comment-by-lesson/" + idCourse +"/" + Id + "/8/0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        String name, image;
                        try {
                            JSONArray ja = new JSONArray(s);
                            for(int i = 0; i < ja.length(); i++)
                            {
                                JSONObject jo = ja.getJSONObject(i);
                                String user = jo.getString("idUser");

                                JSONObject jo2 = new JSONObject(user);
                                image = "http://149.28.24.98:9000/upload/course_image/" + jo2.getString("image");
                                name = jo2.getString("name");

                                cmtItems.add(new cmtItem(jo.getString("_id"), jo.getString("created_at"), jo.getString("content"), name, image));
                                commentAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Comment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}