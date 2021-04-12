package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;

public class View extends DrawSurface implements Drawable{

    final Graphics g;

    public View(int width, int height) {
        super(width, height);
        this.g = Graphics.getGraphics(this);
    }

    public Graphics getGraphics(){
        return this.g;
    }

    public void draw(SpriteBatch batch){
        this.draw(batch, new Rectangle(this.x,this.y,this.getHeight(),this.getHeight()));
    }

    @Override
    public void drawFilled(ShapeRenderer r) {

    }

    @Override
    public void drawOutline(ShapeRenderer r) {

    }

    public void draw(SpriteBatch batch, Rectangle rect){
        if(batch.isDrawing()){
            Image img = this.getImage();
            img.setBounds((int)rect.x,(int)rect.y,(int)rect.width,(int)rect.height);
            img.draw(batch);
        }
    }

    @Override
    public void dispose(){
        this.g.dispose();
        super.dispose();
    }
}
