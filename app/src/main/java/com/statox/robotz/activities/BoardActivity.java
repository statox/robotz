package com.statox.robotz.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.statox.robotz.items.Astronaut;
import com.statox.robotz.R;
import com.statox.robotz.items.Robot;
import com.statox.robotz.items.Robots;

import java.util.Vector;


public class BoardActivity extends Activity {
    public Astronaut astronaut;
    public Robots robots;
    public Vector<ImageView> wreckages;

    /* number of safe teleportations left
     * We need to send this value and to get it back from the next level activity
     * to keep this number between the levels
     */
    private int nbSafeTeleports;
    public final static String MSG_TELEPORTS = "com.statox.robotz.board.teleports";

    /* score */
    public int score;
    public final static String MSG_SCORE = "com.statox.robotz.board.score";

    /* level */
    private int level;
    public final static String MSG_LVL = "com.statox.robotz.board.level";

    /* number of robots on the screen */
    private int NB_ROBOTS = 10;

    /* size of the screen */
    private Point screenSize;

    /* used for detection of swipes */
    private float x1,x2;
    private float y1,y2;
    static final int MIN_DISTANCE = 150;

    /* layout of the activity */
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        /* get the infos from the previous level */
        Intent intent = getIntent();
        level = intent.getIntExtra(NextLevelActivity.MSG_LVL, 1);
        nbSafeTeleports = intent.getIntExtra(NextLevelActivity.MSG_TELEPORTS, 10);
        score = intent.getIntExtra(NextLevelActivity.MSG_SCORE, 0);

        /* initialise the wreckage vector */
        wreckages = new Vector<ImageView>();

        /* Get the layout from the xml */
        LayoutInflater inflater;
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (RelativeLayout) inflater.inflate(R.layout.activity_board , null);

        /* get the size of the screen */
        /* TODO: replace it with the size of the layout */
        screenSize = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);

        /* attemp to get the real size of the layout */
        /*
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
            }
        });
        */

        /* creation of the astronaut */
        astronaut = new Astronaut(this, screenSize, nbSafeTeleports);
        layout.addView(astronaut);

        /* creation of the container of robots */
        robots = new Robots();

        /* creation of each robots */
        for (int i=0; i<10*level; ++i) {
            Robot newRobot = new Robot(this, screenSize);
            robots.list.add(newRobot);
        }

        /* adding the robots to the layout */
        for (Robot r: robots.list)
            layout.addView(r);

        setContentView(layout);
        ((TextView) findViewById(R.id.textSafeTeleport)).setText(String.valueOf(astronaut.getNbSafeTeleports()));
        ((TextView) findViewById(R.id.textViewCurrentScore)).setText("score: " + score);
    }

    /* here we get the swipe made by the user to deplace the astronaut */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:   // point where the finger touch the screen
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:     // point where the finger leave the screen
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                /* move the astronaut */
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if (x2 < x1)
                        astronaut.moveL();
                    else
                        astronaut.moveR();
                }
                if (Math.abs(deltaY) > MIN_DISTANCE)
                {
                    if (y2 < y1)
                        astronaut.moveU();
                    else
                        astronaut.moveD();
                }

                score += robots.turn(astronaut, this, layout, wreckages);
                ((TextView) findViewById(R.id.textViewCurrentScore)).setText("score: " + score);

                checkEndOfLevel();

                break;
        }
        return super.onTouchEvent(event);
    }

    /* This methods check if the level is ended and who wins.
     *  returns:
     *  0 not ended
     *  1 player win
     *  -1 robots win
     */
    public int checkEndOfLevel () {
        if (astronaut.isInCollision(robots, wreckages)) {
            Toast.makeText(getApplicationContext(), "You loose!", Toast.LENGTH_SHORT).show();
            // launch the LostActivity
            Intent intent = new Intent(this, LostActivity.class);
            intent.putExtra(MSG_SCORE, score);
            startActivity(intent);
            return -1;
        } else if (robots.list.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_SHORT).show();
            // launch the NextLevelActivity
            Intent intent = new Intent(this, NextLevelActivity.class);
            intent.putExtra(MSG_LVL, level);
            intent.putExtra(MSG_SCORE, score);
            intent.putExtra(MSG_TELEPORTS, astronaut.getNbSafeTeleports());
            startActivity(intent);
            return 1;
        }
        return 0;
    }

    /* those methods are activated by the buttons */
    public void randomTeleport(View v) {
        astronaut.randomTeleport();
    }
    public void safeTeleport(View v) {
        if (astronaut.getNbSafeTeleports() > 0) {
            astronaut.safeTeleport(robots, wreckages);
            astronaut.setNbSafeTeleports(astronaut.getNbSafeTeleports() - 1);
            ((TextView) findViewById(R.id.textSafeTeleport)).setText(String.valueOf(astronaut.getNbSafeTeleports()));
        } else {
            Toast.makeText(getApplicationContext(), "no safe teleport left", Toast.LENGTH_SHORT).show();
        }
    }
    public void waitForRobots(View v) {
        while (checkEndOfLevel() == 0) {
            score += robots.turn(astronaut, this, layout, wreckages);
            /* TODO: find a way to refresh the view so the player sees the bots moving */
        }
    }
}
