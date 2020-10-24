package io.infinitestrike.flatpixelgdx.event;

public class ActionEvent {
    private final long when;
    private final Object sender;
    private final int state;
    private final String[] params;

    public ActionEvent(Object sender, int state, String... params){
        this.when = System.nanoTime();
        this.sender = sender;
        this.state = state;
        this.params = params;
    }

    public long getWhen(){
        return this.when;
    }

    public Object getSender(){
        return this.sender;
    }

    public int getState(){
        return this.state;
    }

    public String[] getParams(){
        return this.params;
    }
}
