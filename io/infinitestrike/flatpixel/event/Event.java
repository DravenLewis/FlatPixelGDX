package io.infinitestrike.flatpixel.event;

public class Event {

    private final Object sender;
    private final int state;
    private final Object[] args;

    public Event(Object sender, int state, Object... args){
        this.sender = sender;
        this.state = state;
        this.args = args;
    }

    public final Object getSender(){
        return this.sender;
    }

    public final int getState(){
        return this.state;
    }

    public final Object[] getArgs(){
        return this.args;
    }
}
