package Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Model.UserAccount;
import Model.courseItem;
import Retrofit.IMyService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import Retrofit.IMyService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import Retrofit.*;

public class UpdateCourse extends AppCompatActivity {

    UserAccount userAccount;
    SharedPreferences sharedPreferences;
    Button ChonAnh,Create;
    File file;
    Bitmap bitmap;
    ImageView imageView;
    String name, goal, des, price, dis;
    TextInputEditText etName, etGoal, etDes, etPrice, etDiscount;
    courseItem CourseItem;
    Spinner spinner;
    TextView tvLinhvuc, tvTitle;
    String category = null;
    JSONObject jo = null;
    int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        CourseItem = (courseItem) getIntent().getSerializableExtra("course");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));

        String a = CourseItem.getCategoryID();

        try {
            jo = new JSONObject(a);
            category = jo.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        sp();
        try {
            loadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ChonAnh = findViewById(R.id.ThuVienAnh);
        Create = findViewById(R.id.create);
        Create.setText("Cap Nhat");
        ChonAnh.setOnClickListener(new View.OnClickListener() {
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

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckValueInput())
                {
                    updateCourse();
                }

                //changeAvatar();
            }
        });

        //updateCourse();
    }

    private void sp()
    {
        tvLinhvuc = findViewById(R.id.linhvuc);
        spinner = findViewById(R.id.Linhvuc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(category.equals("5f66f8bc877b74b5e133db8c"))
        {
            spinner.setSelection(0);
        }
        else if(category.equals("5f66f8e0877b74b5e133db8d"))
        {
            spinner.setSelection(1);
        }
        else if(category.equals("5fa4ac6fb4e3807bf40fed22"))
        {
            spinner.setSelection(2);
        }
        //spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setText(text);
                ((TextView) parent.getChildAt(0)).setTextAppearance(android.R.style.TextAppearance_Material_Widget_Toolbar_Title);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(21);

                if(text.equals("Toán Tin"))
                {
                    category = "5f66f8bc877b74b5e133db8c";
                }
                if(text.equals("Công Nghệ Thông Tin"))
                {
                    category = "5f66f8e0877b74b5e133db8d";
                }
                if(text.equals("Ngoại Ngữ"))
                {
                    category = "5fa4ac6fb4e3807bf40fed22";
                }
                tvLinhvuc.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UpdateCourse.this, "Vui long chon linh vuc", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadData() throws MalformedURLException {
        etName = findViewById(R.id.TenKhoaHoc);
        etGoal = findViewById(R.id.MucTieu);
        etDes = findViewById(R.id.MoTa);
        etPrice = findViewById(R.id.Giaban);
        etDiscount = findViewById(R.id.Giamgia);
        imageView = findViewById(R.id.image_course);
        tvTitle = findViewById(R.id.regText);

        tvTitle.setText("Cap Nhat Khoa Hoc");
        etName.setText(CourseItem.getTitle());
        etGoal.setText(CourseItem.getGoal());
        etDes.setText(CourseItem.getDesription());
        etPrice.setText(String.format("%.0f", CourseItem.getPrice()));
        //String.format("%.0f", d)
        etDiscount.setText(String.valueOf(CourseItem.getDiscount()));


        Picasso.get()
                .load(CourseItem.getUrl())
                .placeholder(R.drawable.empty23)
                .error(R.drawable.empty23)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
        //FileUtils.copy(CourseItem.getUrl(), file);
        URL url = new URL(CourseItem.getUrl());
        file = new File(url.getFile());
    }

    private boolean CheckValueInput()
    {
        name = etName.getText().toString();
        goal = etGoal.getText().toString();
        des = etDes.getText().toString();
        price = etPrice.getText().toString();
        dis = etDiscount.getText().toString();

        if(name.isEmpty()) {return false; }
        if(goal.isEmpty()) {return false; }
        if(des.isEmpty()) {return false; }
        if(price.isEmpty()) {return false; }
        if(dis.isEmpty()) {return false; }
        return true;
    }

    private void updateCourse()
    {


        RequestBody fileReqBody =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        if (a == 1) {part = null; };
        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);

        iMyService.updateCourse("http://149.28.24.98:9000/course/update/" + CourseItem.getID(), name, goal, des, category, price, dis, part, userAccount.getToken())
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

                    }

                    @Override
                    public void onComplete() {
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

        if (resultCode == RESULT_OK && requestCode == 1000 && data.getData() != null){
            //set image to image view

            Uri path=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                a = 2;
                file = new File(getRealPathFromURI(path));

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        else if(resultCode == RESULT_OK && requestCode == 1001)
        {
            bitmap= (Bitmap) data.getExtras().get("data");

            Uri path = getImageUri(UpdateCourse.this, bitmap);

            file = new File(getRealPathFromURI(path));


        }
    }
    /// pick galeery

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}