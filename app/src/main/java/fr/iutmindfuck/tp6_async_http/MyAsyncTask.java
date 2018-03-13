package fr.iutmindfuck.tp6_async_http;

import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyAsyncTask extends AsyncTask<Object, Void, String> {

    private View viewToUpdate;
    private ProgressBar progressBar;

    MyAsyncTask(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Object[] objects) {
        switch ((RequestType) objects[0])
        {
            case TIME:
                viewToUpdate = (TextView)objects[1];
                return getTime();

            case WEATHER:
                viewToUpdate = (WebView)objects[1];
                return getWeather();

        }
        return null;
    }

    private String getTime()
    {
        try
        {
            URL url = new URL("http://10.0.2.2:3402/time");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return br.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "error";
    }

    private String getWeather()
    {
        try
        {
            URL url = new URL("http://api.meteorologic.net/forecarss?p=Bourg-en-Bresse");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                String result = "";
                boolean keepNextLine = false;

                while((line = br.readLine()) != null)
                {
                    if (keepNextLine && line.contains("</description>"))
                    {
                        return result;
                    }
                    if (line.contains("<![CDATA["))
                    {
                        keepNextLine = true;
                    }
                    else if (keepNextLine)
                    {
                        result = result.concat(line);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "error";
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

        if (viewToUpdate instanceof TextView)
        {
            ((TextView)viewToUpdate).setText(data);
        }
        else
        {
            ((WebView)viewToUpdate).loadData(data,"text/html; charset=utf-8","UTF-8");
        }

        progressBar.setVisibility(View.GONE);
    }
}
