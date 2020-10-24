package io.infinitestrike.flatpixelgdx.state;

import java.util.ArrayList;

public class StateController {
    /**
     * Max Number of render states, if need more, change me to something else;
     */
    public static int MAX_RENDER_STATES = 500;

    private RenderState[] renderStates = new RenderState[MAX_RENDER_STATES];
    private int currentRenderingState = -1;

    public void addRenderState(RenderState s){
        this.renderStates[s.getID()] = s;
    }

    public void removeRenderState(int id){
        if(id > renderStates.length - 1 || id < 0){
            return;
        }
        if(this.currentRenderingState == id){
            this.currentRenderingState = -1;
        }
        RenderState s = this.renderStates[id];
        s.destroy(s.getView());
        this.renderStates[id] = null;
    }

    public RenderState getRenderState(int id){
        if(id > this.renderStates.length - 1 || this.currentRenderingState < 0) return null;
        return this.renderStates[id];
    }

    public RenderState getCurrentRenderState(){
        if(this.currentRenderingState > this.renderStates.length || this.currentRenderingState < 0) return null;
        return this.getRenderState(this.currentRenderingState);
    }

    public void draw(){
        this.getCurrentRenderState().srender();
    }

    public void tick(){
        this.getCurrentRenderState().stick();
    }

    public void enterState(int id){
        if(id < 0 || id > this.renderStates.length - 1) return;
        if(this.renderStates[id] == null) return;

        if(this.getCurrentRenderState() != null){
            this.getCurrentRenderState().stateChange(this.renderStates[id]);
            this.getCurrentRenderState().destroy(this.getCurrentRenderState().getView());
        }
        this.currentRenderingState = id;

        if(this.getCurrentRenderState() != null){
            this.getCurrentRenderState().init(this.getCurrentRenderState().getView());
        }
    }

    public void dispose(){
        for(RenderState c : this.renderStates){
            c.getView().dispose();
        }
    }
}
