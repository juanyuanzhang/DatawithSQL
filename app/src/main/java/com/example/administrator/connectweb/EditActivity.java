package com.example.administrator.connectweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

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
    private static final int PICK_IMAGE=1 ;
    private String picturePath;
    private Bitmap bitmap , oldpic;
    private Handler handler;
    private LoadImageThread imageProcess;
    private  DeleteData deleteData;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findview();
        bdata = this.getIntent().getExtras();
        if(bdata.getString("type").equals("edit")) {
            queryName = bdata.getString("name");
            Log.i("name =", queryName);
            textView.setText("編輯聯絡人");
            editData = new LoadEditData(this, queryName);
            try {
                old_data = editData.execute("https://juanyuanzhang.000webhostapp.com/query.php").get();
                etname.setText(old_data[1]);
                etphone.setText(old_data[2]);
                etemail.setText(old_data[3]);
                etbirth.setText(old_data[4]);
                if(old_data[5] != null){
                    handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == 1){
                                pic.setImageBitmap(oldpic);
                            }
                        }
                    };
                    imageProcess = new LoadImageThread();
                    imageProcess.start();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class LoadImageThread extends Thread{
        @Override
        public void run() {
            String imageUrl = "https://juanyuanzhang.000webhostapp.com/images/" + old_data[5];
            try{
                URL url = new URL(imageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    oldpic = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                handler.sendEmptyMessage(1);
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
                    updateData = new UpdateData(EditActivity.this,old_data,picturePath);
                    updateData.execute("https://juanyuanzhang.000webhostapp.com/update.php");

                }else if(bdata.getString("type").equals("new")){
                    newname =  etname.getText().toString();
                    newphone = etphone.getText().toString();
                    newmail =  etemail.getText().toString();
                    newbirth =  etbirth.getText().toString();
                    String[] data = new String[]{newname, newphone, newmail, newbirth};
                    insertData = new InsertData(EditActivity.this, data, picturePath);
                    insertData.execute("https://juanyuanzhang.000webhostapp.com/insert.php");
                    Intent i2 = new Intent(this,MainActivity.class);
                    startActivity(i2);
                }

                break;
            case R.id.btnselect:
                selectImageFromGallery();
                break;

        }

    }



    public void selectImageFromGallery(){
        if(Build.VERSION.SDK_INT<19){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);

        }else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            Log.i("segment=",String.valueOf(selectedImage.getLastPathSegment()));  //API太高會失敗
            String id = selectedImage.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;

            Uri uri = getUri();
            picturePath= "path";
            Cursor imageCursor = getContentResolver().query(uri,
                    imageColumns,
                    MediaStore.Images.Media._ID +"="+id, //等於旁邊不能有空格！！！！！　API不超過23 權限不足無法使用，因超過23API需要另一種設定權限
                    null,
                    imageOrderBy);
//            Cursor imageCursor = getContentResolver().query(uri, imageColumns,
//                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);
            if(imageCursor.moveToFirst()){
                picturePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            Log.i("path=",picturePath);
            decodeFile(picturePath);
        }
    }
    private Uri getUri(){

        String state = Environment.getExternalStorageState();

        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {

            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
    public void decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inSampleSize = 1;
        BitmapFactory.decodeFile(filePath,o);

        final int REQUIRED_SIZE = 1024;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true){
            Log.i("width_tmp", String.valueOf(o.outWidth));
            if(width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)break;

            width_tmp/=2;
            height_tmp/=2;
            scale*=2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize= scale;
        bitmap = BitmapFactory.decodeFile(filePath,o2);

        pic.setImageBitmap(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bdata.getString("type").equals("edit")){
            MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delbar, menu);
    }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("確認訊息:")
                .setMessage("確定要刪除此聯絡人嗎?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData = new DeleteData(EditActivity.this,old_data);
                        deleteData.execute("https://juanyuanzhang.000webhostapp.com/delete.php");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

        return super.onOptionsItemSelected(item);
    }
}
