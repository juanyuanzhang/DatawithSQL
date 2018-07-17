package com.example.administrator.connectweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoadEditData extends AsyncTask<String,Void,String[]> {
    private Context c;
    private ProgressDialog progressDialog ;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    private Bitmap oldpic;
    String queryName;
    URL u;


    public LoadEditData(Context context,  String queryName) {
        this.c = context;
        progressDialog = new ProgressDialog(c);
        this.queryName = queryName;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        try {
            u =new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //將資料傳送到遠端
            String name = "Name=" + URLEncoder.encode(queryName,"UTF-8");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(name);
            writer.flush();
            writer.close();
            os.close();
            Log.i("postString=",name);

            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];  //原本的傳輸資料太少，所以在自訂一個傳輸單位
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  //位元陣列輸出資料流
            while(is.read(b) != -1){          //如果資料不等於-1(無資料時) 輸出
                baos.write(b);
            }
            String response = new String(baos.toByteArray());
            Log.i("podt-data=",response);
            connection.connect();
            JSONObject obj = new JSONObject(response);
            String[] old_data;
            old_data = loadContact(obj);
            Log.v("data=",old_data[0].toString());

            return old_data;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("連線中......");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
    private  String[] loadContact(JSONObject obj) throws JSONException{
        String id =obj.getString("ContactID");
        String pic =obj.getString("Picture");
        String name = obj.getString("Name");
        String phoneNum = obj.getString("Phone");
        String email = obj.getString("Email");
        String birth = obj.getString("Birthday");
        Log.v("jsonObj",obj.getString("Picture").toString());
        String[]old_data = new String[]{id,name,phoneNum,email,birth,pic};
        return old_data;

    }
}
