package com.example.jarin.music_player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import static android.graphics.PorterDuff.Mode.SRC_IN;

public class Interface_activity extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songtext;
    SeekBar song_seekbar;
    static MediaPlayer mymediaPlayer;
    int position;
    String sname;
    ArrayList<File>mysong;
    Thread updateseekbar;




    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_activity);

        btn_next=(Button)findViewById(R.id.next);
        btn_pause=(Button)findViewById(R.id.pause);
        btn_previous=(Button)findViewById(R.id.prious);

        songtext=(TextView)findViewById(R.id.songtext);
        song_seekbar=(SeekBar)findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now play");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateseekbar=new Thread(){
            @Override
            public void run() {
                int totalduration=mymediaPlayer.getDuration();
                int currentposition=0;

                while(currentposition<totalduration){
                    try{
                        sleep(500);
                        currentposition=mymediaPlayer.getCurrentPosition();
                        song_seekbar.setProgress(currentposition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mymediaPlayer!=null){
            mymediaPlayer.stop();
            mymediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysong=(ArrayList)bundle.getParcelableArrayList("songs");

        sname=mysong.get(position).getName().toString();

        String songName=i.getStringExtra("songname");

        songtext.setText(songName);
        songtext.setSelected(true);


        position=bundle.getInt("pos");

        Uri u=Uri.parse(mysong.get(position).toString());
        mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);

        mymediaPlayer.start();
        song_seekbar.setMax(mymediaPlayer.getDuration());
        updateseekbar.start();
        song_seekbar.getProgressDrawable().
                setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        song_seekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),SRC_IN);

        song_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song_seekbar.setMax(mymediaPlayer.getDuration());

                if(mymediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.play_icon);
                    mymediaPlayer.pause();

                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.pause_icon);
                    mymediaPlayer.start();
                }
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaPlayer.stop();
                mymediaPlayer.release();
                position = ((position + 1) % mysong.size());

                Uri u = Uri.parse(mysong.get(position).toString());

                mymediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname=mysong.get(position).getName().toString();
                songtext.setText(sname);
                mymediaPlayer.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaPlayer.stop();
                mymediaPlayer.release();
                position=(((position-1)<0)?(mysong.size()-1):(position-1));
                Uri u=Uri.parse(mysong.get(position).toString());
                mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mysong.get(position).getName().toString();
                songtext.setText(sname);
                mymediaPlayer.start();

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
