package io.infinitestrike.flatpixel.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.event.Event;
import io.infinitestrike.flatpixel.event.EventDispacher;
import io.infinitestrike.flatpixel.grafx.Drawable;
import io.infinitestrike.flatpixel.state.RenderState;
import io.infinitestrike.flatpixel.state.StateController;


public abstract class FlatPixelGame extends StateController implements InputManager.InputListener {

    public static boolean FLAG_RECALCULATE_MATRIX_ON_RESIZE = true;

    private static ArrayList<Updatable> updatables = new ArrayList<Updatable>();
    private static ArrayList<Drawable> drawables = new ArrayList<Drawable>();
    private static FlatPixelGame game = null;

    private int gameWidth = 0;
    private int gameHeight = 0;

    // NEED A STATE MANAGER
    // NEED A GRAPHICS CONTEXT

    int offset = 0;
    boolean scaleOK = true;

    public FlatPixelGame(int gameWidth, int gameHeight){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        FlatPixelGame.game = this;
        LogBot.log("Game Instance Created Size [%s,%s]",gameWidth,gameHeight);
        InputManager.getInstance().addListener(this);
        EventDispacher.getInstance().setGameController(this);

        FlatPixelGame.addUpdatable(EventDispacher.getInstance());
        FlatPixelGame.addUpdatable(new DebugExitUpdatable());
        CameraController.getInstance().make2DGame();
    }

    public int getGameWidth() {
        return gameWidth;
    }

    public int getGameHeight() {
        return gameHeight;
    }

    public final void onGameStart(){
        this.onInit();
    }

    public abstract void onInit();
    public abstract void onExit();

    public final void onGameUpdate(){
        this.tick();
        for(Updatable u : FlatPixelGame.updatables){
            u.update(Gdx.graphics.getDeltaTime());
        }
    };
    public final void onGameDraw(){
        Gdx.gl.glViewport(0,0,this.getGameWidth(),this.getGameHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        this.draw();
        for(Drawable u : FlatPixelGame.drawables){
            this.getAlphaBatch().begin();
            u.draw(this.getAlphaBatch());
            this.getAlphaBatch().end();

            this.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
            u.drawFilled(this.getRenderer());
            this.getRenderer().end();

            this.getRenderer().begin(ShapeRenderer.ShapeType.Line);
            u.drawOutline(this.getRenderer());
            this.getRenderer().end();
        }
        CameraController.getInstance().getCamera().update();
    };

    public final void onGameExit(){
        this.dispose();
        this.onExit();
    };
    public abstract void onInputEvent(InputManager.InputType t, InputManager m);

    public static final Camera getGameCamera(){ // shorthand for back compat
        return CameraController.getInstance().getCamera();
    };

    public static final float getAspectRatio(Camera c){
        return c.viewportWidth / c.viewportHeight;
    }

    public static final float getHeightAspect(float width){
        //(original height / original width) x new width = new height
        float aspectRatio = getAspectRatio(getGameCamera());
        return  aspectRatio * width;
    }

    public static void addUpdatable(Updatable u){
        FlatPixelGame.updatables.add(u);
    }

    public static void removeUpdatable(Updatable u){
        FlatPixelGame.updatables.remove(u);
    }

    public static void addDrawable(Drawable u){
        FlatPixelGame.drawables.add(u);
    }

    public static void removeDrawable(Drawable u){
        FlatPixelGame.drawables.remove(u);
    }

    public final void onResize(int width, int height){
        if(CameraController.getInstance().getCamera() != null && FLAG_RECALCULATE_MATRIX_ON_RESIZE) {
            CameraController.getInstance().getViewport().update(width, height);
            CameraController.getInstance().getCamera().position.set(
                    CameraController.getInstance().getCamera().viewportWidth / 2,
                    CameraController.getInstance().getCamera() .viewportHeight / 2,
                    0
            );

            this.gameWidth = width;
            this.gameHeight = height;
            CameraController.getInstance().getCamera().update();
        }
    }

    public static FlatPixelGame getInstance(){
        return FlatPixelGame.game;
    }

    public static final void quit(){
        Thread t = Thread.currentThread();
        StackTraceElement[] elements = t.getStackTrace();
        StackTraceElement lastCall = elements[2];
        String[] qualifiedClassName = lastCall.getClassName().split("\\.");
        LogBot.log(LogBot.Status.INFO,"Quit Called from %s.%s(); on line: %s",qualifiedClassName[qualifiedClassName.length - 1],lastCall.getMethodName(),lastCall.getLineNumber());
        FlatPixelGame g = FlatPixelGame.getInstance();
        for(int i = 0; i < StateController.MAX_RENDER_STATES; i++){
            RenderState state;
            if((state = g.getRenderState(i)) != null){
                state.destroy();
            }
        }
        g.dispose();
        Gdx.app.exit();
        System.exit(0);
    }

    private static class DebugExitUpdatable implements Updatable{

        InputManager manager = InputManager.getInstance();

        @Override
        public void update(float deltaTime) {
            if(manager.isKeyDown(Input.Keys.CONTROL_LEFT) && manager.isKeyDown(Input.Keys.SHIFT_LEFT)
                    && manager.isKeyDown(Input.Keys.ESCAPE)){
                FlatPixelGame.quit();
            }
        }
    }
}