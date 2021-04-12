package io.infinitestrike.flatpixel.event;

import java.util.*;

import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.core.Updatable;
import io.infinitestrike.flatpixel.state.StateController;
import sun.rmi.runtime.Log;

public class EventDispacher implements Updatable {

    public static boolean LOG_DATA_DEBUG = false;

    private HashMap<String, EventCondition> events = new HashMap<String,EventCondition>();
    private ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();
    private StateController gameController = null;

    private EventDispacher(){};

    // Singleton
    private static final EventDispacher instance = new EventDispacher();

    public static EventDispacher getInstance(){
        return instance;
    }

    @Override
    public void update(float deltaTime) {
        for(Iterator<Map.Entry<String,EventCondition>> eventsItr = events.entrySet().iterator(); eventsItr.hasNext();){
            Map.Entry<String,EventCondition> entry = eventsItr.next();
            if(entry.getValue().testCondition()){
                fire(entry.getKey(),gameController);
            }
        }
    }

    public void release(String name){
        events.remove(name);
        for(Iterator<EventHandler> handlersItr = handlers.iterator(); handlersItr.hasNext();){
            EventHandler handler = handlersItr.next();
            if(handler.eventName.equals(name)){
                handlersItr.remove();
            }
        }
    }

    public void define(String name, EventCondition condition){
        events.put(name,condition);
    }

    public void bind(EventHandler handler){
        if(EventDispacher.LOG_DATA_DEBUG){
            LogBot.log(LogBot.Status.DEVELOPER, "New Event Listener:, '%s'",handler.eventName);
        }
        handlers.add(handler);
    }

    public void fire(String name, Object... args){
        EventHandler[] handlers = new EventHandler[this.handlers.size()];
        System.arraycopy(this.handlers.toArray(),0,handlers,0,this.handlers.size());
        for(EventHandler h : handlers){
            if(h.eventName.equals(name) || h.eventName.equals("*")){
                h.onEventFire(this.gameController,name,args);
            }
        }
    }

    public void setGameController(StateController controller){
        this.gameController = controller;
    }

    public static abstract class EventCondition{
        public abstract boolean testCondition();
    }

    public static abstract class EventHandler{
        protected final String eventName;
        public EventHandler(String eventName){
            this.eventName = eventName;
        }
        public abstract void onEventFire(StateController state, Object... args);
        public void onEventFire(StateController state, String name, Object... args){
            this.onEventFire(state,args);
        }
    }
}
