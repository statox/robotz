package com.statox.robotz;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.Vector;

/**
 * Created by adrien on 26/04/15.
 *
 * The astronaut is controlled by the player.
 * It is shown on screen as an image view and has the ability to move.
 */
/*TODO: get screen size */
public class Astronaut extends ImageView {
    private int stepV;
    private int stepH;
    private double maxX;
    private double maxY;
    private int nbSafeTeleports;

    public Astronaut(Context context, Point screenSize, int safeTL) {
        super(context);
        setImageResource(R.drawable.astronaut);
        maxX = screenSize.x;
        maxY = screenSize.y;
        setY((float) (maxY/2));
        setX((float) (maxX / 2));
        stepV = 24;
        stepH = 24;
        nbSafeTeleports = safeTL;
    }

    public int getNbSafeTeleports() { return nbSafeTeleports; }
    public void setNbSafeTeleports(int nbSafeTeleports) { this.nbSafeTeleports = nbSafeTeleports; }

    /* player movement */
    /* left */
    public void moveL(){
        if (this.getX() >= stepV)
            this.setX(this.getX() - stepV);
    }
    /* up */
    public void moveU(){
        if (this.getY() >= stepH)
            this.setY(this.getY() - stepH);
    }
    /* right */
    public void moveR() {
        if (this.getX() <= maxX - stepV)
            this.setX(this.getX() + stepV);
    }
    /* down */
    public void moveD() {
        if (this.getY() <= maxY - stepH)
            this.setY(this.getY() + stepH);
    }

    /* teleport randomly (might create collision with robot) */
    public void randomTeleport() {
        setX((float) (Math.random() * maxX));
        setY((float) (Math.random() * maxY));
    }

    /* teleport in a place which avoids collision with a robot */
    public void safeTeleport(Robots robots, Vector<ImageView> wreckages) {
        do {
            /* try a teleportation */
            setX((float) (Math.random() * maxX));
            setY((float) (Math.random() * maxY));

        }while (isInCollision(robots, wreckages));
    }

    /* check if the astronaut collides with something */
    public boolean isInCollision(Robots robots, Vector<ImageView> wreckages) {
        boolean collision = false;
        Rect rc1 = new Rect();
        Rect rc2 = new Rect();

        /* get my hit box */
        this.getHitRect(rc1);
        /* check collisions with robots */
        for (Robot r : robots.list ){
            r.getHitRect(rc2);
            if (Rect.intersects(rc1, rc2)) {
                collision = true;
            }
        }
        /* check collisions with wreckages */
        if (!collision) {
            for (ImageView r : wreckages) {
                r.getHitRect(rc2);
                if (Rect.intersects(rc1, rc2)) {
                    collision = true;
                }
            }
        }

        return collision;
    }
}
