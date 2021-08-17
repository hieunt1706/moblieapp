package Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.tutorial_v1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import Model.MultiChoice;
import Model.UserAccount;
import dmax.dialog.SpotsDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Retrofit;
import Retrofit.IMyService;
import Retrofit.RetrofitClient;

public class LessionCreate extends AppCompatActivity {

    TextView nameD, nameV;
    Button video, doc, create;
    File fVideo, fDoc;
    SharedPreferences sharedPreferences;
    UserAccount userAccount;
    Toolbar userAvaTB;
    String name, stt, courseID, ok;
    EditText etName, etStt;
    int validV = 0;
    int validD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lession_create);

        courseID = (String) getIntent().getSerializableExtra("idCourse");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setToken(sharedPreferences.getString("token","default"));

        findViewByIds();
        ActionToolBar();


        video.setOnClickListener(new View.OnClickListener() {
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
                        pickVideoFromGallery();
                    }
                }
                else {
                    //system os is less then marshmallow
                    pickVideoFromGallery();
                }
            }
        });

        doc.setOnClickListener(new View.OnClickListener() {
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
                        pickDocFromGallery();
                    }
                }
                else {
                    //system os is less then marshmallow
                    pickDocFromGallery();
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckValidInput())
                {
                    Create();
                }
            }
        });

    }

    private void Create()
    {
        RequestBody fileReqBodyVideo =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        fVideo
                );
        MultipartBody.Part partVideo = MultipartBody.Part.createFormData("videos", fVideo.getName(), fileReqBodyVideo);

        RequestBody fileReqBodyDoc =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        fDoc
                );
        MultipartBody.Part partDoc = MultipartBody.Part.createFormData("docs", fDoc.getName(), fileReqBodyDoc);

        AlertDialog alertDialog;
        alertDialog= new SpotsDialog.Builder().setContext(LessionCreate.this).build();
        alertDialog.show();

        MultiChoice multiChoice;
        JSONObject sendJO = new JSONObject();
        multiChoice = new MultiChoice("cau a", "cau b", "cau c", "cau d", "A", "cau joiokok", "");
        try {
            sendJO.put("A", multiChoice.GetA());
            sendJO.put("B", multiChoice.GetB());
            sendJO.put("C", multiChoice.GetC());
            sendJO.put("D", multiChoice.GetD());
            sendJO.put("answer", multiChoice.Getanswer());
            sendJO.put("question", multiChoice.Getquestion());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String temp = sendJO.toString();

        IMyService iMyService;
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        iMyService.createLession(name, stt, temp, courseID, partVideo, partDoc, userAccount.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        ok = s;
                    }

                    @Override
                    public void onError(Throwable e) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);
                        Toast.makeText(LessionCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        alertDialog.dismiss();
                                    }
                                }, 500);

                        Toast.makeText(LessionCreate.this, "Taọ bài giảng thành công", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(LessionCreate.this)
                                .setMessage("Bạn có chắc muốn tạo Muti-Choice test")
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LessionCreate.this, CreateMultiChoice.class);
                                        JSONObject jsonObject;
                                        String ok2 = "";
                                        try {
                                            jsonObject = new JSONObject(ok);
                                            ok2 = jsonObject.getString("_id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        intent.putExtra("ID", ok2);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog1.show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(LessionCreate.this)
                .setMessage("Bạn có chắc muốn tạo Muti-Choice test")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LessionCreate.this, UserInfoActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private boolean CheckValidInput()
    {
        Boolean valid=true;
        name = etName.getText().toString();
        stt = etStt.getText().toString();
        if(name.isEmpty())
        {
            etName.setError("Tên bài giảng không được để trống");
            valid = false;
        } else {
            etName.setError(null);
        }
        if(stt.isEmpty())
        {
            etStt.setError("Số thứ tự không được để trống");
            valid = false;
        } else {
            etStt.setError(null);
        }
        if(validV == 0)
        {
            Toast.makeText(LessionCreate.this, "Chưa chọn file video", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(validD == 0)
        {
            Toast.makeText(LessionCreate.this, "Chưa chọn file doc", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void findViewByIds()
    {
        userAvaTB = findViewById(R.id.LessionTB);
        nameD = findViewById(R.id.name_doc);
        nameV = findViewById(R.id.name_video);
        video = findViewById(R.id.fvideo);
        doc = findViewById(R.id.fDoc);
        create = findViewById(R.id.createLession);
        etName = findViewById(R.id.TenBaiGiang);
        etStt = findViewById(R.id.stt);
    }

    private void pickVideoFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "video"), 10);
    }

    private void pickDocFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Secsajdasd"), 12);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1000:{


                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickVideoFromGallery();
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
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri path = data.getData();
            fVideo = new File(getPathFromUri(this,path));
            validV = 1;

            String filename = fVideo.getName();
            nameV.setVisibility(View.VISIBLE);
            nameV.setText(filename);

        }

        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri path = data.getData();
            fDoc = new File(getPathFromUri(this, path));
            validD = 1;

            String filename = fDoc.getName();
            nameD.setVisibility(View.VISIBLE);
            nameD.setText(filename);
        }

    }

    private String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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