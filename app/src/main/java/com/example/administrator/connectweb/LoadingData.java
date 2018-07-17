package com.example.administrator.connectweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//連接資料庫 用類別
public class LoadingData extends AsyncTask<String,Void,List<Contact>> {

    private final ProgressDialog dialog;
    Context c;
    public LoadingData (Context c) {  //用建構子傳送Context
          this.c = c;
          dialog = new ProgressDialog(c);
    }
    @Override
    protected List<Contact> doInBackground(String... strings) {  //在背景執行的方法，不想一直刷新螢幕
        List<Contact> result = new ArrayList<Contact>();
        URL u = null;
        try {
            u = new URL(strings[0]);   //只有一組資料所以用0
            HttpURLConnection connection = (HttpURLConnection) u.openConnection(); //與網路資料庫連結
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];  //原本的傳輸資料太少，所以在自訂一個傳輸單位
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  //位元陣列輸出資料流
            while(is.read(b) != -1){          //如果資料不等於-1(無資料時) 輸出
                baos.write(b);
            }
            String JSONResp = new String(baos.toByteArray()); //用String  物件 接收資料流
            Log.i("JSONResp=", JSONResp);

            JSONArray jsonArray = new JSONArray(JSONResp); //將字串JSONResp轉換成JSONARRAY型態
            for(int i = 0 ; i<jsonArray.length() ;i++){
                if(jsonArray.getJSONObject(i)!=null) {
                    result.add(convertContact(jsonArray.getJSONObject(i)));//JSONARRAY資料加入result
                    Log.v("data=", jsonArray.getJSONObject(i).toString());
                }
            }
            return result; //回傳取得JSON的陣列資料

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return null;
    }
    private  Contact convertContact (JSONObject obj) throws JSONException {
        Bitmap bitmap;
        if(obj.getString("Picture")!=null){
            bitmap = LoadImage("https://juanyuanzhang.000webhostapp.com/images/" + obj.getString("Picture").toString());
        }else{
            bitmap = LoadImage("https://juanyuanzhang.000webhostapp.com/images/supportmale.png");
        }
//        String pic = obj.getString("Picture");
        String name = obj.getString("Name");
        String phoneNum = obj.getString("Phone");
        String email = obj.getString("Email");
        String birthday = obj.getString("Birthday");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new Contact(bitmap, name, phoneNum, email, birthday);

    }
    @Override
    protected void onPreExecute() {  //預先執行的方法(在背景執行前執行
        super.onPreExecute();
        dialog.setMessage("檔案下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<Contact> contactList) { //在背景執行完執行
        super.onPostExecute(contactList);
        dialog.dismiss();
    }
    private Bitmap LoadImage(String imageUrl){
        Bitmap pic = null;
        try{
            URL url = new URL(imageUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                pic = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return pic ;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return pic;
    }
}
