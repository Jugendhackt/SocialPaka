package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toolbar;

import com.google.android.gms.samples.vision.ocrreader.R;

public class AccountDetailActiviy extends Activity {
    private static final String USER_NAME_ID = "USER_NAME_ID";
    private Toolbar toolbar;
    private String name;


    public static void putName(String name, Intent intent) {
        intent.putExtra(USER_NAME_ID, name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_activiy);

        this.name = getIntent().getStringExtra(USER_NAME_ID);

        toolbar = (Toolbar) findViewById(R.id.account_detail_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(name);
    }

}
