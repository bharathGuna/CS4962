package com.example.cs4962.scrap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class PageActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL("http://www.nytimes.com");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    InputStream responseStream = connection.getInputStream();
                    //BufferedInputStream bufferedInputStream = new BufferedInputStream(responseStream);
                   // int responseData = bufferedInputStream.read();
                    Scanner responseScanner = new Scanner(responseStream);
                    StringBuilder responseString = new StringBuilder();
                    while(responseScanner.hasNext())
                    {
                        responseString.append(responseScanner.nextLine());
                    }
                    String response = responseString.toString();
                    Log.i("Scraper", "Got Response: \n" + response);
                    int imageTageStartIndex = response.indexOf("<img src=\"");
                    if(imageTageStartIndex > 0)
                    {
                        response = response.substring(imageTageStartIndex);
                    }
                    connection.disconnect();
                } catch (MalformedURLException e)
                {
                    Log.e("Connection", "Malform URL : " + e.getMessage());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    Log.e("Connection", "Unknown Exception : " + e.getMessage());
                }
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page, menu);
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
