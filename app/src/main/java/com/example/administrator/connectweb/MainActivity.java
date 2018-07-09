package com.example.administrator.connectweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ContactAdapter contactAdapter;
    Intent intent ;
    LoadingData loadingData;
    List<Contact> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        loadingData = new LoadingData(this); //與線上資料庫取得連結
        try {
          list=loadingData.execute("https://juanyuanzhang.000webhostapp.com/db_connect.php").get();//使用get方法取得
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        contactAdapter = new ContactAdapter(this,list); //利用ContactAdapter取得資料
        listView.setAdapter(contactAdapter);    //在用取得的資料顯示在listView


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this,EditActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}
