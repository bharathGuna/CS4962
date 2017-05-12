package com.example.cs4962.battleship;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class GameActivity extends ActionBarActivity
{

    private class GameSummary
    {
        public String id;
        public String name;
        public String status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                  /*  URL gameListUrl = new URL("http://battleship.pixio.com/api/games");
                    HttpURLConnection gameListConnection = (HttpURLConnection) gameListUrl.openConnection();
                    Scanner gameListScanner = new Scanner(gameListConnection.getInputStream());
                    StringBuilder stringBuilder = new StringBuilder();
                    while(gameListScanner.hasNextLine())
                    {
                        stringBuilder.append(gameListScanner.nextLine());
                    }
                    String gameListString = stringBuilder.toString();

                    Gson gson = new Gson();
                    GameSummary[] games = gson.fromJson(gameListString,GameSummary[].class );
                    Log.i("Battleship", "Got back gameList with count: " + games.length);
                */
                    URL gameCreateUrl = new URL("http://battleship.pixio.com/api/games");
                    HttpURLConnection gameCreateConnection = (HttpURLConnection) gameCreateUrl.openConnection();
                    gameCreateConnection.setRequestMethod("POST");
                    //JSONObject payload = new JSONObject()
                    gameCreateConnection.addRequestProperty("Content-Type","application/json");


                    String payload ="{ \"gameName\"  : \"STRING\", \"playerName\": \"STRING\" }"; //"{\"playerName\" : me\", \"gameName\" : \"someGame\" }";
                    gameCreateConnection.setDoOutput(true);
                    OutputStreamWriter outputStream = new OutputStreamWriter(gameCreateConnection.getOutputStream());
                    outputStream.write(payload);
                    outputStream.flush();
                    outputStream.close();
                    int code = gameCreateConnection.getResponseCode();
                    gameCreateConnection.disconnect();
                    Log.i("Battleship", "Added game with response code: " + code);
                } catch (MalformedURLException e)
                {
                    Log.i("Battleship", "Invalid request url");
                    e.printStackTrace();
                } catch (IOException e)
                {
                    Log.i("Battleship", "Invalid request url");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
