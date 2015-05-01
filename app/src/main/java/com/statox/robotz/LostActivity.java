package com.statox.robotz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


public class LostActivity extends Activity {
    public final static String MSG_LVL = "com.statox.robotz.playAgain.level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        /* get the score */
        Intent intent = getIntent();
        int score = intent.getIntExtra(BoardActivity.MSG_SCORE, 1);
        ((TextView) findViewById(R.id.textViewScore)).setText("Score: " + score);
        
    }

    /* Start a new game
     * Launch the Board activity (no need to give a level since the default value is 1)
     */
    public void playAgain (View view) {
        // launch the BoardActivity
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }
}
