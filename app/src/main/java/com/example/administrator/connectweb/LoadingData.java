package com.example.administrator.connectweb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
    protected List<Contact> doInBackground(String... strings) {
        List<Contact> result = new ArrayList<Contact>();
        URL u = null;
        try {
            u = new URL(strings[0]);   //只有一組資料
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  //位元陣列輸出資料流
            while(is.read(b) != -1){          //如果資料不等於-1(無資料時) 輸出
                baos.write(b);
            }
            String JSONResp = new String(baos.toByteArray()); //用String  物件 接收資料流
            Log.i("JSONResp=", JSONResp);

            JSONArray jsonArray = new JSONArray(JSONResp); //將字串JSONResp轉換成JSONARRAY型態
            for(int i = 0 ; i<jsonArray.length() ;i++){
                if(jsonArray.getJSONObject(i)!=null) {
                    result.add(convertContact(jsonArray.getJSONObject(i)));
                    Log.v("data=", jsonArray.getJSONObject(i).toString());
                }
            }
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return null;
    }
    private  Contact convertContact (JSONObject obj) throws JSONException {
        String name = obj.getString("Name");
        String phoneNum = obj.getString("Phone");
        String email = obj.getString("Email");
        String birthday = obj.getString("Birthday");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new Contact(null, name, phoneNum, email, birthday);

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("檔案下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<Contact> contactList) {
        super.onPostExecute(contactList);
        dialog.dismiss();
    }
}
