package com.example.kimkleisner.games;

import java.io.Serializable;

public class Game implements Serializable {

    String name;
    String description;
    String image;
    String platforms;
    String id;

    public Game(String _name, String _description, String _image, String _platforms, String _id){
        name = _name;
        description = _description;
        image = _image;
        platforms = _platforms;
        id = _id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getPlatforms() {
        return platforms;
    }

    public String getId() {
        return id;
    }
}
