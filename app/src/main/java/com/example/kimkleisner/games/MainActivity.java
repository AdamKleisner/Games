package com.example.kimkleisner.games;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<Game> savedArray;
    String apiData;
    String urlImage;
    EditText searchBar;
    Button searchButton;
    ListView list;
    ArrayList<String> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(this);

        gameList = new ArrayList<>();

        searchButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(!searchBar.getText().toString().equals("")){

            String gameSearch = searchBar.getText().toString().toLowerCase();
            String apiSearch = gameSearch.replace(" ","%20");

            apiData = "https://www.giantbomb.com/api/search/?api_key=d03d2ba59da5689de4fdc4312ab14f38b31394df&format=json&query=" + apiSearch +"&resources=game&limit=100";

            giantBombApiPull();

        }else{

            Log.i("this text","Nothing entered");
            Toast.makeText(this,"Not valid entry",Toast.LENGTH_LONG).show();
        }

    }

    public void giantBombApiPull() {

        asyncClass task = new asyncClass();
        task.execute(apiData);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent selectedGameIntent = new Intent(this,SelectedGame.class);
        selectedGameIntent.putExtra("selectedGame",savedArray.get(i));
        startActivity(selectedGameIntent);
    }


    private class asyncClass extends AsyncTask<String, Void, ArrayList<Game>> {

            @Override
            protected ArrayList<Game> doInBackground(String... params) {

                ArrayList<Game> apiArray = new ArrayList<>();
                try {
                    URL url = new URL(params[0]);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    String data = IOUtils.toString(is);

                    try {
                        JSONObject outerMostObject = new JSONObject(data);

                        JSONArray childrenData = outerMostObject.getJSONArray("results");

                        for (int i = 0; i < childrenData.length(); i++){

                            urlImage = "";

                            JSONObject childrenObject = childrenData.getJSONObject(i);

                            String name = childrenObject.getString("name");

                            String id = childrenObject.getString("id");

                            String descrpition = childrenObject.getString("deck");

                            String consoles = "";

                            if(!childrenObject.isNull("image")) {
                                JSONObject image = childrenObject.getJSONObject("image");

                                urlImage = image.getString("small_url");
                            }

                            if(!childrenObject.isNull("platforms")) {
                                JSONArray platforms = childrenObject.getJSONArray("platforms");
                                for (int j = 0; j < platforms.length(); j++) {

                                    JSONObject childrenObject2 = platforms.getJSONObject(j);
                                    String console = childrenObject2.getString("name");

                                    consoles = consoles + " " + console + " ";

                                }
                            }

                            Game pulledGame = new Game(name,descrpition,urlImage,consoles,id);

                            apiArray.add(pulledGame);
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    savedArray = apiArray;
                    is.close();
                    connection.disconnect();

                    return savedArray;

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

                Toast.makeText(getBaseContext(),"Failed to find any games",Toast.LENGTH_LONG).show();

                cancel(true);
            }

            @Override
            protected void onPostExecute(ArrayList<Game> strings) {
                super.onPostExecute(strings);

                GameCustomAdapter adapter1 = new GameCustomAdapter(MainActivity.this,strings);

                list.setAdapter(adapter1);

                Toast.makeText(getBaseContext(),"Loading Complete",Toast.LENGTH_LONG).show();
            }
        }
}
