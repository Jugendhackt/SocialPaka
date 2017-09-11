package me.simonbohnen.socialpaka;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.samples.vision.ocrreader.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements PasswordDialogFragment.DialogListener {
    private static String inited = "inited";
    private MessageDigest md;
    private String passwordHashed = "ca5383225e1850560cf91c5282d197c74e729022f08c50f108c0c90caad1ea36";
    private String enteredPasswort;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        sp = getSharedPreferences("sp", 0);
        if(!sp.getBoolean(inited, false)) {
            DialogFragment dialog = new PasswordDialogFragment();
            dialog.show(getFragmentManager(), "PasswordDialogFragment");
        }

        Button scanName = (Button) findViewById(R.id.scanName);
        scanName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OcrCaptureActivity.class));
            }
        });
    }

    @Override
    public void onDialogPositiveClick(String passwort) {
        try {
            md.update(passwort.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] digest = md.digest();
        if(String.format("%064x", new java.math.BigInteger(1, digest)).equals(passwordHashed)) {
            enteredPasswort = passwort;
            sp.edit().putBoolean(inited, true).apply();
        } else {
            Toast.makeText(this, "Falsches Passwort", Toast.LENGTH_LONG).show();
            DialogFragment dialog = new PasswordDialogFragment();
            dialog.show(getFragmentManager(), "PasswordDialogFragment");
        }
    }
}
