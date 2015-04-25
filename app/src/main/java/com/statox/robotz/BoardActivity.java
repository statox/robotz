package com.statox.robotz;

import android.animation.ObjectAnimator;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Vector;


public class BoardActivity extends ActionBarActivity {
    public ImageView astronaut;
    public Vector<ImageView> robots;

    /* how much images should be deplaced */
    private int STEP = 50;

    /* used for detection of swipes */
    private float x1,x2;
    private float y1,y2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        astronaut = (ImageView) findViewById(R.id.astronaut);
        robots = new Vector<ImageView>();
        robots.add( (ImageView) findViewById(R.id.robot1));
        robots.add( (ImageView) findViewById(R.id.robot2));
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
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if (x2 < x1)
                        movePlayerL();
                    else
                        movePlayerR();
                }
                if (Math.abs(deltaY) > MIN_DISTANCE)
                {
                    if (y2 < y1)
                        movePlayerU();
                    else
                        movePlayerD();
                }
                /* now let the robots chase the astronaut */
                moveRobots();
                break;
        }

        return super.onTouchEvent(event);
    }

    /* player movement */
    /* left */
    public void movePlayerL(){
        astronaut.setX(astronaut.getX() - STEP);
    }
    /* up */
    public void movePlayerU(){
        astronaut.setY(astronaut.getY() - STEP);
    }
    /* right */
    public void movePlayerR(){
        astronaut.setX(astronaut.getX() + STEP);
    }
    /* down */
    public void movePlayerD(){
        astronaut.setY(astronaut.getY() + STEP);
    }

    /* robots movements */
    public void moveRobots() {
        /* get the position of the astonaut */
        float astroX = astronaut.getX();
        float astroY = astronaut.getY();

        for (ImageView r : robots) {
        /* get the position of the current robot */
            float robotX = r.getX();
            float robotY = r.getY();

        /* deplace the current robot */
            if (robotX < astroX) {
                moveRobotR(r);
            } else if (robotX > astroX) {
                moveRobotL(r);
            }

            if (robotY < astroY) {
                moveRobotD(r);
            } else if (robotY > astroY) {
                moveRobotU(r);
            }
        }

    }
    /* left */
    public void moveRobotL(ImageView robot){
        robot.setX(robot.getX() - STEP);
    }
    /* up */
    public void moveRobotU(ImageView robot){
        robot.setY(robot.getY() - STEP);
    }
    /* right */
    public void moveRobotR(ImageView robot){
        robot.setX(robot.getX() + STEP);
    }
    /* down */
    public void moveRobotD(ImageView robot){
        robot.setY(robot.getY() + STEP);
    }
}
