package io.infinitestrike.flatpixel.entity;


import com.badlogic.gdx.math.*;

public class PlayArea {
    private Rectangle bounds = new Rectangle();

    public PlayArea(int x, int y, int w, int h){
        this(new Rectangle(x,y,w,h));
    }

    public PlayArea(Rectangle rect){
        this.bounds = rect;
    }

    public boolean isInside(Entity e){
        if(e.getX() >= bounds.getX() && e.getY() >= bounds.getY() &&
        e.getX() + e.getWidth() <= bounds.x + bounds.width && e.getY() + e.getHeight() <= bounds.y + bounds.height){
            return true;
        }else{
            return false;
        }
    }

    public void correct(Entity e){
            if(e.getX() + e.getWidth() > bounds.x + bounds.width) e.move(-1,0,false);
            if(e.getX() < bounds.x) e.move(1,0,false);
            if(e.getY() + e.getHeight() > bounds.y + bounds.height) e.move(0,-1,false);
            if(e.getY() < bounds.y) e.move(0,1,false);
    }
}
