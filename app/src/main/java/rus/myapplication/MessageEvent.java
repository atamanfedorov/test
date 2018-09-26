package rus.myapplication;

import android.graphics.drawable.Drawable;


public class MessageEvent {

    private String mEmail;
    private String mPhone;
    private String mPassword;
    private Drawable drawable;


    public MessageEvent(String mEmail, String mPhone, String mPassword, Drawable drawable) {
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.drawable = drawable;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmPassword() {
        return mPassword;
    }

    public Drawable getDrawable() {
        return drawable;
    }

}
