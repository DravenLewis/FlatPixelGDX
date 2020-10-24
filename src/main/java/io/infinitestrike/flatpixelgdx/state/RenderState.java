package io.infinitestrike.flatpixelgdx.state;

import com.badlogic.gdx.Gdx;

import io.infinitestrike.flatpixelgdx.entity.EntityManager;
import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.grafx.View;

public abstract class RenderState {
    private View v;
    private final int id;
    private StateController controller = null;
    private final EntityManager manager = new EntityManager();


    public RenderState(int id, int width, int height){
        v = new View(width,height);
        this.id = id;
        this.manager.setParent(this);
    }

    public int getID(){
        return this.id;
    }

    public View getView(){
        return this.v;
    }

    public void srender(){
        this.render(this.getView(),this.getView().getGraphics());
        this.getEntityManager().render(this,this.getView().getGraphics());
        this.getView().draw();
    }

    public void stick(){
        this.tick(this.getView(), Gdx.graphics.getDeltaTime());
        this.getEntityManager().tick(this, Gdx.graphics.getDeltaTime());
    }

    public void setStateController(StateController c){
        this.controller = c;
    }

    public StateController getController(){
        return this.controller;
    }

    public EntityManager getEntityManager(){
        return this.manager;
    }

    public abstract void init(View v);
    public abstract void tick(View v, float delta);
    public abstract void render(View v, Graphics g);
    public abstract void stateChange(RenderState to);
    public abstract void destroy(View v);
}
