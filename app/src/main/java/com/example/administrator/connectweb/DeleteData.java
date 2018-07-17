package com.example.administrator.connectweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DeleteData extends AsyncTask <String,Void,String>{
    private Context c;

    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String[] getdata ;

    public DeleteData(Context c, String[] getdata) {
        this.c = c;
        this.getdata = getdata;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL u = null;
        try {
            u = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //設定標頭格式
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);  //錯誤字，導致無法傳送到PHP讀取
            connection.setRequestProperty("Charset", "UTF-8");

            //建立資料串流
            DataOutputStream re = new DataOutputStream(connection.getOutputStream());
            re.writeBytes(this.twoHyphens+this.boundary+this.crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"ContactID\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[0]);
            re.writeBytes(crlf);
            re.writeBytes(this.twoHyphens+this.boundary+this.crlf);

            re.flush();
            //關閉dataoutputstream
            re.close();
            Log.i("string====>",re.toString());

            connection.connect();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];  //原本的傳輸資料太少，所以在自訂一個傳輸單位
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  //位元陣列輸出資料流
            while(is.read(b) != -1){          //如果資料不等於-1(無資料時) 輸出
                baos.write(b);
            }
            String response = new String(baos.toByteArray());
            Log.i("podt-data=",response);
            return response;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent i = new Intent(c, MainActivity.class);
        c.startActivity(i);


    }
}
