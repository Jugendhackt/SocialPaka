package me.simonbohnen.socialpaka;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nbeye on 09. Sep. 2017.
 */

public class SearchIntentService extends IntentService {
    private static final String TAG = SearchIntentService.class.getSimpleName();

    private static final String SERVER_URL_SEARCH_USER = "http://192.168.0.100:9253/user/skill/search?skill=";
    public static final String PENDING_RESULT_EXTRA = "PENDING_RESULT_EXTRA";
    public static final String ID_EXTRA_INPUT = "ID_EXTRA_DATA";
    public static final String ID_EXTRA_JSON_TEXT = "ID_EXTRA_JSON_TEXT";

    public static final int ERROR_CODE = 0;
    public static final int SUCCESS_CODE = 1;


    public SearchIntentService() {
        super(TAG);
    }

    public static void startService(Activity context, int requestCode, String input) {
        Log.d(TAG, "startService");
        PendingIntent pendingResult = context.createPendingResult(
                requestCode, new Intent(), 0);

        Intent intent = new Intent(context, SearchIntentService.class);
        intent.putExtra(SearchIntentService.PENDING_RESULT_EXTRA, pendingResult);
        intent.putExtra(SearchIntentService.ID_EXTRA_INPUT, input);

        context.startService(intent);
    }

    protected void onHandleIntent(Intent intent) {
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        final String input = intent.getStringExtra(ID_EXTRA_INPUT);

        try {
            InputStream inputStream = getStream(SERVER_URL_SEARCH_USER + input);
            if (inputStream != null) {
                String text = readStream(inputStream);
                if (text != null) {
                    Intent data = new Intent();

                    data.putExtra(ID_EXTRA_JSON_TEXT, text);

                    reply.send(this, SUCCESS_CODE, data);
                }
            } else {
                reply.send(ERROR_CODE);
            }
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    // See: https://stackoverflow.com/questions/5027395/android-download-json-file-from-url
    /**
     * Start a download from the internet
     * @param urlPath URL to download
     * @return InputStream
     */
    private InputStream getStream(String urlPath) {
        Log.d(TAG, "getStream");
        try {
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.connect();
            return urlConnection.getInputStream();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    /**
     * Reads Text of an InputString
     * @param inputStream InputString to read from
     * @return Text read
     */
    private String readStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read + "\n");
            }

            return stringBuilder.toString();
        } catch (IOException exception) {
            return null;
        }
    }
}
