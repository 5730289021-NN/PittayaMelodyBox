package com.oaksmuth.pittayamelodybox;
    /*
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech ttsThai;
    private PoemProvider data;
    private TextView poemText;
    private Button randButton;
    private short longRunPlay = 0; //0 : off, 1 : respectively, 2 : randomly
    private boolean musicState = true;
    private Menu menu;
    private MediaPlayer mPlayer;
    private int playing;
    private short languageState;
    private int poemAt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mPlayer = MediaPlayer.create(this, R.raw.canonind);
        playing = R.raw.canonind;
        final EditText searchTxt = (EditText) findViewById(R.id.playEditText);
        poemText = (TextView) findViewById(R.id.poemText);
        Button searchButton = (Button) findViewById(R.id.playButton);
        data = Splash.data;
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });
        languageState = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (musicState && !mPlayer.isPlaying()) {
                            mPlayer.reset();
                            mPlayer.release();
                            mPlayer = null; //with release() and = null mPlayer will be nullified
                        if (playing == R.raw.canonind) {
                            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alwayswithme);
                            playing = R.raw.alwayswithme;
                        } else if (playing == R.raw.alwayswithme) {
                            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kisstherain);
                            playing = R.raw.kisstherain;
                            } else if (playing == R.raw.kisstherain) {
                            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.myheartwillgoon);
                            playing = R.raw.myheartwillgoon;
                        } else if (playing == R.raw.myheartwillgoon) {
                            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.peergyntsuite);
                            playing = R.raw.peergyntsuite;
                        } else if (playing == R.raw.peergyntsuite) {
                            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.canonind);
                            playing = R.raw.canonind;
                        }
                        mPlayer.start();
                        }
                    if (!ttsThai.isSpeaking()) {
                        switch (longRunPlay) {
                            case 1: //Respectively
                            {
                                speak(++poemAt);
                                //Toast.makeText(MainActivity.this, "Respect", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 2: //Randomly
                            {
                                int pos = (int) (Math.random() * 2000);
                                //Toast.makeText(MainActivity.this, "Random", Toast.LENGTH_SHORT).show();
                                speak(pos);
                                break;
                            }
                        }
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
                    /*
                        longRunPlay = 0;
                        menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Off");
                    */
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
                /*
                    longRunPlay = 0;
                    menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Off");
                */
            }
        });

        ttsThai = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsThai.setLanguage(new Locale("th"));
                ttsThai.speak("Hello สวัสดี", TextToSpeech.QUEUE_FLUSH, null);
            }
        }/*,"com.vajatts.nok"*/);

        ListView listView = (ListView) findViewById(R.id.poemListView);
        ArrayAdapter<Poem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data.poems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                    longRunPlay = 0;
                    menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Off");
                */
                speak(position);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_musicState).setTitle("Music : On");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_longRunPlay:
                longRunPlay++;
                if (longRunPlay == 1) {
                    menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Respectively");
                } else if (longRunPlay == 2) {
                    menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Randomly");
                } else//longRunPlay = 3 or unknown case
                {
                    longRunPlay = 0;
                    menu.findItem(R.id.action_longRunPlay).setTitle("Long Run Play : Off");
                }
                return true;
            case R.id.action_musicState:
                musicState = !musicState;
                menu.findItem(R.id.action_musicState).setTitle("Music : " + (musicState ? "On" : "Off"));
                if (!musicState)
                    mPlayer.pause();
                else
                    mPlayer.start();
                return true;
            case R.id.action_language:
                languageState++;
                if(languageState == 3) languageState = 0;
                if(languageState == 0)
                    menu.findItem(R.id.action_language).setTitle("Language : Thai|English");
                else if(languageState == 1)
                    menu.findItem(R.id.action_language).setTitle("Language : Thai");
                else if(languageState == 2)
                    menu.findItem(R.id.action_language).setTitle("Language : English");
        }
        return super.onOptionsItemSelected(item);
    }

    private void speak(final int id) {
        if (id >= 0 && id <= 1999) {
            String toSpeak = "";
            switch (languageState){
                case 0:
                    toSpeak = data.poems.get(id).AllString();
                    break;
                case 1:
                    toSpeak = data.poems.get(id).toString();
                    break;
                case 2:
                    toSpeak = data.poems.get(id).EnglishString();
                    break;
            }
            ttsThai.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            synchronized (ttsThai) {
                poemAt = id;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    poemText.setText(data.poems.get(id).enteredString());
                    Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ttsThai.stop();
        ttsThai.shutdown();
        ttsThai = null;
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
