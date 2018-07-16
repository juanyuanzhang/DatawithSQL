package com.example.administrator.connectweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnok,btnback,btnselect;
    ImageView pic;
    EditText etname,etphone,etemail,etbirth;
    String newname,newphone,newmail,newbirth;
    TextView textView ;

    private InsertData insertData;
    private Bundle bdata ;
    private String queryName;
    private LoadEditData editData;
    private String[] old_data;
    private UpdateData updateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findview();
        bdata = this.getIntent().getExtras();
        queryName = bdata.getString("name");
        Log.i("name =",queryName );
        if(bdata.getString("type").equals("edit")){

            textView.setText("編輯聯絡人");
            editData = new LoadEditData(this,queryName);
            try {
                old_data = editData.execute("https://juanyuanzhang.000webhostapp.com/query.php").get();
                etname.setText(old_data[1]);
                etphone.setText(old_data[2]);
                etemail.setText(old_data[3]);
                etbirth.setText(old_data[4]);
            }catch (Exception e){
                e.printStackTrace();
            }

        }





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
        textView = findViewById(R.id.textView);

        btnback.setOnClickListener(this);
        btnselect.setOnClickListener(this);
        btnok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnback:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.btnok:
                if(bdata.getString("type").equals("edit")) {
                    old_data[1]=etname.getText().toString();
                    old_data[2]=etphone.getText().toString();
                    old_data[3]=etemail.getText().toString();
                    old_data[4]=etbirth.getText().toString();
                    updateData = new UpdateData(EditActivity.this,old_data);
                    updateData.execute("https://juanyuanzhang.000webhostapp.com/update.php");

                }else{
                    newname =  etname.getText().toString();
                    newphone = etphone.getText().toString();
                    newmail =  etemail.getText().toString();
                    newbirth =  etbirth.getText().toString();
                    String[] data = new String[]{newname, newphone, newmail, newbirth};
                    insertData = new InsertData(EditActivity.this, data);
                    insertData.execute("https://juanyuanzhang.000webhostapp.com/insert.php");
                    Intent i2 = new Intent(this,MainActivity.class);
                    startActivity(i2);
                }

                break;
            case R.id.btnselect:

                break;

        }

    }
}
