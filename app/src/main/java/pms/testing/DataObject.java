package pms.testing;

import android.graphics.Bitmap;

/**
 * Created by ricar on 12/01/2017.
 */

public class DataObject {
    private String mText1;
    private String mText2;
    private Bitmap image;

    DataObject (String text1, String text2, Bitmap img){
        mText1 = text1;
        mText2 = text2;
        image = img;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
