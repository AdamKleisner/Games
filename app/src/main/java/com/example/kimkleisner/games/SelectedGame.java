package com.example.kimkleisner.games;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;


public class SelectedGame extends AppCompatActivity implements Serializable {

    String apiData;
    String developerText;
    String publisherText;
    ListView videosList;
    TextView selectedGameName;
    TextView selectedGameDescription;
    TextView developer;
    TextView publisher;
    TextView platforms;
    ImageView selectedGameImage;
    Game selectedGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedvideo_layout);

        Intent i = getIntent();
        selectedGame = (Game) i.getSerializableExtra("selectedGame");

        videosList = (ListView) findViewById(R.id.videoPictures);
        selectedGameName = (TextView) findViewById(R.id.textViewSelectedGame);
        selectedGameDescription = (TextView) findViewById(R.id.textViewSelectedGameDescription);
        selectedGameImage = (ImageView) findViewById(R.id.imageViewSelectedGameImage);
        publisher = (TextView) findViewById(R.id.textViewPublisher);
        developer = (TextView) findViewById(R.id.textViewDeveloper);
        platforms = (TextView) findViewById(R.id.textViewSelectedGamePlatforms);

        selectedGameName.setText(selectedGame.getName());
        selectedGameDescription.setText(selectedGame.getDescription());
        platforms.setText(selectedGame.getPlatforms());

        Picasso.with(this)
                .load(selectedGame.getImage())
                .resize(300,300)
                .into(selectedGameImage);

        asyncClass async = new asyncClass();
        apiData = "https://www.giantbomb.com/api/game/" + selectedGame.getId() + "/?api_key=d03d2ba59da5689de4fdc4312ab14f38b31394df&format=json";
        async.execute(apiData);

    }

    //getting videos
    private class asyncClass extends AsyncTask<String, Void, ArrayList<SelectedGameImage>> {

        @Override
        protected ArrayList<SelectedGameImage> doInBackground(String... params) {

            ArrayList<SelectedGameImage> apiArray = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                String data = IOUtils.toString(is);

                try {

                    ArrayList<String> screenUrl = new ArrayList<>();
                    developerText = "";
                    publisherText = "";

                    JSONObject outerMostObject = new JSONObject(data);

                    JSONObject childrenData = outerMostObject.getJSONObject("results");

                        JSONArray images = childrenData.getJSONArray("images");

                    if(!childrenData.isNull("images")) {
                        for (int j = 0; j < images.length(); j++) {
                            JSONObject imageObjects = images.getJSONObject(j);

                            screenUrl.add(imageObjects.getString("screen_url"));

                        }
                    }screenUrl.add("");

                    if(!childrenData.isNull("developers")) {
                        JSONArray developers = childrenData.getJSONArray("developers");
                        for (int i = 0; i < developers.length(); i++) {
                            JSONObject developerObjects = developers.getJSONObject(i);

                            developerText = developerObjects.getString("name");
                        }
                    }else{
                        developerText = "";
                    }

                    if(!childrenData.isNull("publishers")) {

                        JSONArray publishers = childrenData.getJSONArray("publishers");
                        for (int k = 0; k < publishers.length(); k++) {
                            JSONObject publisherObjects = publishers.getJSONObject(k);

                            publisherText = publisherObjects.getString("name");
                        }
                    }else{
                        publisherText = "";
                    }

                            SelectedGameImage pulledImage = new SelectedGameImage(screenUrl, developerText, publisherText);

                            apiArray.add(pulledImage);


                }catch (JSONException e) {
                    e.printStackTrace();
                }

                is.close();
                connection.disconnect();

                return apiArray;

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(ArrayList<SelectedGameImage> strings) {
            super.onPostExecute(strings);


            //set publisher and developer text here
            publisher.setText(publisherText);
            developer.setText(developerText);

            ImageCustomAdapter adapter1 = new ImageCustomAdapter(SelectedGame.this,strings.get(0).getUrl());
            videosList.setAdapter(adapter1);

            Toast.makeText(getBaseContext(),"Loading Complete",Toast.LENGTH_LONG).show();
        }
    }
}
