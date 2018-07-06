package com.example.administrator.connectweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnok,btnback,btnselect;
    ImageView pic;
    EditText etname,etphone,etemail,etbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findview();


    }
    public void findview(){
        btnok = findViewById(R.id.btnok);
        btnback = findViewById(R.id.btnback);
        btnselect = findViewById(R.id.btnselect);
        pic = findViewById(R.id.pic);
        etname = findViewById(R.id.etname);
        etphone = findViewById(R.id.etphone);
        etemail = findViewById(R.id.etemail);
        etbirth = findViewById(R.id.etbirth);

        btnback.setOnClickListener(this);
        btnselect.setOnClickListener(this);
        btnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnback:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.btnok:

                break;
            case R.id.btnselect:

                break;

        }

    }
}
