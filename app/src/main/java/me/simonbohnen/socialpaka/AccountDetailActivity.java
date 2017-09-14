package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONObject;

public class AccountDetailActivity extends AppCompatActivity {
    public static JSONObject user;
    public static String userID;

    private TextView textView_realname;
    private TextView textView_username;
    private TextView textView_mail;
    private TextView textView_skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.account_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(R.string.nutzerdetails);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textView_realname = (TextView) findViewById(R.id.textView_name);
        textView_username = (TextView) findViewById(R.id.textView_username);
        textView_mail = (TextView) findViewById(R.id.textView_e_mail);
        textView_skills = (TextView) findViewById(R.id.textView_skills);

        showInformation();

        Button slackbutton = (Button) findViewById(R.id.slackbutton);
        slackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "slack://user?team=T02E176Q1&id=" + userID;
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
            }
        });
    }


    private void showInformation() {
        textView_realname.setText(user.optString("real_name"));
        textView_username.setText(user.optString("name"));
        JSONObject profileInfo = user.optJSONObject("profile");
        if(profileInfo != null) {
            textView_mail.setText(profileInfo.optString("email"));
            String skills = profileInfo.optString("title");
            if (skills != null && !skills.isEmpty()) {
                textView_skills.setVisibility(View.VISIBLE);
                textView_skills.setText(skills);
            }
        }
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }*/
}
