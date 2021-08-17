package Model;

import java.io.Serializable;

public class QuestionItem implements Serializable {
    private String Cauhoi;
    private String A;
    private String B;
    private String C;
    private String D;
    private String Dapan;
    private String image;
    private String Choice;
    private String _idMultichoice;

    public QuestionItem() {

    }

    public String getCauhoi() {return Cauhoi; }
    public String getA() {return A; }
    public String getB() {return B; }
    public String getC() {return C; }
    public String getD() {return D; }
    public String get_idMultichoice(){return _idMultichoice;}
    public String getDapan() {return  Dapan; }
    public String getImageQues() {return image; }
    public  void set_idMultichoice(String idMultichoice)
    {
        this._idMultichoice = idMultichoice;
    }
    public void setChoice(String choice)
    {
        this.Choice = choice;
    }
    public  String getChoice(){
        return Choice;
    }
    public QuestionItem(String image, String Cauhoi, String A, String B, String C, String D, String Dapan,String idMultichoice)
    {
        this.image = image;
        this.Cauhoi = Cauhoi;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.Dapan = Dapan;
        this._idMultichoice = idMultichoice;
    }
}
