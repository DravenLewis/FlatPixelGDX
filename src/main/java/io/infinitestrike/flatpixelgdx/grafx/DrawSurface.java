package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class DrawSurface {

    private int surfaceWidth = 0;
    private int surfaceHeight = 0;
    private Pixmap drawSurface = null;
    private boolean isValdated = false;

    public DrawSurface(){}

    public static DrawSurface createSurface(int width, int height){
        DrawSurface surface = new DrawSurface();
        surface.surfaceWidth = width;
        surface.surfaceHeight = height;
        surface.drawSurface = new Pixmap(surface.surfaceWidth,surface.surfaceHeight, Pixmap.Format.RGBA8888);
        return surface;
    }

    public void draw(SpriteBatch batch, float x, float y){
        this.draw(batch,x,y,surfaceWidth,surfaceHeight);
    }

    public void draw(SpriteBatch batch, float x, float y, float w, float h){
        batch.begin();
        Texture t = new Texture(this.drawSurface);
        batch.draw(t,x,y,w,h);
        batch.end();
    }

    public void setWidth(int width){
        this.surfaceWidth = width;
        this.drawSurface = new Pixmap(this.surfaceWidth,this.surfaceHeight, Pixmap.Format.RGBA8888);
    }

    public void setHeight(int height){
        this.surfaceHeight = height;
        this.drawSurface = new Pixmap(this.surfaceWidth,this.surfaceHeight, Pixmap.Format.RGBA8888);
    }

    public void dispose(){
        this.drawSurface.dispose();
        this.drawSurface = null;
        this.isValdated = false;
    }

    public int getWidth(){
        return this.surfaceWidth;
    }

    public int getHeight(){
        return this.surfaceHeight;
    }

    public Pixmap getPixMap(){
        return this.drawSurface;
    }
}
