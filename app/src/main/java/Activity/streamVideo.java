package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tutorial_v1.R;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;

import Model.lessionItem;

public class streamVideo extends AppCompatActivity {

    VideoView vv;
    TextView tvDoc, tvTitle;
    Button btnback, btnQuest, btnHD;
    lessionItem lessionItems;
    MediaController mediaController;
    String url = "http://149.28.24.98:9000/upload/lesson/";
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoc);


        lessionItems = (lessionItem) getIntent().getSerializableExtra("lession");
        System.out.println(lessionItems.GetId());
        findViewByIds();
        setTexts();
        StreamVideo();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(streamVideo.this, Question.class);
                intent.putExtra("id",lessionItems.GetId());
                startActivity(intent);
            }
        });

        btnHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(streamVideo.this, Comment.class);
                intent.putExtra("id",lessionItems.GetId());
                intent.putExtra("idCourse", lessionItems.GetIdCouse());
                startActivity(intent);
            }
        });
    }

    private void StreamVideo()
    {
        mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);

        uri = Uri.parse(url + lessionItems.GetVideo());

        vv.setMediaController(mediaController);
        vv.setVideoURI(uri);
        vv.start();
    }

    private void setTexts()
    {
        String doc = lessionItems.GetDoc();
        doc = doc.replace("[", "").replace("]", "");
        tvTitle.setText(lessionItems.GetName());
        tvDoc.setText(doc);
    }

    private void findViewByIds()
    {
        tvDoc = findViewById(R.id.Document);
        tvTitle = findViewById(R.id.Lession);
        btnback = findViewById(R.id.back);
        btnQuest = findViewById(R.id.btnQuest);
        btnHD = findViewById(R.id.hoidap);
        vv = findViewById(R.id.vv_lession);
    }
}