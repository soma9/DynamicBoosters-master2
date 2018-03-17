package com.example.hp.dynamicboosters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

private TextView showVoiceTex;
private final  int CODE_SPEECH_OUPU=143;
private ConstraintLayout constraintLayout;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

showVoiceTex = (TextView) findViewById(R.id.showVoice);
constraintLayout = (ConstraintLayout)findViewById(R.id.activity_main);
        tts = new TextToSpeech(this, this);
//buton click for both TTS and STT
constraintLayout.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        btnToOpenMic();
        return false;
    }
});
    }
    private void btnToOpenMic()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speek now......");
        try{
            startActivityForResult(intent,CODE_SPEECH_OUPU);
        }
        catch (ActivityNotFoundException e)
        {


        }

    }
    //speech to text

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CODE_SPEECH_OUPU:{
                if(resultCode == RESULT_OK && null!= data){
                    ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    showVoiceTex.setText(voiceInText.get(0));
                }
              break;
            }

        }
        String check = showVoiceTex.getText().toString();
        if (check.equals("1234"))
        {
            Toast.makeText(getApplicationContext(), "Successfully logged in",
                    Toast.LENGTH_SHORT).show();
            speakOut();
        }
        else{
            Toast.makeText(getApplicationContext(), "Sorry",
                    Toast.LENGTH_SHORT).show();
            reject();

        }
    }
//for text o speech destroy garbage
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
//init process for text to speech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
               // btnSpeak.setEnabled(true);
                initialCall();


            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    // text to speech correct pw
    private void speakOut() {

        String text = "Welcome Its a pleasure to help you!! ";

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    //text to speech wrong pw
    private void reject()
    {

        String mistake = "Oops , Sorry Please try again ";
        tts.speak(mistake,TextToSpeech.QUEUE_FLUSH,null);
    }
    private void initialCall()
    {
     String msg = "Hello, Please touch the screen to login the app " ;
     tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null);

    }
}
