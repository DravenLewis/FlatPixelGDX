package io.infinitestrike.flatpixelgdx.entity;

import java.util.ArrayList;

import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.state.RenderState;
import io.infinitestrike.flatpixelgdx.ui.UIEntity;

public class EntityManager {
    public final int ENTITY_CAP = 500;

    private RenderState parentRenderState = null;
    private Entity[] entityList = new Entity[ENTITY_CAP];

    public void tick(RenderState s, float deltaTime){
        for(int i = 0; i < this.entityList.length; i++){
            Entity e = this.entityList[i];
            if(e != null){
                e.onEntityUpdate(s,deltaTime);
                Entity collider = this.doesEntityCollide(e);
                e.onEntityCollide(collider);
                e.life++;
            }
        }
    }

    public  void render(RenderState s, Graphics g){
        ArrayList<UIEntity> uiEntityCache = new ArrayList<UIEntity>();
        for(int i = 0; i < this.entityList.length; i++){
            Entity e = this.entityList[i];
            if(e != null){
                if(e instanceof UIEntity){
                    uiEntityCache.add((UIEntity) e);
                }else {
                    if(e.isDoRender()) {
                        e.onEntityRender(s, g);
                    }
                }
            }
        }

        if(uiEntityCache.size() > 0){ // render UI entities on top.
            for(int i = 0; i < uiEntityCache.size(); i++){
                UIEntity e = uiEntityCache.get(i);
                if(e != null){
                    if(e.isDoRender()) {
                        e.onEntityRender(s, g);
                    }
                }
            }
        }
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
            System.out.println("[EntityManager] Added Entity: ID# " + index);
        }
    }

    public void removeEntity(Entity e){
        int index = e.getEntityIndex();
        e.onEntityDestroy(this.getParent());
        e.setEntityManager(null);
        this.entityList[index] = null;
        System.out.println("[EntityManager] Removed Entity: ID# " + index);
    }

    public int findEmptyIndex(){
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
}
