package io.infinitestrike.flatpixel.util;

import com.badlogic.gdx.math.*;
import com.google.gson.*;

import java.lang.reflect.*;
import java.util.*;

import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.entity.EntityManager;
import io.infinitestrike.flatpixel.entity.MapEntity;
import io.infinitestrike.flatpixel.event.EventDispacher;
import io.infinitestrike.flatpixel.json.JSON;
import io.infinitestrike.flatpixel.state.StateController;


public class TiledMapObject extends Tile{
    private final HashMap<String, Object> objectProperties = new HashMap<String, Object>();
    private EventDispacher dispacher = EventDispacher.getInstance();
    private final TiledMapWrapper wrapper;
    private int layerIndex = -1;

    protected TiledMapObject(float x, float y, float w, float h, TiledMapWrapper parent) {
        this.setBounds(new Rectangle(x,y,w,h));
        this.wrapper = parent;
    }

    public TiledMapWrapper getParent(){
        return wrapper;
    }

    protected void setProperty(String name, Object value){
        this.objectProperties.put(name,value);
    }

    public Object getProperty(String name, Object fallback){
        if(this.objectProperties.containsKey(name)){
            return this.objectProperties.get(name);
        }
        return fallback;
    }

    public <T> T getProperty(String name, T fallback, Class<T> clazz){
        if(this.objectProperties.containsKey(name)){
            return clazz.cast(this.objectProperties.get(name));
        }
        return fallback;
    }

    public boolean hasProperty(String name){
        return this.objectProperties.containsKey(name);
    }

    public HashMap<String, Object> getObjectProperties() {
        return objectProperties;
    }

    private Object decodeValue(String name, Object def){
        if(name.startsWith("$")){
            Object prop = this.getProperty(name.replace("$",""),def);
            try{

                double val = Double.parseDouble("" + prop);
                if((val == Math.floor(val) && !Double.isInfinite(val))){
                    return (int) val;
                }else{
                    return val;
                }
            }catch (NumberFormatException e){
                return prop;
            }
        }else{
            return name;
        }
    }

    public int getLayerIndex(){
        return this.layerIndex;
    }

    protected void setLayerIndex(int index){
        this.layerIndex = index;
    }

    public void defineActions(String json){
        JsonObject object = JSON.parse(json);
        JsonArray actionSubGroup = object.get("actions").getAsJsonArray();
        final EventDispacher dispach = EventDispacher.getInstance();
        for(int i = 0; i < actionSubGroup.size(); i++){
            JsonObject iAction = actionSubGroup.get(i).getAsJsonObject();
            final String name = iAction.get("name").getAsString();
            String trigger = iAction.get("trigger").getAsString();
            final String target = iAction.get("target").getAsString();
            final EntityManager manager = this.getParent().getParent().getEntityManager();

            JsonObject iActionEffect = iAction.get("effect").getAsJsonObject();
            final String effectName = iActionEffect.get("name").getAsString();
            final Object[] effectValues = JSON.parse(iActionEffect.get("values").toString(),Object[].class);

            for(int o = 0; o < effectValues.length; o++){
                if(effectValues[o] instanceof Number){
                    double objectVal = (double) effectValues[o];
                    if ((objectVal == Math.floor(objectVal) && !Double.isInfinite(o))){
                        effectValues[o] = (int) objectVal;
                    }
                }
                if(effectValues[o] instanceof String){
                    String s = (String) effectValues[o];
                    effectValues[o] = decodeValue(s,effectValues[o]);
                }
            }

            dispach.bind(new EventDispacher.EventHandler(trigger) {
                @Override
                public void onEventFire(StateController state, Object... args) {
                    for(Object o : args){
                        if(o instanceof TiledMapObject){
                            if(o != TiledMapObject.this) return;
                        }
                    }

                    try {
                        Class targetEntity = Class.forName(target);
                        for(Entity e : manager.getEntities()){
                            if(e != null){
                                if(e.getClass().equals(targetEntity)){ // Means We Are the Target
                                    Class targetEntityFromPool = e.getClass();
                                    for(Method effectMethod : targetEntityFromPool.getMethods()){
                                        if(effectMethod.getName().equals(effectName) &&
                                        effectMethod.getParameterTypes().length == effectValues.length){
                                            Object result = effectMethod.invoke(e,effectValues);
                                            if(result instanceof MapEntity){
                                                // if we create a map entity
                                                MapEntity resultEntity = (MapEntity) result;
                                                resultEntity.setProperties(TiledMapObject.this.objectProperties);
                                                resultEntity.onMapEntityLoaded(TiledMapObject.this.getParent().getParent());
                                            }
                                            dispach.fire(name,e,effectValues);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        LogBot.logException(e, "No Such Class: %s ",target);
                    } catch (IllegalAccessException e) {
                        LogBot.logException(e, "Illegal Access: ");
                    } catch (InvocationTargetException e) {
                        LogBot.logException(e, "Illegal Invocation: ");
                    }
                }
            });
        }
    }
}
