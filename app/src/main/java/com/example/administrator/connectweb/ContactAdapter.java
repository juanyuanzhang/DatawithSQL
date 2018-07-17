package com.example.administrator.connectweb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends BaseAdapter {  //自定Adapter
    private LayoutInflater layoutInflater;
    private List<Contact> contactList ;  //使用LIST陣列存放資料(JSON式陣列字串)

    public ContactAdapter(Context c , List<Contact> contactList) { //自定建構子
        layoutInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.contactList=contactList;
    }

    @Override
    public int getCount() {
        if(contactList!=null) {
            return contactList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(contactList != null) {
            return contactList.get(position);
        }return null;
    }

    @Override
    public long getItemId(int position) {
        if(contactList!=null) {
            return contactList.indexOf(getItem(position));
        }return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact data = contactList.get(position);
        convertView = layoutInflater.inflate(R.layout.item_view,null);

        ImageView pic = (ImageView) convertView.findViewById(R.id.imageView);
        TextView tvname = (TextView) convertView.findViewById(R.id.tvname);
        TextView tvphone = (TextView) convertView.findViewById(R.id.tvphone);
        TextView tvbirth = (TextView) convertView.findViewById(R.id.tvbirth);
        if(data.getPic() != null){
            pic.setImageBitmap(data.getPic());
            Log.i("getPic=",data.getPic().toString());
        }
        pic.setImageBitmap(data.getPic());
        tvname.setText(data.getName());
        tvphone.setText(data.getPhone());
        tvbirth.setText(data.getBirth());
        return  convertView;
    }
}
