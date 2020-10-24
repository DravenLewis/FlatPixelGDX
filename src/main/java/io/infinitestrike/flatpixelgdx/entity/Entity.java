package io.infinitestrike.flatpixelgdx.entity;

import com.badlogic.gdx.math.Rectangle;

import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.state.RenderState;

public abstract class Entity {
    private EntityManager entityManager = null;

    private float x = 0;
    private float y = 0;
    private float width = 32;
    private float height = 32;
    private boolean doRender = true;

    long life = 0;

    int entityIndex = 0;

    public Entity(float x, float y){
        this(x,y,32,32);
    }

    public Entity(float x, float y, float w, float h){
        this.setX(x);
        this.setY(y);
        this.width = w;
        this.height = h;
    }

    public final EntityManager getEntityManager(){
        return this.entityManager;
    }

    public final void setEntityManager(EntityManager m){
        if(this.entityManager == null || m == null){
            this.entityManager = m;
        }
    }

    public final int getEntityIndex(){
        return this.entityIndex;
    }

    public final void destroy(){
        if(this.getEntityManager() != null) { // object could have already called destory and still be active
            this.getEntityManager().removeEntity(this);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void move(float x, float y){
        this.x += x;
        this.y += y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public long getLife() {return this.life;}

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public boolean isDoRender(){
        return this.doRender;
    }

    public void setDoRender(boolean b){
        this.doRender = b;
    }

    public abstract void onEntityCreate(RenderState s);
    public abstract void onEntityUpdate(RenderState s, float deltatime);
    public abstract void onEntityRender(RenderState s, Graphics g);
    public abstract void onEntityDestroy(RenderState s);
    public abstract void onEntityCollide(Entity e);
}
