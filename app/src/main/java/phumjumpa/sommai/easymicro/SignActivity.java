package phumjumpa.sommai.easymicro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignActivity extends AppCompatActivity {
// Explicit
    private EditText nameEditText,userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString,userString, passwordString;




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
                if (nameString.equals("") || userString.equals("") || passwordString.equals(""))
                {
                    // Have space
                    Log.d("5novV1", "Have Space");
                    myAlert _myAlert = new myAlert(SignActivity.this,
                            R.drawable.doremon48,
                            getResources().getString(R.string.title_havespace)
                            , getResources().getString(R.string.message_havespace));
                    _myAlert.myDiaLog();

                } // if

            } // onclick
        });

         // Image Controller

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to other app

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Please select program image"),0);




            } // onclick image
        });



    } //Main method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( (requestCode==0) && ( resultCode==RESULT_OK))
        {
            Log.d("5novV1", "result ok");
            // Result =true

        } // if



    } // onActivityResult
} // Main Class

