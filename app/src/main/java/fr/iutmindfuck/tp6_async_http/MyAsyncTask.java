package fr.iutmindfuck.tp6_async_http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyAsyncTask extends AsyncTask<Object, Void, String> {

    private OnTaskCompleted onTaskCompleted;
    private RequestType requestType;

    MyAsyncTask(OnTaskCompleted onTaskCompleted)
    {
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskCompleted.onTaskStart();
    }

    @Override
    protected String doInBackground(Object[] objects) {
        requestType = (RequestType) objects[0];

        switch (requestType)
        {
            case TIME:
                return getTime();

            case WEATHER:
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

        onTaskCompleted.onTaskCompleted(requestType, data);
    }
}
