package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONObject;

public class AccountDetailActivity extends AppCompatActivity {
    public static final String ID_EXTRA_DOWNLOADINFO = "ID_EXTRA_DOWNLOADINFO";
    public static final String USER_NAME_ID = "USER_NAME_ID";
    public static final String JSON_DATA_ID = "JSON_DATA_ID";

    public static JSONObject user;
    public static String userID;

    private Toolbar toolbar;
    private String name;
    private TextView textView_name;
    private TextView textView_mail;
    private TextView textView_tel;
    private TextView textView_bday;
    private TextView textView_skills;
    private Button slackbutton;
    private Button contactButton;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_activity);

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
        textView_skills = (TextView) findViewById(R.id.textView_skills);

        showInformation();

        slackbutton = (Button) findViewById(R.id.slackbutton);
        slackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "slack://user?team=T02E176Q1&id=" + userID;
                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
            }
        });

        contactButton = (Button) findViewById(R.id.button_add_contact);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                /*
                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, downloadInfo.getName())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, downloadInfo.getPhone())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL, downloadInfo.getMail()); */

                startActivityForResult(contactIntent, 1);
            }
        });
    }


    protected void showInformation() {
        /*
        name = downloadInfo.getInputName();
        actionBar.setTitle(downloadInfo.getInputName());
        textView_name.setText(downloadInfo.getName());
        textView_bday.setText(downloadInfo.getBday());
        textView_tel.setText(downloadInfo.getPhone());
        textView_mail.setText(downloadInfo.getMail());
        if(downloadInfo.skills != null) {
            textView_skills.setText(String.format(getString(R.string.skills_template), downloadInfo.getSkills()));
        }
        */
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
