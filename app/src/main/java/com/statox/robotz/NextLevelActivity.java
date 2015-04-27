package com.statox.robotz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class NextLevelActivity extends ActionBarActivity {
    public final static String MSG_LVL = "com.statox.robotz.nextlevel.level";
    private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_level);

        Intent intent = getIntent();
        level = intent.getIntExtra(BoardActivity.MSG_LVL, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_next_level, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToNextLevel(View view) {
        // launch the NextLevelActivity
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(MSG_LVL, level+1);
        startActivity(intent);
    }
}
