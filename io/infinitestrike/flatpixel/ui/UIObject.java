package io.infinitestrike.flatpixel.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import io.infinitestrike.flatpixel.core.Updatable;
import io.infinitestrike.flatpixel.grafx.Drawable;
import io.infinitestrike.flatpixel.grafx.Graphics;

public abstract class UIObject implements Disposable {

    protected int objectID = -1;

    public void tick(float delta){
        this.onUIUpdate(delta);
    }

    public void render(Graphics g){
        this.onUIRender(g);
    }

    @Override
    public final void dispose(){
        this.onUIDestroy();
    }

    public abstract void onUICreate();
    public abstract void onUIUpdate(float delta);
    public abstract void onUIRender(Graphics g);
    public abstract void onUIDestroy();
}
