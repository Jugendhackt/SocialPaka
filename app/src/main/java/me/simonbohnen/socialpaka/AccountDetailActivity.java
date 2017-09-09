package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    public static final String ID_EXTRA_DOWNLOADINFO = "ID_EXTRA_DOWNLOADINFO";
    public static final String USER_NAME_ID = "USER_NAME_ID";
    public static final String JSON_DATA_ID = "JSON_DATA_ID";

    private Toolbar toolbar;
    private String name;
    private TextView textView_name;
    private TextView textView_mail;
    private TextView textView_tel;
    private TextView textView_bday;
    private Button slackbutton;
    private ActionBar actionBar;

    private DownloadInfo downloadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_activity);

        downloadInfo = (DownloadInfo) getIntent().getSerializableExtra(ID_EXTRA_DOWNLOADINFO);

        toolbar = (Toolbar) findViewById(R.id.account_detail_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_mail = (TextView) findViewById(R.id.textView_e_mail);
        textView_tel = (TextView) findViewById(R.id.textView_phone);
        textView_bday = (TextView) findViewById(R.id.textView_birthday);

        showInformation();

        final RequestQueue queue = Volley.newRequestQueue(this);
        slackbutton = (Button) findViewById(R.id.slackbutton);
        slackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "slack://user?team=T02E176Q1&id=" + OcrCaptureActivity.emailToUserid.get(downloadInfo.getMail());
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
            }
        });
    }


    protected void showInformation() {
        name = downloadInfo.getInputName();
        actionBar.setTitle(downloadInfo.getInputName());
        textView_name.setText(downloadInfo.getName());
        textView_bday.setText(downloadInfo.getBday());
        textView_tel.setText(downloadInfo.getPhone());
        textView_mail.setText(downloadInfo.getMail());
    }
}
