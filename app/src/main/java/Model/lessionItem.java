package Model;

import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONObject;

import java.io.Serializable;

public class lessionItem implements Serializable {
    private String name;
    private String num;
    private String video;
    private String Id;
    private String Doc;
    private String IdCourse;

    public String GetName() {return name;}
    public String GetNum() {return num;}
    public String GetVideo() {return video;}
    public String GetId() {return Id;}
    public String GetDoc() {return Doc;}
    public String GetIdCouse() {return IdCourse;}

    public void SetName(String name) {this.name = name;}
    public void SetNum(String num) {this.num = num;}
    public void SetVideo(String video) {this.video = video;}

    public lessionItem(String name, String num, String video, String Id, String Doc, String IdCourse)
    {
        this.name = name;
        this.num = num;
        this.video = video;
        this.Id = Id;
        this.Doc = Doc;
        this.IdCourse = IdCourse;
    }

}
