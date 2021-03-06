package com.example.administrator.connectweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UpdateData extends AsyncTask<String,Void,String>{
    private Context c;
    private ProgressDialog progressDialog ;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String[] getdata ;
    String attachmentFileName;
    int bytesRead,bytesAvailable,bufferSize;
    byte[] buffer ;
    int maxBufferSize = 1*1024*1024;

    public UpdateData(Context context, String[] data,String picturePath) {
        this.c = context;
        progressDialog = new ProgressDialog(c);
        this.getdata =data;
        this.attachmentFileName = picturePath;
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

            if(this.attachmentFileName != null){
                File sourceFile = new File(attachmentFileName);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                re.writeBytes(this.twoHyphens+this.boundary+this.crlf);
                re.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + this.attachmentFileName + "\""+crlf);
                re.writeBytes(crlf);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                //寫入串流
                Log.i("bytesRead", String.valueOf(bytesRead));
                while (bytesRead > 0) {

                    re.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                re.write(buffer);
                re.writeBytes(this.crlf);
            }else {
                re.writeBytes(this.twoHyphens+this.boundary+this.crlf);
                re.writeBytes("Content-Disposition: form-data; name=\"Picture\"" + "\""+crlf);
                re.writeBytes(crlf);
                re.writeBytes(getdata[5]);
                re.writeBytes(crlf);
            }

            re.writeBytes(this.twoHyphens+this.boundary+this.crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"ContactID\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[0]);
            re.writeBytes(crlf);
            re.writeBytes(this.twoHyphens+this.boundary+this.crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"Name\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[1]);
            re.writeBytes(crlf);
            re.writeBytes(twoHyphens+boundary+crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"Phone\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[2]);
            re.writeBytes(crlf);
            re.writeBytes(twoHyphens+boundary+crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"Email\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[3]);
            re.writeBytes(crlf);
            re.writeBytes(twoHyphens+boundary+crlf);
            re.writeBytes("Content-Disposition: form-data; name=\"Birthday\"" + "\""+crlf);
            re.writeBytes(crlf);
            re.writeBytes(getdata[4]);
            re.writeBytes(crlf);
            re.writeBytes(twoHyphens+boundary+twoHyphens+crlf);

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
        progressDialog.setMessage("連線中......");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        int isSuccess= 0;
        isSuccess = s.indexOf("edit Success");
        if(isSuccess != -1) Toast.makeText(c,"成功更新一筆資料", Toast.LENGTH_LONG).show();
        Intent i = new Intent(c, MainActivity.class);
        c.startActivity(i);


    }
}
