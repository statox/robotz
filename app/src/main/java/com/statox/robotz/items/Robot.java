package com.statox.robotz.items;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;

import com.statox.robotz.R;

import java.util.Random;

/**
 * Created by adrien on 26/04/15.
 *
 * The robot is shown on screen as an ImageView.
 * It has the ability to move.
 */
public class Robot extends ImageView {
    private int STEP = 24;
    private boolean toRemove;
    private boolean status; // used to change to drawable used

    /* create a robot with the image and a random position on screen */
    /* TODO: Get the limit of the screen position */
    /* TODO: Check that the position is unique */
    public Robot(Context context, Point screenSize ) {
        super(context);
        toRemove = false;
        double distanceToCenter = 0;

        /* randomly select the initial drawable used */
        status = true;
        if (new Random().nextInt(2) == 0)
            status = !status;
        changeDrawable();

        /* avoid to place a robot on the astronaut */
        do {
            setY((float) (Math.random() * screenSize.y));
            setX((float) (Math.random() * screenSize.x));
            distanceToCenter = Math.sqrt( (Math.pow((getX() - (screenSize.x/2) ), 2)) + (Math.pow((getY() - (screenSize.y/2) ), 2)) );
        }while (distanceToCenter < 500);
    }

    public boolean isToRemove() { return toRemove; }
    public void setToRemove(boolean t) { toRemove = t; }

    /* this methods is called at each move and change the drawable used to by the view
     * which give a mini animation
     */
    public void changeDrawable() {
        Log.d("robotz.log", "===================================" );
        Log.d("robotz.log", this.toString());
        Log.d("robotz.log", "status: " + this.status);
        if (status) {
            Log.d("robotz.log", "\t ROBOT2");
            setImageResource(R.drawable.robot2);
        }else {
            Log.d("robotz.log", "\t ROBOT1");
            setImageResource(R.drawable.robot1);
        }

        this.invalidate();
        status = !status;
        Log.d("robotz.log", "status: " + this.status);
        Log.d("robotz.log", "==================================="  );
    }

    /* movements */
    /* left */
    public void moveL(){
        this.setX(this.getX() - STEP);
    }
    /* up */
    public void moveU(){
        this.setY(this.getY() - STEP);
    }
    /* right */
    public void moveR(){
        this.setX(this.getX() + STEP);
    }
    /* down */
    public void moveD(){
        this.setY(this.getY() + STEP);
    }
}
