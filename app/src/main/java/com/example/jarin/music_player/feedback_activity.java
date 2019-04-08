package com.example.jarin.music_player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class feedback_activity extends AppCompatActivity implements View.OnClickListener {


    private Button sendButton, clearButton;
    private EditText nameEditText, messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_activity);


        sendButton = (Button) findViewById(R.id.sendButtonId);
        clearButton = (Button) findViewById(R.id.clearButtonId);

        nameEditText = (EditText) findViewById(R.id.nameEdiytextId);
        messageEditText = (EditText) findViewById(R.id.messageEdiytextId);

        sendButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        try {
            String name = nameEditText.getText().toString();
            String message = messageEditText.getText().toString();


            if (v.getId() == R.id.sendButtonId) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jtasnim743@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from app");
                intent.putExtra(Intent.EXTRA_TEXT, "Name: " + name + "\n  Message:" + message);
                startActivity(Intent.createChooser(intent, "Feedback with"));

            } else if (v.getId() == R.id.clearButtonId) {

                nameEditText.setText("");
                messageEditText.setText("");

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Exception :"+e,Toast.LENGTH_SHORT).show();
        }


    }
}
