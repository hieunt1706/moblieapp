package Model;

import java.io.Serializable;

public class MultiChoice implements Serializable {
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
    private String question;
    private String image;

    public void SetA(String A) {this.A = A; }
    public void SetB(String B) {this.B = B; }
    public void SetC(String C) {this.C = C; }
    public void SetD(String D) {this.D = D; }
    public void Setanswer(String answer) {this.answer = answer; }
    public void Setquestion(String question) {this.question = question; }
    public void Setimage(String image) {this.image = image; }

    public String GetA() {return A; }
    public String GetB() {return B; }
    public String GetC() {return C; }
    public String GetD() {return D; }
    public String Getanswer() {return answer; }
    public String Getquestion() {return question; }
    public String Getimage() {return image; }

    public MultiChoice(String A, String B, String C, String D, String answer, String question, String image)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.answer = answer;
        this.question = question;
        this.image = image;
    }
}
