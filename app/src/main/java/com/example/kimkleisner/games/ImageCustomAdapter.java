package com.example.kimkleisner.games;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> objects;

    public ImageCustomAdapter (Context _context, ArrayList<String> _objects){
        context = _context;
        objects = _objects;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.image_custom_layout, viewGroup, false);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.imageViewGameImage);

        //icon.setImageResource(R.mipmap.ic_launcher);

        if(!objects.get(i).equals("")) {
            Picasso.with(context)
                    .load(objects.get(i))
                    .resize(800, 500)
                    .into(icon);
        }else{
            icon.setImageResource(R.mipmap.ic_launcher);
        }

        return view;
    }
}
