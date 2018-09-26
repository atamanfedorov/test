package rus.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class PreviewFragment extends Fragment {

    private ImageView mprevImage;
    private TextView  mprevEmail;
    private TextView  mprevPhone;
    private TextView  mprevPassw;

    public PreviewFragment() {

    }

    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.preview_screen,container,false);

        mprevEmail = (TextView)v.findViewById(R.id.prevEmail);
        setTextViewDrawableColor(mprevEmail,R.color.color4);

        mprevPhone = (TextView)v.findViewById(R.id.prevPhone);
        setTextViewDrawableColor(mprevPhone,R.color.color4);

        mprevPassw = (TextView)v.findViewById(R.id.prevPassw);
        setTextViewDrawableColor(mprevPassw,R.color.color4);

        mprevImage = (ImageView)v.findViewById(R.id.prevImage);
        Button button = (Button) v.findViewById(R.id.buttonSend);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendByEmail();
                }
                catch (ActivityNotFoundException ex)
                {
                    Toast.makeText(getContext(), getString(R.string.error_email), Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event != null) {
            mprevEmail.setText(event.getmEmail());
            mprevPhone.setText(event.getmPhone());
            mprevPassw.setText(event.getmPassword());
            mprevImage.setImageDrawable(event.getDrawable());
        }

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void sendByEmail() {


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", mprevEmail.getText().toString(), null));

        StringBuilder subject = new StringBuilder(getString(R.string.app_name));
        subject.append(":")
                .append(getString(R.string.spaceSymbol))
                .append(getString(R.string.letter_subject));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());

        StringBuilder body = new StringBuilder(getString(R.string.email));
        String separator = System.getProperty("line.separator");
        body.append(mprevEmail.getText())
                .append(separator)
                .append(getString(R.string.phone))
                .append(mprevPhone.getText())
                .append(separator)
        ;
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());

        Bitmap photo = ((BitmapDrawable) mprevImage.getDrawable()).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), photo, "photo", null);
        Uri photoUri = Uri.parse(path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, photoUri);

        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
