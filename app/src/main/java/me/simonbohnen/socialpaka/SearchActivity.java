package me.simonbohnen.socialpaka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final int REQUEST_SEARCH = 5;


    EditText editText;
    Button button_search;
    ListView listview;

    ArrayList<DownloadInfo> downloadInfoArrayList = new ArrayList<DownloadInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Suche nach Mitgliedern");

        editText = (EditText) findViewById(R.id.editText_skill);
        button_search = (Button) findViewById(R.id.button_search);
        listview = (ListView) findViewById(R.id.search_listview);



        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchIntentService.startService(SearchActivity.this, REQUEST_SEARCH, editText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "Result");
        if (requestCode == REQUEST_SEARCH) {
            if (resultCode == SearchIntentService.SUCCESS_CODE) {
                String jsonData = data.getStringExtra(SearchIntentService.ID_EXTRA_JSON_TEXT);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);


                    downloadInfoArrayList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("Json", jsonObject.toString());
                        DownloadInfo downloadInfo = new DownloadInfo(jsonObject.getString("name"), jsonObject.toString());
                        downloadInfoArrayList.add(downloadInfo);
                    }

                    updateListView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateListView() {
        ArrayAdapter<DownloadInfo> adapter = new UsersAdapter(this, downloadInfoArrayList);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class UsersAdapter extends ArrayAdapter<DownloadInfo> {
        public UsersAdapter(Context context, ArrayList<DownloadInfo> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DownloadInfo user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.search_item_name);

            tvName.setText(user.getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchActivity.this, AccountDetailActivity.class);

                    DownloadInfo downloadInfo = getItem(position);
                    intent.putExtra(AccountDetailActivity.ID_EXTRA_DOWNLOADINFO, downloadInfo);

                    startActivity(intent);
                }
            });

            // Return the completed view to render on screen
            return convertView;
        }
    }
}
