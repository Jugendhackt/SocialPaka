package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountDetailActivity extends AppCompatActivity {
    private static final String USER_NAME_ID = "USER_NAME_ID";
    private static final int REQUEST_CODE_DOWNLOAD = 5;

    private Toolbar toolbar;
    private String name;
    private TextView textView_name;
    private TextView textView_mail;
    private TextView textView_tel;
    private TextView textView_bday;
    private Button slackbutton;

    private ProgressBar progressBar;
    private TextView textView_download;


    public static void putName(String name, Intent intent) {
        intent.putExtra(USER_NAME_ID, name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_activity);

        this.name = getIntent().getStringExtra(USER_NAME_ID);

        toolbar = (Toolbar) findViewById(R.id.account_detail_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar t = getSupportActionBar();
        if(t != null) {
            t.setDisplayHomeAsUpEnabled(true);
            t.setTitle(name);
        }

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_mail = (TextView) findViewById(R.id.textView_e_mail);
        textView_tel = (TextView) findViewById(R.id.textView_phone);
        textView_bday = (TextView) findViewById(R.id.textView_birthday);

        textView_download = (TextView) findViewById(R.id.account_detail_download);
        progressBar = (ProgressBar) findViewById(R.id.account_detail_progressBar);

         slackbutton = (Button) findViewById(R.id.slackbutton);
        slackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://10.172.14.70:9253/user?name=" + name.toLowerCase();
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String email = new JSONObject(response).getString("email");
                            String uri = "slack://user?team=T02E176Q1&id=" + OcrCaptureActivity.emailToUserid.get(email);
                            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", "That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DownloadIntentService.startService(this, REQUEST_CODE_DOWNLOAD);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DOWNLOAD) {
            if (resultCode == DownloadIntentService.SUCCESS_CODE) {
                String text = data.getStringExtra(DownloadIntentService.ID_EXTRA_DATA);

                try {
                    JSONObject jsonObject = new JSONObject(text);

                    String fullName = jsonObject.getString("fullName");
                    String bday = jsonObject.getString("bday");
                    String tel = jsonObject.getString("phone");
                    String mail = jsonObject.getString("mail");

                    textView_name.setText(fullName);
                    textView_tel.setText(tel);
                    textView_mail.setText(mail);
                    textView_bday.setText(bday);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == DownloadIntentService.ERROR_CODE) {
                Toast.makeText(this, "Keine Verbindung möglich.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
