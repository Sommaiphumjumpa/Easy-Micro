package phumjumpa.sommai.easymicro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SignActivity extends AppCompatActivity {
    // Explicit
    private EditText nameEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, userString, passwordString, imageString,
            imagePathString, imageNameString;

    private Uri uri;

    // 06-nov-2016
    private boolean aBoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        //bind widget
        nameEditText = (EditText) findViewById(R.id.editText);
        userEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);

        // Signup Controller

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get value from edit text

                nameString = nameEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();


                // Check space
                if (nameString.equals("") || userString.equals("") || passwordString.equals("")) {
                    // Have space
                    Log.d("5novV1", "Have Space");
                    myAlert _myAlert = new myAlert(SignActivity.this,
                            R.drawable.doremon48,
                            getResources().getString(R.string.title_havespace)
                            , getResources().getString(R.string.message_havespace));
                    _myAlert.myDiaLog();

                } else if (aBoolean) {
                    // none choose image
                    myAlert _myAlert1 = new myAlert(SignActivity.this, R.drawable.nobita48,
                            getResources().getString(R.string.title_ImageChoose),
                            getResources().getString((R.string.title_ImageChoose))
                    );
                    _myAlert1.myDiaLog();
                } else {
                    // upload to server
                    uploadImage();
                    upLoadString();
                }

            } // onclick
        });

        // Image Controller

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to other app

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please select program image"), 0);


            } // onclick image
        });


    } //Main method

    private class AddNewUser extends AsyncTask<String, Void, String> {
      //Explicit
       private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                OkHttpClient okHttpClient=new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Name", nameString)
                        .add("User", userString)
                        .add("Password", passwordString)
                        .add("Image", imageString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("6novV2", "e do inback" + e.toString());
                return null;
            }


        } //doinback

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("6NovV2","Result==>" + s);


        } // Onpost
    }// Add new user Class
    private void upLoadString() {
        imageString="http://swiftcodingthai.com/mic/Images"+ imageNameString;
        MyConstante myConstante=new MyConstante();
        AddNewUser addNewUser = new AddNewUser(SignActivity.this);
        addNewUser.execute((myConstante.getUrlAddUser()));


    }

    private void uploadImage() {
        // Change Policy
        StrictMode.ThreadPolicy threadPolicy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy((threadPolicy));

        try {
            MyConstante myConstante = new MyConstante();
            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect(myConstante.getHostString(),
                    myConstante.getPortAnInt(),
                    myConstante.getUserString(),
                    myConstante.getPasswordString());
            simpleFTP.bin();
            simpleFTP.cwd("Images");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

            Toast.makeText(SignActivity.this,"Upload Image Finish",Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Log.d("6novV1", "ftp error==> " + e.toString());
        }

    }// upload image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            // Result =true
            Log.d("5novV1", "result ok");

            //Setup choose image to imageview
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory
                        .decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // check boolean
            aBoolean = false;
            // find path image choose
            imagePathString = myFindPath(uri);

            // Find Name of image choose
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("6novV1", "Path==> " + imageNameString);

        } // if


    } // onActivityResult

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {
            result = uri.getPath();
        } // if

        return result;
    }
} // Main Class

