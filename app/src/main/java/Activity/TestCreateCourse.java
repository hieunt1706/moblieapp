package Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
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

import org.bouncycastle.util.test.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import Model.UserAccount;
import Retrofit.IMyService;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import Retrofit.RetrofitClient;
import retrofit2.Response;
import retrofit2.Retrofit;
public class TestCreateCourse extends AppCompatActivity {
    UserAccount userAccount;
    SharedPreferences sharedPreferences;
    IMyService iMyService;
    Button ChonAnh,Create;
    File file;
    Bitmap bitmap;
    ImageView imageView;
    String name, goal, des, price, dis;
    TextInputEditText etName, etGoal, etDes, etPrice, etDiscount;
    Spinner spinner;
    String category = "1";
    TextView tvLinhvuc;
    boolean checkbtn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));

        etName = findViewById(R.id.TenKhoaHoc);
        etGoal = findViewById(R.id.MucTieu);
        etDes = findViewById(R.id.MoTa);
        etPrice = findViewById(R.id.Giaban);
        etDiscount = findViewById(R.id.Giamgia);
        imageView = findViewById(R.id.image_course);

        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        ChonAnh = findViewById(R.id.ThuVienAnh);

        sp();
        Create = findViewById(R.id.create);
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
                if (CheckValueInput()) {
                    CreateCourse();
                }
                else {
                    Toast.makeText(TestCreateCourse.this, "Vui long dien day du thong tin", Toast.LENGTH_LONG).show();}
            }
        });
    }

    private boolean CheckValueInput()
    {
        name = etName.getText().toString();
        goal = etGoal.getText().toString();
        des = etDes.getText().toString();
        price = etPrice.getText().toString();
        dis = etDiscount.getText().toString();

        boolean check;
        if (name.isEmpty() || goal.isEmpty() || des.isEmpty())
        {
            return false;
        }

        try {
            Double.parseDouble(price);
            Double.parseDouble(dis);
            check = true;
        } catch(NumberFormatException e){
            Toast.makeText(TestCreateCourse.this, "Error: Gía bán và giảm giá phải là số", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!checkbtn) {
            Toast.makeText(TestCreateCourse.this, "Error: Chưa chọn thumnail", Toast.LENGTH_SHORT).show();
            return false;
        }
        return check;
    }

    private void sp()
    {
        tvLinhvuc = findViewById(R.id.linhvuc);
        spinner = findViewById(R.id.Linhvuc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                ((TextView) parent.getChildAt(0)).setText(text);
                ((TextView) parent.getChildAt(0)).setTextAppearance(android.R.style.TextAppearance_Material_Widget_Toolbar_Title);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(21);
                System.out.println("ok");
                System.out.println(text);

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
                Toast.makeText(TestCreateCourse.this, "Vui long chon linh vuc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateCourse ()
    {
        RequestBody fileReqBody =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");



        iMyService.createCourse(name,goal,des,category,price,dis,part,userAccount.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                            System.out.println("Server Tra ve");
                            System.out.println(s);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(TestCreateCourse.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(TestCreateCourse.this, "Oke", Toast.LENGTH_SHORT).show();
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
                file = new File(getRealPathFromURI(path));
                checkbtn = true;

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        else if(resultCode == RESULT_OK && requestCode == 1001)
        {
            bitmap= (Bitmap) data.getExtras().get("data");

            Uri path = getImageUri(TestCreateCourse.this, bitmap);

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
