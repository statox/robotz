package com.statox.robotz;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageView;

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
    
    public Astronaut(Context context, Point screenSize) {
        super(context);
        setImageResource(R.drawable.astronaut);
        setY((float) (screenSize.y/2));
        setX((float) (screenSize.x/2));
        stepV = 24;
        stepH = 24;
    }

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
        if (this.getX() <= 1000 - stepV)
            this.setX(this.getX() + stepV);
    }
    /* down */
    public void moveD() {
        if (this.getY() <= 2000 - stepH)
            this.setY(this.getY() + stepH);
    }
}
