package com.statox.robotz;

import android.animation.ObjectAnimator;
import android.content.Context;
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

    /* how much images should be deplaced */
    private int STEP = 50;

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
        for (int i=0; i<20; ++i) {
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
                /* now let the robots chase the astronaut */
                robots.moveRobots(astronaut);
                /* after the deplacements check if the robots collides with each others */
                robots.checkCollisions(this, layout, wreckages);
                setContentView(layout);
                break;
        }

        return super.onTouchEvent(event);
    }
}
