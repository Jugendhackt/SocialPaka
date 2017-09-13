package me.simonbohnen.socialpaka;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements PasswordDialogFragment.DialogListener {
    private static final String inited = "inited";
    private MessageDigest md;
    private SharedPreferences sp;
    private int finishedCount = 0;

    //Maps all names (like "Simon Bohnen") to the corresponding user id
    public static HashMap<String, String> nameToUserID;
    //Maps a user id to the user info (only jh)
    public static HashMap<String, JSONObject> userInfo;
    //A set of all the names of all the users in the jugendHackt channel
    public static Set<String> names;
    //A String of all the user ids in the jugendhackt channel
    private static String jhUserIDs;
    //allUsers of the open knowledge germany team
    private JSONArray allUsers;

    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("sp", 0);
        if(!sp.getBoolean(inited, false)) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            DialogFragment dialog = new PasswordDialogFragment();
            dialog.show(getFragmentManager(), "PasswordDialogFragment");
        } else {
            decryptToken(sp.getString("passwort", ""));
            downloadSlackData();
        }

        Button scanName = (Button) findViewById(R.id.scanName);
        scanName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OcrCaptureActivity.class));
            }
        });
    }

    private void decryptToken(String passwort) {
        try {
            final byte[] tokenEncrypted = {-16, -58, -105, 26, 66, -49, -6, 124, 106, -12, -89, 65, -109, 80, 26, -125, -10, 120, -72, 11, -65, 68, 99, 53, -74, -100, 77, 86, -66, 30, 121, -20, 92, -103, -51, 23, -79, 28, 91, 91, 34, -52, -29, -115, 53, -114, 108, -58, 4, 113, 43, 14, 6, -102, -18, -106, 41, 123, -25, -124, 70, 16, -69, 56, -62, 73, 106, 95, 84, -34, 99, -128, -110, 73, 107, -31, 17, 89, -120, 117};
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeySpec k = new SecretKeySpec((passwort + "0").getBytes(), "DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            token = new String(cipher.doFinal(tokenEncrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogPositiveClick(String passwort) {
        try {
            md.update(passwort.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest();
        byte[] passwordHashed = {-54, 83, -125, 34, 94, 24, 80, 86, 12, -7, 28, 82, -126, -47, -105, -57, 78, 114, -112, 34, -16, -116, 80, -15, 8, -64, -55, 12, -86, -47, -22, 54};
        if(Arrays.equals(digest, passwordHashed)) {
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean(inited, true);
            ed.putString("passwort", passwort);
            ed.apply();
            decryptToken(passwort);
            downloadSlackData();
        } else {
            Toast.makeText(this, "Falsches Passwort", Toast.LENGTH_LONG).show();
            DialogFragment dialog = new PasswordDialogFragment();
            dialog.show(getFragmentManager(), "PasswordDialogFragment");
        }
    }

    private void downloadSlackData() {
        nameToUserID = new HashMap<>();
        userInfo = new HashMap<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://slack.com/api/users.list?token=" + token;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jObject != null) {
                    allUsers = jObject.optJSONArray("members");
                    finishedCount++;
                    convertSlackData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        String url2 ="https://slack.com/api/channels.info?token=" + token + "&channel=C0565C5GT";
        // Request a string response from the provided URL.
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jObject != null) {
                    JSONArray jsa = jObject.optJSONObject("channel").optJSONArray("members");
                    jhUserIDs = jsa.toString();
                    finishedCount++;
                    convertSlackData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest2);
        queue.add(stringRequest);
    }

    private void convertSlackData() {
        if(finishedCount >= 2) {
            for(int i = 0; i < allUsers.length(); ++i) {
                JSONObject user = (JSONObject) allUsers.opt(i);
                String id = user.optString("id");
                if(jhUserIDs.contains(id)) {
                    userInfo.put(user.optString("id"), user);
                }
            }

            for(Map.Entry user : userInfo.entrySet()) {
                nameToUserID.put(((JSONObject) user.getValue()).optString("real_name"), (String) user.getKey());
            }
            names = nameToUserID.keySet();
            findViewById(R.id.loading).setVisibility(View.GONE);
            findViewById(R.id.mainmenu).setVisibility(View.VISIBLE);
        }
    }
}
