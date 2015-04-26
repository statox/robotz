package com.statox.robotz;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageView;

/**
 * Created by adrien on 26/04/15.
 *
 * The robot is shown on screen as an ImageView.
 * It has the ability to move.
 */
public class Robot extends ImageView {
    private int STEP = 24;
    private boolean toRemove;

    /* create a robot with the image and a random position on screen */
    /* TODO: Get the limit of the screen position */
    /* TODO: Check that the position is unique */
    public Robot(Context context, Point screenSize ) {
        super(context);
        toRemove = false;
        setImageResource(R.drawable.robot);
        double distanceToCenter = 0;
        do {
            setY((float) (Math.random() * screenSize.y));
            setX((float) (Math.random() * screenSize.x));
            distanceToCenter = Math.sqrt( (Math.pow((getX() - (screenSize.x/2) ), 2)) + (Math.pow((getY() - (screenSize.y/2) ), 2)) );
        }while (distanceToCenter < 50);
    }

    public boolean isToRemove() { return toRemove; }
    public void setToRemove(boolean t) { toRemove = t; }

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
