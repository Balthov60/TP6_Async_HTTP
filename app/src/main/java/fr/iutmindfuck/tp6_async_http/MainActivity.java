package fr.iutmindfuck.tp6_async_http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private WebView webview;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());

        progressBar = findViewById(R.id.progressBar);
    }

    public void updateDayAndHour(View view) {
        new MyAsyncTask(this).execute(RequestType.TIME);
    }

    public void launchGoogle(View view)
    {
        webview.loadUrl("http://www.google.fr/");
    }

    public void displayHTML(View view)
    {
        String html = "<html><body><b> Ceci est un texte au format HTML </b></br>qui s’affiche très simplement</body></html>";
        webview.loadData(html,"text/html; charset=utf-8","UTF-8");
    }

    public void displayMeteo(View view)
    {
        webview.loadUrl("http://api.meteorologic.net/forecarss?p=Bourg-en-Bresse");
    }

    public void displayMeteoData(View view)
    {
        new MyAsyncTask(this).execute(RequestType.WEATHER);
    }

    @Override
    public void onTaskStart()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onTaskCompleted(RequestType type, String data)
    {
        switch (type)
        {
            case TIME:
                ((TextView)findViewById(R.id.time)).setText(data);
                break;

            case WEATHER:
                webview.loadData(data,"text/html; charset=utf-8","UTF-8");
                break;
        }

        progressBar.setVisibility(View.GONE);
    }
}