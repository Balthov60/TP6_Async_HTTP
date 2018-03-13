package fr.iutmindfuck.tp6_async_http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

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
        new MyAsyncTask(progressBar).execute(RequestType.TIME, findViewById(R.id.time));
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
        new MyAsyncTask(progressBar).execute(RequestType.WEATHER, webview);
    }
}