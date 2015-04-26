package com.statox.robotz;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by adrien on 26/04/15.
 *
 * This class contains the all of the robots on the board and call the methods to move them,
 * check for their collision and destroy them when necessary
 */

public class Robots {
    public Vector<Robot> list;
    public Robots () { list = new Vector<Robot>(); }

    /* this method correspon to the turn of the computer:
     * we deplace the robots and check for the collisions
     */
    public void turn (Astronaut astronaut, Context context, RelativeLayout layout, Vector<ImageView> wreckages) {
        /* let the robots chase the astronaut */
        moveRobots(astronaut);
        /* after the deplacements check if the robots collides with each others */
        checkCollisions(context, layout, wreckages);
    }

    /* this method moves each robot to the astronaut */
    public void moveRobots(Astronaut astronaut) {
        /* get the position of the astonaut */
        float astroX = astronaut.getX();
        float astroY = astronaut.getY();

        for (Robot r : list) {
            /* get the position of the current robot */
            float robotX = r.getX();
            float robotY = r.getY();

            /* deplace the current robot */
            if (robotX < astroX) {
                r.moveR();
            } else if (robotX > astroX) {
                r.moveL();
            }

            if (robotY < astroY) {
                r.moveD();
            } else if (robotY > astroY) {
                r.moveU();
            }
        }
    }

    /* This method check for each robot if it is colliding with an other robot or with a wreckage
     * from a previous collision.
     * If the robot is in collision, it is "destroyed" (removed from the list and visibility set to gone)
     * and a new wreckage is placed if necessary.
     */
    public boolean checkCollisions(Context context, RelativeLayout layout, Vector<ImageView> wreckages) {
        boolean collision = false;
        for (Robot r1: list) {
            Log.d("robotz.log", "robot1: " + r1.toString());
            Rect rc1 = new Rect();
            Rect rc2 = new Rect();

            /* get the collision boxe for the current robot */
            r1.getHitRect(rc1);

            /* first check collisions with existing wreckages */
            for (ImageView r2 : wreckages) {
                /* get the collision box for the wreckage */
                r2.getHitRect(rc2);

                if (Rect.intersects(rc1, rc2)) {
                    Log.d("robotz.log", "collision with wreckage " + rc1.toString() + " - " + rc2.toString());
                    collision = true;

                    /* mark the robot to remove */
                    r1.setToRemove(true);
                }
            }

            /* if the robot didnt collide with a wreckage check if it collides with another robot */
            if (!collision) {
                for (Robot r2 : list) {
                    Log.d("robotz.log", "robot2: " + r2.toString());

                    if (!r1.equals(r2)) {
                        /* get the collision box for the compared robot */
                        r2.getHitRect(rc2);

                        if (Rect.intersects(rc1, rc2)) {
                            Log.d("robotz.log", "collision with robot " + rc1.toString() + " - " + rc2.toString());
                            collision = true;

                            /* add a new wreckage at the place of the collision */
                            ImageView wreckage = new ImageView(context);
                            wreckage.setImageResource(R.drawable.gear);
                            wreckage.setX((r1.getX() + r2.getX()) / 2);
                            wreckage.setY((r1.getY() + r2.getY()) / 2);
                            layout.addView(wreckage);
                            wreckages.add(wreckage);

                            /* remove the 2 destroyed robots */
                            r1.setToRemove(true);
                            r2.setToRemove(true);
                        }
                    }
                }
            }
        }

        /* if a collision has happened remove the robots concerned */
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            Robot r = (Robot) i.next();
            Log.d("robotz", "LOG: " + ((ImageView) r).toString());
            if (r.isToRemove())
            {
                r.setVisibility(View.GONE);
                i.remove();
            }
        }
        return collision;
    }
}
