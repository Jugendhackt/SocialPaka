package me.simonbohnen.socialpaka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.samples.vision.ocrreader.R;

public class ErrorActivity extends AppCompatActivity {

    private Button error_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        error_button = (Button) findViewById(R.id.error_button);

        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
