package io.infinitestrike.flatpixel.entity;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.event.EventDispacher;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.state.RenderState;

public class EntityManager {
    public final int ENTITY_CAP = 500;

    private RenderState parentRenderState = null;
    private Entity[] entityList = new Entity[ENTITY_CAP];
    private EventDispacher disp = EventDispacher.getInstance();

    public EntityManager(){

    }

    public void tick(RenderState s, float deltaTime){

        for(Entity e : this.entityList){
            if(e != null && e.isActive()) {
                e.onEntityUpdate(s, deltaTime);
                Entity collider = this.doesEntityCollide(e);
                if(collider != e && collider != null){
                    e.onEntityCollide(collider);
                    disp.fire("entity_collide",e,collider);
                }
                e.life++;
            }
        }
        disp.fire("entity_manager_update",s);
    }

    public  void render(RenderState s, Graphics g){
        for(Entity e : this.entityList){
            if(e != null){
                if(e.isDoRender() && e.isActive()){
                    e.onEntityRender(s,g);
                }
            }
        }

        disp.fire("entity_manager_draw");
    }

    public Entity doesEntityCollide(Entity e){
        for(int i = 0; i < this.entityList.length; i++){
            Entity index = this.entityList[i];
            if(index != null && index != e){
                if(index.getBounds().contains(e.getBounds()) ||
                        index.getBounds().overlaps(e.getBounds())){
                    return index;
                }
            }
        }
        return null;
    }

    public void addEntity(Entity e){
        int index = findEmptyIndex();
        if(index != -1){
            e.entityIndex = index;
            this.entityList[index] = e;
            e.setEntityManager(this);
            e.onEntityCreate(this.getParent());
            LogBot.log(LogBot.Status.INFO,"Added Entity: ID#: %s - %s",index,e.getClass().getName());
        }
    }

    public void removeEntity(Entity e){
        int index = e.getEntityIndex();
        e.onEntityDestroy(this.getParent());
        e.setEntityManager(null);
        this.entityList[index] = null;
        LogBot.log(LogBot.Status.INFO,"Removed Entity: ID#: %s - %s",index,e.getClass().getName());
    }

    public <T> void removeAllOfType(Class<T> clazz){
        for(int i = 0; i < ENTITY_CAP; i++){
            Entity e = this.entityList[i];
            if(e != null){
                if(e.getClass().equals(clazz)){
                    removeEntity(e);
                }
            }
        }
    }

    public <T> T[] getAllOfType(Class<T> clazz){
        ArrayList<T> arrayStruct = new ArrayList<T>();
        for(Entity e : this.entityList){
            if(e != null) {
                if (e.getClass().isAssignableFrom(clazz)) {
                    arrayStruct.add((T) e);
                }
            }
        }
        return Core.fromArrayList(arrayStruct,clazz);
    }

    public void removeEntities(Entity... e){
        for (Entity entity : e) {
            this.removeEntity(entity);
        }
    }

    private int findEmptyIndex(){
        for(int i = 0; i < this.entityList.length; i++){
            if(this.entityList[i] == null) return i;
        }
        return -1;
    }

    public void setParent(RenderState s){
        if(this.parentRenderState == null) {
            this.parentRenderState = s;
        }
    }

    public RenderState getParent(){
        return this.parentRenderState;
    }

    public int getCurrentEntityCount(){
        int count = 0;
        for(int i = 0; i < this.entityList.length; i++){
            if(this.entityList[i] != null) count++;
        }
        return count;
    }

    public Entity[] getEntities(){
        return this.entityList;
    }
}
