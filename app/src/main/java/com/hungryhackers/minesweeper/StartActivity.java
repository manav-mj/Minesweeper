package com.hungryhackers.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button startGame;
    TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startGame = (Button) findViewById(R.id.startGame);
        highScore = (TextView) findViewById(R.id.highScore);

        SharedPreferences sp = getSharedPreferences("Minesweeper_highScore", MODE_PRIVATE);
        int high_score = sp.getInt("HIGH_SCORE", 0);

        highScore.setText(Integer.toString(high_score));

        startGame.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == startGame){
            Intent i = new Intent();
            i.setClass(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
