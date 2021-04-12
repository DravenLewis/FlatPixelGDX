package io.infinitestrike.flatpixel.util;

import com.badlogic.gdx.math.*;

public class Tile {
    public int x = 0, y = 0, width = 0, height = 0;

    public final Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    protected final void setBounds(Rectangle rect){
        this.x = (int )rect.x;
        this.y = (int )rect.y;
        this.width = (int )rect.width;
        this.height = (int )rect.height;
    }
}
