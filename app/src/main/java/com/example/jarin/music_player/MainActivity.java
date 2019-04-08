package com.example.jarin.music_player;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Handler;



public class MainActivity extends AppCompatActivity {

    SearchView mysearchview;

    ListView my_listview_music;

    String[] items;
    ArrayAdapter<String>adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mysearchview=(SearchView)findViewById(R.id.searchViewid);

        my_listview_music = (ListView) findViewById(R.id.music_listview);

        runtime_permission();
    }

    public void runtime_permission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                           display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.cancelPermissionRequest();
                    }
                }).check();
    }

    public ArrayList <File> findSong(File file) {
        ArrayList <File> arrayList = new ArrayList <>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {

                arrayList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")) {
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
    }

    void display() {
        final ArrayList <File> mysong = findSong(Environment.getExternalStorageDirectory());
        items = new String[mysong.size()];
        for (int i = 0; i < mysong.size(); i++) {


            items[i] = mysong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        final ArrayAdapter <String> adapter;
        adapter = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, items);
        my_listview_music.setAdapter(adapter);

        mysearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


        my_listview_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

                String songname=my_listview_music.getItemAtPosition(position).toString();


                startActivity(new Intent(getApplicationContext(),Interface_activity.class)
                        .putExtra("songs",mysong).putExtra("songname",songname).putExtra("pos",position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.shareId){

            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            String subject ="Music_player";
            String body = "This is a very good music app.com.example.jarin.music_player";

            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(intent,"share with"));



        }

        else if(item.getItemId()==R.id.feedbackId){

            Intent intent=new Intent(getApplicationContext(),feedback_activity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}