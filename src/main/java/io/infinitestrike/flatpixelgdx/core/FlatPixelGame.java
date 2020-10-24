package io.infinitestrike.flatpixelgdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;

import io.infinitestrike.flatpixelgdx.grafx.Color;
import io.infinitestrike.flatpixelgdx.grafx.DrawSurface;
import io.infinitestrike.flatpixelgdx.state.StateController;

public abstract class FlatPixelGame extends StateController implements InputManager.InputListener {

    private int gameWidth = 0;
    private int gameHeight = 0;

    // NEED A STATE MANAGER
    // NEED A GRAPHICS CONTEXT

    ShapeRenderer renderer = new ShapeRenderer();

    int offset = 0;
    boolean scaleOK = true;

    public FlatPixelGame(int gameWidth, int gameHeight){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        LogBot.log("Game Instance Created Size [%s,%s]",gameWidth,gameHeight);
        InputManager.getInstance().addListener(this);
    }

    public int getGameWidth() {
        return gameWidth;
    }

    public int getGameHeight() {
        return gameHeight;
    }

    public abstract void onGameStart();
    public final void onGameUpdate(){
        this.tick();
    };
    public final void onGameDraw(){
        this.draw();
    };

    public final void onGameExit(){
        this.dispose();
    };
    public abstract void onInputEvent(InputManager.InputType t, InputManager m);
}
