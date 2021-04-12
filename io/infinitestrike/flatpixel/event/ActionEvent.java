package io.infinitestrike.flatpixel.event;

public class ActionEvent extends Event{
    private final long when;

    public ActionEvent(Object sender, int state, Object... params){
        super(sender,state,params);
        this.when = System.nanoTime();
    }

    public long getWhen(){
        return this.when;
    }
}
