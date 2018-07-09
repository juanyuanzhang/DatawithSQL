package com.example.administrator.connectweb;

import android.graphics.Bitmap;

//建立contact將取得的資料設定給變數用

public class Contact {
    private Bitmap pic;
    private String name;
    private String phone;
    private String email;
    private String birth;

    public Contact(Bitmap pic, String name, String phone, String email, String birth) {
       // this.pic = pic;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth() {
        return birth;
    }
}
