package phumjumpa.sommai.easymicro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    // explicit การประกาศตัวแปร
    private Button signInButton, signUpButton;
    private EditText userEditText,passwordEditText;
    private String userString,passwordString;
    private MyConstante myConstante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myConstante = new MyConstante();
        //Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        signUpButton = (Button) findViewById(R.id.button2);
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);


        // Signup controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignActivity.class));
            }
        });

        //Signin Controller
         signInButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
            // Get value From Edit Text
                 userString = userEditText.getText().toString().trim();
                 passwordString = passwordEditText.getText().toString().trim();

                 //check space
                 if (userString.equals("") || passwordString.equals("")) {
                     myAlert _myAlert = new myAlert(MainActivity.this,
                             R.drawable.kon48,
                             getResources().getString(R.string.title_havespace),
                             getResources().getString(R.string.message_havespace));
                     _myAlert.myDiaLog();
                 } else {
                     // No Space
                        Getuser getuser=new Getuser(MainActivity.this);
                     getuser.execute(myConstante.getUrlJSon());

                 }

             } // onclick sign in
         });


    }// Main method

    private class Getuser extends AsyncTask<String, Void, String> {
private Context context;

        public Getuser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                OkHttpClient okHttpClient=new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).build();
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } // do in

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("6novV3", "JSon ==>" + s);






        } //onPost
    } // class


} // main class นี่คือ class หลัก
