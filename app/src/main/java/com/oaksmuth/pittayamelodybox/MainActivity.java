package com.oaksmuth.pittayamelodybox;
    /*
    * Connect this to Arduino ... is a smart way to rotate motor
    *
    */

import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private PoemProvider data;
    private TextView poemText;
    private Button randButton;
    private boolean longRunPlay = false;
    private boolean musicState = true;
    private Menu menu;
    private MediaPlayer mPlayer;
    private int playing;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = MediaPlayer.create(this, R.raw.kisstherain);
        playing = R.raw.kisstherain;
        final EditText searchTxt = (EditText) findViewById(R.id.searchTxt);
        poemText = (TextView) findViewById(R.id.poemText);
        Button searchButton = (Button) findViewById(R.id.searchBtn);
        data = Splash.data;
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (musicState && !mPlayer.isPlaying()) {
                            mPlayer.reset();
                            mPlayer.release();
                            mPlayer = null; //with release() and = null mPlayer will be nullified
                            if (playing == R.raw.canonind) {
                                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kisstherain);
                                playing = R.raw.kisstherain;
                            } else if (playing == R.raw.kisstherain) {
                                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alwayswithme);
                                playing = R.raw.alwayswithme;
                            } else if (playing == R.raw.alwayswithme){
                                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.canonind);
                                playing = R.raw.canonind;
                            }
                        mPlayer.start();
                        }

                    if (longRunPlay && !tts.isSpeaking()) {
                        int pos = (int) (Math.random() * 2000);
                        speak(pos);
                    }
                }
            }
        }).start();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(searchTxt.getText().toString().trim());
                    speak(id);
                    longRunPlay = false;
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please insert a valid number " + searchTxt.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        randButton = (Button) findViewById(R.id.randomButton);
        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) (Math.random() * 2000);
                speak(pos);
                longRunPlay = false;
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
                tts.speak("Hello สวัสดี", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        ListView listView = (ListView) findViewById(R.id.PoemlistView);
        ArrayAdapter<Poem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data.poems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                longRunPlay = false;
                speak(position);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_musicstate).setTitle("Music : On");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_longrunplay:
                longRunPlay = !longRunPlay;
                menu.findItem(R.id.action_longrunplay).setTitle("Long Run Play : " + (longRunPlay ? "On" : "Off"));
                return true;
            case R.id.action_musicstate:
                musicState = !musicState;
                menu.findItem(R.id.action_musicstate).setTitle("Music : " + (musicState ? "On" : "Off"));
                if (!musicState)
                    mPlayer.pause();
                else
                    mPlayer.start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void speak(int id) {
        if (id >= 0 && id <= 1999) {
            synchronized (tts) {
                tts.speak(data.poems.get(id).toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
            final int idx = id;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    poemText.setText(data.poems.get(idx).enteredString());
                    Toast.makeText(getApplicationContext(), "ID : " + idx , Toast.LENGTH_SHORT).show();
                }
            });
        } else
            return;
    }


    @Override
    protected void onPause() {
        super.onPause();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
