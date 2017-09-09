package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.samples.vision.ocrreader.R;

public class AccountDetailActivity extends AppCompatActivity {
    private static final String USER_NAME_ID = "USER_NAME_ID";
    private Toolbar toolbar;
    private String name;


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
    }

}
