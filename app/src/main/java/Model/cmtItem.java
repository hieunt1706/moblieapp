package Model;

import android.view.View;

import java.io.Serializable;

import Adapter.commentAdapter;

public class cmtItem implements Serializable {
    private String idcmt;
    private String createat;
    private String content;
    private String name;
    private String image;


    public String getIdcmt() {return idcmt; }
    public String getCreateat() {return createat; }
    public String getContentcmt() {return content; }
    public String getName() { return name; }
    public String getImage() {return image; }

    public cmtItem(String idcmt, String createat, String content, String name, String image)
    {
        this.idcmt = idcmt;
        this.createat = createat;
        this.content = content;
        this.image = image;
        this.name = name;
    }


}
