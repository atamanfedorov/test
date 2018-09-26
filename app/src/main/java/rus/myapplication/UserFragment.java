package rus.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextInputLayout mlayoutEmail;
    private TextInputLayout mlayoutPhone;
    private TextInputLayout mlayoutPassword;

    private AppCompatImageButton mImageButton;

    private AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);


    public UserFragment() {
    }



    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.user_screen,container,false);

        mlayoutEmail = (TextInputLayout) v.findViewById(R.id.layoutEmail);

        mlayoutPhone = (TextInputLayout) v.findViewById(R.id.layoutPhone);
        mlayoutPassword = (TextInputLayout) v.findViewById (R.id.layoutPassword);

        mImageButton = (AppCompatImageButton) v.findViewById(R.id.imageView1);
        mImageButton.setTag(mImageButton.getDrawable().hashCode());

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Button button = (Button)v.findViewById(R.id.buttonView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        mAwesomeValidation.addValidation(mlayoutEmail,android.util.Patterns.EMAIL_ADDRESS,getString(R.string.edit_text_email_error));
        mAwesomeValidation.addValidation(mlayoutPhone, Pattern.PHONE_PATTERN,getString(R.string.edit_text_phone_error));
        mAwesomeValidation.addValidation(mlayoutPassword, Pattern.PASSWORD_PATTERN,getString(R.string.edit_text_password_error));



        return v;
    }

    public void onButtonPressed() {

        if (!mAwesomeValidation.validate())
            return;

        if(mImageButton.getTag().hashCode() == mImageButton.getDrawable().hashCode()) {

            Toast toast = Toast.makeText(getContext(),"No Image",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();

            return;

        }


        ((MainActivity)getActivity()).showPreviewFragment();

        String email = mlayoutEmail.getEditText().getText().toString();
        String phone = mlayoutPhone.getEditText().getText().toString();
        String password = mlayoutPassword.getEditText().getText().toString();

        EventBus.getDefault().postSticky(new MessageEvent(email,phone,password, mImageButton.getDrawable()));
    }



    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            mImageButton.setColorFilter(android.R.color.black);
            mImageButton.setImageBitmap(imageBitmap);


        }

    }
}
