package io.infinitestrike.flatpixel.event;

public abstract class ActionListener implements EventListener{
    public abstract void actionPerformed(ActionEvent e);
    public void onEvent(Event e){
        ActionEvent ev = new ActionEvent(e.getSender(),e.getState(),e.getArgs());
        this.actionPerformed(ev);
    }
}
