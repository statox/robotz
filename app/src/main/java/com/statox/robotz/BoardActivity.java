package com.statox.robotz;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Vector;


public class BoardActivity extends ActionBarActivity {
    public Astronaut astronaut;
    public Robots robots;
    public Vector<ImageView> wreckages;

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

    /* entries in the menu */
    private int ID_MENU_RANDOM_TL = 1;
    private int ID_MENU_SAFE_TL = 2;
    private int ID_MENU_WAIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        /* get the level */
        Intent intent = getIntent();
        level = intent.getIntExtra(NextLevelActivity.MSG_LVL, 1);

        /* initialise the wreckage vector */
        wreckages = new Vector<ImageView>();

        /* first get the layout from the xml */
        LayoutInflater inflater;
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (RelativeLayout) inflater.inflate(R.layout.activity_board , null);

        /* get the size of the screen */
        /* TODO: replace it with the size of the layout */
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        /* creation of the astronaut */
        astronaut = new Astronaut(this, screenSize);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);

        menu.add(Menu.NONE, ID_MENU_RANDOM_TL, Menu.NONE, R.string.random_teleport);
        menu.add(Menu.NONE, ID_MENU_SAFE_TL, Menu.NONE, R.string.safe_teleport_1);
        menu.add(Menu.NONE, ID_MENU_WAIT, Menu.NONE, R.string.wait);

        return true;
    }

    /* execute the actions selected in the menu
     *  - Random Teleportation
     *  - Safe Teleportation
     *  - Wait for the bots until the end of the level
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case 1:
                astronaut.randomTeleport();
                break;
            case 2:
                astronaut.safeTeleport(robots, wreckages);
                break;
            case 3:
                while (checkEndOfLevel() == 0) {
                    robots.turn(astronaut, this, layout, wreckages);
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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

                robots.turn(astronaut, this, layout, wreckages);
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
            return -1;
        } else if (robots.list.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_SHORT).show();
            // launch the NextLevelActivity
            Intent intent = new Intent(this, NextLevelActivity.class);
            intent.putExtra(MSG_LVL, level);
            startActivity(intent);
            return 1;
        }
        return 0;
    }
}
