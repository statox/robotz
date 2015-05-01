package com.statox.robotz.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.statox.robotz.R;
import com.statox.robotz.activities.BoardActivity;


public class LostActivity extends Activity {
    public final static String MSG_LVL = "com.statox.robotz.playAgain.level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        /* get the score */
        Intent intent = getIntent();
        int score = intent.getIntExtra(BoardActivity.MSG_SCORE, 1);

        /* getting the previous highscore */
        SharedPreferences prefs = this.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        int highscore = prefs.getInt("highscore", 0); //0 is the default value

        /* String to display the results */
        String scoreString = "Score: " + score;
        String highScoreString = "Highscore: " + highscore;

        /* save the score if highscore */
        if (score > highscore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.commit();

            scoreString= "New High score !";
            highScoreString = "Previous: " + highscore + " New: " + score;
        }

        /* output the results */
        ((TextView) findViewById(R.id.textViewScore)).setText(scoreString);
        ((TextView) findViewById(R.id.textViewHighScore)).setText(highScoreString);

    }

    /*
     * Start a new game
     * Launch the Board activity (no need to give a level since the default value is 1)
     */
    public void playAgain (View view) {
        // launch the BoardActivity
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }
}
