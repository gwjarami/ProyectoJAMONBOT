package com.proyecto.app.proyectojamonbot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.JsonElement;
import java.util.Map;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIButton;

public class MainScreen extends AppCompatActivity implements AIListener, View.OnClickListener{

    private AIButton aiMicrofono;
    private TextView tvLeer;
    private TextView tvRespuesta;

    private Button btnVoice;

    private AIService aiService;
    private static final int REQUEST_INTERNET = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        aiMicrofono = (AIButton) findViewById(R.id.micButton);
        tvLeer = (TextView) findViewById(R.id.tvTextoEscuchado);
        tvRespuesta = (TextView) findViewById(R.id.tvTextoRespuesta);

         validateOS();

        final AIConfiguration config = new AIConfiguration("6010bb1d1e4a439fa809884f2573c09e",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiMicrofono.setOnClickListener(this);



    }


    private void validateOS() {
        if (ContextCompat.checkSelfPermission(MainScreen.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_INTERNET);
        }
    }

    @Override
    public void onResult(AIResponse response) {

        Result result = response.getResult();

        // Get parameters
        String parameterString = result.getFulfillment().getSpeech();
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue();
            }
        }

        tvLeer.setText(result.getResolvedQuery());
        tvRespuesta.setText(parameterString);
    }

    @Override
    public void onError(AIError error) {
        tvRespuesta.setText(error.toString());

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.micButton:
                aiService.startListening();
                break;
        }

    }

}
