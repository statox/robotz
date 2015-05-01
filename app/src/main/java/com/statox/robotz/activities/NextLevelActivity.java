package com.statox.robotz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.statox.robotz.R;
import com.statox.robotz.activities.BoardActivity;


public class NextLevelActivity extends Activity {
    public final static String MSG_LVL = "com.statox.robotz.nextlevel.level";
    public final static String MSG_TELEPORTS = "com.statox.robotz.nextlevel.teleports";
    public final static String MSG_SCORE = "com.statox.robotz.nextlevel.score";
    private int level;
    private int nbSafeTeleports;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_level);

        Intent intent = getIntent();
        level = intent.getIntExtra(BoardActivity.MSG_LVL, 1);
        nbSafeTeleports = intent.getIntExtra(BoardActivity.MSG_TELEPORTS, 10);
        score = intent.getIntExtra(BoardActivity.MSG_SCORE, 0);
        ((TextView) findViewById(R.id.textViewNextLevel)).setText("Score: " + score);
    }

    public void goToNextLevel(View view) {
        // launch the BoardActivity with the infos from the previous level
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(MSG_LVL, level+1);
        intent.putExtra(MSG_TELEPORTS, nbSafeTeleports+1);
        intent.putExtra(MSG_SCORE, score);
        startActivity(intent);
    }
}
