package com.statox.robotz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class NextLevelActivity extends Activity {
    public final static String MSG_LVL = "com.statox.robotz.nextlevel.level";
    public final static String MSG_TELEPORTS = "com.statox.robotz.nextlevel.teleports";
    private int level;
    private int nbSafeTeleports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_level);

        Intent intent = getIntent();
        level = intent.getIntExtra(BoardActivity.MSG_LVL, 1);
        nbSafeTeleports = intent.getIntExtra(BoardActivity.MSG_TELEPORTS, 10);
        int score = intent.getIntExtra(BoardActivity.MSG_SCORE, 0);
        ((TextView) findViewById(R.id.textViewNextLevel)).setText("Score: " + score);
    }

    public void goToNextLevel(View view) {
        // launch the NextLevelActivity
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(MSG_LVL, level+1);
        intent.putExtra(MSG_TELEPORTS, nbSafeTeleports+1);
        startActivity(intent);
    }
}
