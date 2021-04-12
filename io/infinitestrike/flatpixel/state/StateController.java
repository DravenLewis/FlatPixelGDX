package io.infinitestrike.flatpixel.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.*;

import io.infinitestrike.flatpixel.core.CameraController;
import io.infinitestrike.flatpixel.core.FlatPixelGame;
import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.event.EventDispacher;
import io.infinitestrike.flatpixel.grafx.Font;
import io.infinitestrike.flatpixel.grafx.Image;


public class StateController {
    /**
     * Max Number of render states, if need more, change me to something else;
     */
    public static int MAX_RENDER_STATES = 500;

    // Debug Stuff;
    Font f = new Font(28, true);

    private SpriteBatch alphaBatch = new SpriteBatch();
    ShapeRenderer renderer = new ShapeRenderer();

    private RenderState[] renderStates = new RenderState[MAX_RENDER_STATES];
    private int currentRenderingState = -1;
    private float originalSizeX = 0, originalSizeY = 0;

    private EventDispacher disp = EventDispacher.getInstance();

    public void addRenderState(RenderState s){
        this.renderStates[s.getID()] = s;
        disp.fire("state_add",s);
        s.init();
    }

    public void removeRenderState(int id){
        if(id > renderStates.length - 1 || id < 0){
            return;
        }
        if(this.currentRenderingState == id){
            this.currentRenderingState = -1;
        }
        RenderState s = this.renderStates[id];
        disp.fire("state_remove",s);
        s.destroy();
        this.renderStates[id] = null;
    }

    // Expensive, only call when needed.
    public ArrayList<RenderState> getValidRenderStates(){
        ArrayList<RenderState> renderStates = new ArrayList<RenderState>();
        for(int i = 0; i < this.renderStates.length; i++){
            if(this.renderStates[i] != null){
                renderStates.add(this.renderStates[i]);
            }
        }
        return renderStates;
    }

    public RenderState getRenderState(int id){
        if(id > this.renderStates.length - 1 || id < 0) return null;
        return this.renderStates[id];
    }

    public RenderState getCurrentRenderState(){
        if(this.currentRenderingState > this.renderStates.length || this.currentRenderingState < 0) return null;
        return this.getRenderState(this.currentRenderingState);
    }

    public void draw(){
        if(this.currentRenderingState != -1){
            if(this.renderStates[this.currentRenderingState] == null) {alphaBatch.end(); return;}

            this.getCurrentRenderState().render();
            alphaBatch.setProjectionMatrix(CameraController.getInstance().getCamera().combined);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            alphaBatch.begin();

            this.getCurrentRenderState().draw(alphaBatch,new Rectangle(
                    0,0,
                    FlatPixelGame.getInstance().getGameWidth() / (FlatPixelGame.getInstance().getGameWidth() / originalSizeX),
                    FlatPixelGame.getInstance().getGameHeight() / (FlatPixelGame.getInstance().getGameHeight() /originalSizeY)
            ));

            Image img = this.getCurrentRenderState().getUILayer().getImage();
            img.setBounds(0,0,FlatPixelGame.getInstance().getGameWidth(),FlatPixelGame.getInstance().getGameHeight());
            img.draw(alphaBatch);

            alphaBatch.end();
        } else {
            alphaBatch.setProjectionMatrix(CameraController.getInstance().getCamera().combined);
            alphaBatch.begin();
            f.getInternalFont().draw(alphaBatch,"FlatPixel: No Valid Render State.",30, 40);
            alphaBatch.end();
        }
    }

    public void tick(){
        if(this.originalSizeX == 0 || this.originalSizeY == 0){
            this.originalSizeX = Gdx.graphics.getWidth();
            this.originalSizeY = Gdx.graphics.getHeight();
        }

        if(this.currentRenderingState != -1){
            if(this.renderStates[this.currentRenderingState] == null) {return;}
            this.getCurrentRenderState().tick();
            this.getCurrentRenderState().getUILayer().update();
        }
    }

    public void enterState(int id){

        if(id < 0 || id > this.renderStates.length - 1) return;
        if(this.renderStates[id] == null) return;
        int old = this.currentRenderingState;

        // if the current state is valid
        if(this.getCurrentRenderState() != null){
            // tell the current state that we are changing.
            this.getCurrentRenderState().stateChange(this.getRenderState(this.currentRenderingState),this.getRenderState(id));
            // if the target state is valid
            if(this.getRenderState(id) != null){
                // tell the target state that a change is happening
                this.getRenderState(id).stateChange(this.getCurrentRenderState(),this.getRenderState(id));
            }
            // if the current state destroys on exit
            if(this.getCurrentRenderState().doesDestroyOnExit()) {
                // destroy it.
                this.getCurrentRenderState().destroy();
            }
        }

        // the state has been changed now.
        this.currentRenderingState = id;

        // if the current render state is valid
        if(this.getCurrentRenderState() != null){
            // if this current render state does not destroy on exit and it has been initialized
            if(!(!this.getCurrentRenderState().doesDestroyOnExit() && this.getCurrentRenderState().isInit())) {
                // do not call its Init Function again.
                this.getCurrentRenderState().init();
                disp.fire("state_init",getCurrentRenderState());
            }
        }

        disp.fire("state_change",getRenderState(old),getRenderState(id));
    }

    public int getValidRenderStateSize(){
        return this.getValidRenderStates().size();
    };

    public SpriteBatch getAlphaBatch(){
        return this.alphaBatch;
    }

    public ShapeRenderer getRenderer(){
        return this.renderer;
    }

    public void dispose(){
        for(RenderState c : this.renderStates){
            if(c != null) {
                c.dispose();
            }
        }
        alphaBatch.dispose();
        disp.fire("state_machine_dispose");
    }
}