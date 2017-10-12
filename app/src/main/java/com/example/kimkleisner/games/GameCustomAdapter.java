package com.example.kimkleisner.games;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GameCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Game> objects;

    public GameCustomAdapter (Context _context, ArrayList<Game> _objects){
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
                    inflate(R.layout.game_custom_layout, viewGroup, false);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.imageViewGame);
        TextView name = (TextView) view.findViewById(R.id.textViewGameName);
        TextView platform = (TextView) view.findViewById(R.id.textViewGamePlatform);


        //icon.setImageResource(R.mipmap.ic_launcher);
        name.setText(objects.get(i).name);
        platform.setText(objects.get(i).platforms);

//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(objects.get(i).image).getContent());
//            icon.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        if(!objects.get(i).image.equals("")) {
            Picasso.with(context)
                    .load(objects.get(i).image)
                    .resize(200, 200)
                    .into(icon);
        }else{
            icon.setImageResource(R.mipmap.ic_launcher);
        }

        return view;
    }
}
