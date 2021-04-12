package io.infinitestrike.flatpixel.entity;

import com.badlogic.gdx.math.Rectangle;

import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.state.RenderState;


public abstract class Entity {
    private EntityManager entityManager = null;

    protected float x = 0;
    protected  float y = 0;
    protected  float width = 32;
    protected  float height = 32;
    private boolean doRender = true;
    private boolean solid = false;
    private PlayArea playArea = null;
    private boolean isActive = true;

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

    public final void destory(Entity e){
        if(this.getEntityManager() != null){
            this.getEntityManager().removeEntity(e);
        }
    }

    public final void instantiate(Entity e){
        if(this.getEntityManager() != null){
            this.getEntityManager().addEntity(e);
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

    public boolean move(float x, float y, boolean checkCollision){
        if(checkCollision) {
            if(placeFree(this.x + x, this.y + y,this.getWidth(),this.getHeight())){
                // move by bound
                this.x += x;
                this.y += y;
                return true;
            }else if(placeFree(this.x + 1, this.y + 1, this.getWidth(),this.getHeight())){
                // move by one
                if(x != 0) this.x += ((x > 0) ? 1 : -1);
                if(y != 0) this.y += ((y > 0) ? 1 : -1);
                return false;
            }else{
                this.x += 0;
                this.y += 0;
                return false;
            }
        }else {
            this.x += x;
            this.y += y;
            return true;
        }
    }

    public boolean move(float x, float y, boolean checkCollision, Rectangle[] r){
        if(checkCollision) {
            if(placeFreeRect(this.x + x,this.y + y,this.getWidth(),this.getHeight(),r) &&
                    placeFree(this.x + x, this.y + y,this.getWidth(),this.getHeight())){
                // move by bound
                this.x += x;
                this.y += y;
                return true;
            }else if(placeFreeRect(this.x + x,this.y + y,this.getWidth(),this.getHeight(),r) &&
                    placeFree(this.x + 1, this.y + 1, this.getWidth(),this.getHeight())){
                // move by one
                if(x != 0) this.x += ((x > 0) ? 1 : -1);
                if(y != 0) this.y += ((y > 0) ? 1 : -1);
                return false;
            }else{
                this.x += 0;
                this.y += 0;
                return false;
            }
        }else {
            this.x += x;
            this.y += y;
            return true;
        }
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

    public void setBounds(Rectangle rect){
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public boolean isDoRender(){
        return this.doRender;
    }

    public void setDoRender(boolean b){
        this.doRender = b;
    }

    public boolean placeFree(float x, float y, float w, float h){
        Rectangle r = new Rectangle(x,y,w,h);

        if(this.playArea != null){
            if(!this.getPlayArea().isInside(this)){
                this.getPlayArea().correct(this);
                return false;
            }
        }

        for(Entity e : this.getEntityManager().getEntities()){
            if(e == null || e == this) continue;
            Rectangle t = new Rectangle(e.getBounds());
            t.width += 2;
            t.height += 2;
            if(t.contains(r) || t.overlaps(r) && e.isSolid()) return false;
        }
        return true;
    }

    public boolean placeFreeRect(float x, float y, float w, float h, Rectangle[] rect){
        Rectangle r = new Rectangle(x,y,w,h);

        if(this.playArea != null){
            if(!this.getPlayArea().isInside(this)){
                this.getPlayArea().correct(this);
                return false;
            }
        }

        for(Rectangle rec : rect){
            Rectangle t = new Rectangle(rec);
            t.width += 2;
            t.height += 2;
            if(t.contains(r) || t.overlaps(r)) return false;
        }

        return true;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean placeFree(float x, float y){
        return placeFree(x,y,2,2);
    }

    public PlayArea getPlayArea(){
        return this.playArea;
    }

    public void setPlayArea(PlayArea a){
        this.playArea = a;
    }

    public void setActive(boolean b){
        this.isActive = b;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public abstract void onEntityCreate(RenderState s);
    public abstract void onEntityUpdate(RenderState s, float deltatime);
    public abstract void onEntityRender(RenderState s, Graphics g);
    public abstract void onEntityDestroy(RenderState s);
    public abstract void onEntityCollide(Entity e);
}
