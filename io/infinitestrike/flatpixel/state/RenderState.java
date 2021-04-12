package io.infinitestrike.flatpixel.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.entity.EntityManager;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.View;
import io.infinitestrike.flatpixel.ui.UILayer;
import io.infinitestrike.flatpixel.ui.UIObject;

public abstract class RenderState extends View {

    public static final int MAX_UI_LAYERS = 20;

    private int id = -1;
    private EntityManager manager = new EntityManager();
    private boolean doesDestroyOnExit = false;
    private boolean isInit = false;
    private UILayer uiLayer = null;

    public RenderState(int width, int height, int id) {
        super(width, height);
        this.id = id;
    }

    public void init(){
        if(!this.isInit) {
            manager.setParent(this);
            this.uiLayer = new UILayer();
            this.uiLayer.setParent(this);
            this.onInit(this);
            this.isInit = true;
        }
    }

    public void tick(){
        if(this.isInit) {
            manager.tick(this, Gdx.graphics.getDeltaTime());
            this.onTick(this, Gdx.graphics.getDeltaTime());
        }
    }

    public void render(){
        if(this.isInit) {
            this.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            this.onRender(this, this.getGraphics());
            manager.render(this, this.getGraphics());
            this.end();
        }
    }

    public UILayer getUILayer(){
        return this.uiLayer;
    }

    public void stateChange(RenderState from, RenderState to){
        this.onStateChange(from,to);
    }

    public void destroy(){

        if(this.doesDestroyOnExit) this.isInit = false;

        for(Entity e : manager.getEntities()){
            if(e != null){
                e.destroy();
            }
        }
        this.onDestroy(this);
        this.dispose();
        this.getUILayer().dispose();
    }

    public int getID(){
        return this.id;
    }

    public boolean doesDestroyOnExit(){
        return this.doesDestroyOnExit;
    }

    public void setDoesDestroyOnExit(boolean doesDestroyOnExit){
        this.doesDestroyOnExit = doesDestroyOnExit;
    }

    public boolean isInit(){
        return this.isInit;
    }

    public EntityManager getEntityManager(){
        return this.manager;
    }

    // shorthands
    public void instantiate(Entity e){
        if(e == null) return;
        this.getEntityManager().addEntity(e);
    }

    public void destroy(Entity e){
        if(e == null) return;
        this.getEntityManager().removeEntity(e);
    }

    public void addUIObject(UIObject obj){
        this.uiLayer.addUIObject(obj);
    }

    public void removeUIObject(UIObject obj){
        this.uiLayer.removeUIObject(obj);
    }

    // small methods
    public abstract void onInit(View v);
    public abstract void onTick(View v, float delta);
    public abstract void onRender(View v, Graphics g);
    public abstract void onStateChange(RenderState from, RenderState to);
    public abstract void onDestroy(View v);
}
