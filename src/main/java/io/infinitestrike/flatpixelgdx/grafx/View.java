package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.infinitestrike.flatpixelgdx.core.InputManager;

public class View {
    private int width;
    private int height;
    private Graphics graphics;

    public View(int width, int height){
        this.width = width;
        this.height = height;
        this.graphics = Graphics.getGraphics(this);
    }

    // Run All Draw calls Prior
    public void draw(){

    }

    public void dispose(){
        this.graphics.dispose();
    }

    public int getWidth() {
        return width;
    }

    public SpriteBatch getDrawBatch(){
        return this.getGraphics().getDrawBatch();
    }

    public void setWidth(int width) {
        this.width = width;
        this.graphics.dispose();
        this.graphics = Graphics.getGraphics(this);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.graphics.dispose();
        this.graphics = Graphics.getGraphics(this);
    }

    public Graphics getGraphics(){
        return this.graphics;
    }

    public InputManager getInput(){
        return InputManager.getInstance();
    }
}
