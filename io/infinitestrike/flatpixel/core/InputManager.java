package io.infinitestrike.flatpixel.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.UUID;

import io.infinitestrike.flatpixel.math.*;

public class InputManager implements InputProcessor {

    private static final InputManager instance = new InputManager();
    private static final int HEIGHT = Gdx.graphics.getHeight();
    private final ArrayList<InputListener> listeners = new ArrayList<InputListener>();

    private boolean[] keys = new boolean[256];
    private int mouseX = 0;
    private int mouseY = 0;
    private int scroll = 0;
    private Pointer[] pointers = new Pointer[10];
    private final int initialSizeX, initialSizeY;
    private int currentSizeX, currentSizeY;

    public static class Pointer{
        public int x;
        public int y;
        public int button;
        private int holdTime = 0;
        Vector.Vector2i touchStartPosition = new Vector.Vector2i();
        Vector.Vector2i lastDragPosition = new Vector.Vector2i();
        Vector.Vector2i currDragPosition = new Vector.Vector2i();
        public int getTimeAlive() {return this.holdTime;};

        public Pointer getYInverse(){
            Pointer p = new Pointer();
            p.x = x;
            p.y = HEIGHT - y;
            p.button = button;
            p.holdTime = holdTime;
            p.touchStartPosition = touchStartPosition;
            p.lastDragPosition = lastDragPosition;
            p.currDragPosition = currDragPosition;
            return p;
        }

        public String toString(){
            return "Pointer: @ " + x + ","+ y;
        }
    }

    private InputManager(){
        this.initialSizeX = Gdx.graphics.getWidth();
        this.initialSizeY = Gdx.graphics.getHeight();
        this.currentSizeX = this.initialSizeX;
        this.currentSizeY = this.initialSizeY;
        Gdx.input.setInputProcessor(this);
        FlatPixelGame.addUpdatable(new Updatable() {
            @Override
            public void update(float deltaTime) {
                currentSizeX = Gdx.graphics.getWidth();
                currentSizeY = Gdx.graphics.getHeight();
            }
        });
    };

    @Override
    public boolean keyDown(int keycode) {
        if(keycode >= 0 && keycode < 256){
            keys[keycode] = true;
            notifyAll(InputType.KEY_DOWN);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode >= 0 && keycode < 256){
            keys[keycode] = false;
            notifyAll(InputType.KEY_UP);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer >= 0 && pointer < this.pointers.length - 1){
            Pointer p = this.pointers[pointer];
            if(p != null){
                p.x = this.getScaledPointX(screenX);
                p.y = this.getScaledPointY(screenY);
                p.touchStartPosition.x = p.x;
                p.touchStartPosition.y = p.y;
                p.button = button;
                p.holdTime++;
            }else{
                p = new Pointer();
                p.x = this.getScaledPointX(screenX);
                p.y = this.getScaledPointY(screenY);
                p.touchStartPosition.x = p.x;
                p.touchStartPosition.y = p.y;
                p.button = button;
                p.holdTime = 1;
                this.pointers[pointer] = p;
            }
            notifyAll(InputType.TOUCH_DOWN);
            return  true;
        }else{
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer >= 0 && pointer < this.pointers.length -1){
            this.pointers[pointer].holdTime = 0;
            this.pointers[pointer] = null;
            notifyAll(InputType.TOUCH_UP);
            return  true;
        }else{
            return false;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        //this.mouseX = screenX;
        //this.mouseY = screenY;

        this.mouseX = this.getScaledPointX(screenX);
        this.mouseY = this.getScaledPointY(screenY);

        if(pointer >= 0 && pointer < this.pointers.length - 1){
            if(this.pointers[pointer] != null){
                Pointer p = this.pointers[pointer];
                p.x = this.getScaledPointX(screenX);
                p.y = this.getScaledPointY(screenY);
                p.holdTime++;
                p.lastDragPosition.x = p.currDragPosition.x;
                p.currDragPosition.x = p.x;
                p.lastDragPosition.y = p.currDragPosition.y;
                p.currDragPosition.y = p.y;

                notifyAll(InputType.TOUCH_DRAG);


                return true;
            }
            return  false;
        }else{
            return  false;
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //this.mouseX = screenX;
        //this.mouseY = screenY;

        this.mouseX = this.getScaledPointX(screenX);
        this.mouseY = this.getScaledPointY(screenY);
        notifyAll(InputType.MOUSE_MOVE);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        this.scroll = (int) amountX;
        notifyAll(InputType.SCROLL);
        return true;
    }

    public int getScaledPointX(int point){
        return Math.round((float) point / (float) this.currentSizeX * (float) this.initialSizeX);
    }

    public int getScaledPointY(int point){
        return Math.round((float) point / (float) this.currentSizeY * (float) this.initialSizeY);
    }

    /*@Override
    public boolean scrolled(int amount) {
        this.scroll = amount;
        notifyAll(InputType.SCROLL);
        return true;
    }*/

    public ArrayList<Pointer> getPointers(){
        ArrayList<Pointer> pointers = new ArrayList<Pointer>();
        for(int i = 0; i < this.pointers.length; i++){
            if(this.pointers[i] != null){
                pointers.add(this.pointers[i]);
            }
        }
        return  pointers;
    }

    public Pointer[] getShallowPointerCopy(){
        Pointer[] pointers = new Pointer[this.pointers.length];
        for(int i = 0; i < this.pointers.length; i++){
            pointers[i] = this.pointers[i];
        }
        return this.pointers;
    }

    public  boolean isKeyDown(int code){
        if(code >= 0 && code < 256){
            return this.keys[code];
        }
        return  false;
    }

    public int getMouseX(){
        return this.mouseX;
    }

    public  int getMouseY(){
        return  this.mouseY;
    }

    public  int getScroll(){
        int scroll = this.scroll;
        this.scroll = 0;
        return  scroll;
    }

    public void addListener(InputListener l){
        this.listeners.add(l);
    }

    public void removeListener(InputListener l){
        this.listeners.remove(l);
    }

    public void notifyAll(InputType t){
        try {
            for(InputListener l : this.listeners){
                l.onInputEvent(t,this);
            }
        }catch (ConcurrentModificationException e){
            System.out.println("[InputManager] Do not Try to add/remove listeners in event. Call in destroy.");
        }
    }

    public static InputManager getInstance(){
        return InputManager.instance;
    }

    public enum InputType{
        TOUCH_UP,
        TOUCH_DOWN,
        TOUCH_DRAG,
        MOUSE_MOVE,
        KEY_DOWN,
        KEY_UP,
        SCROLL
    }

    public enum  DragType{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    public DragType getDragDirectionHorizontal(Pointer p){
        if(p == null) return DragType.NONE;
        if(p.currDragPosition.x > p.lastDragPosition.y) return DragType.RIGHT;
        if(p.currDragPosition.x < p.lastDragPosition.x) return  DragType.LEFT;
        return DragType.NONE;
    }

    public DragType getDragDirectionVertical(Pointer p){
        if(p == null) return DragType.NONE;
        if(p.currDragPosition.y > p.lastDragPosition.y) return  DragType.UP;
        if(p.currDragPosition.y < p.lastDragPosition.y) return DragType.DOWN;
        return DragType.NONE;
    }

    public int getDragDirectionHorizontalInt(Pointer p){
        if(p == null) return 0;
        if(p.currDragPosition.x > p.lastDragPosition.x) return 1;
        if(p.currDragPosition.x < p.lastDragPosition.x) return  -1;
        return 0;
    }

    public int getDragDirectionVerticalInt(Pointer p){
        if(p == null) return 0;
        if(p.currDragPosition.y > p.lastDragPosition.y) return  1;
        if(p.currDragPosition.y < p.lastDragPosition.y) return -1;
        return 0;
    }


    public Pointer getFirstAvailablePointer(){
        for(int i = 0; i < this.pointers.length; i++){
            Pointer p;
            if((p = this.pointers[i]) != null) return p;
        }
        return null;
    }

    public Pointer getClosestPointer(float x, float y, float maxdistance){
        ArrayList<Pointer> pointers = this.getPointers();
        if(pointers.size() == 0) return null;
        ArrayList<CollectionStructs.Pair<Pointer,Float>> pointerPairs = new ArrayList<>();
        for(Pointer p : pointers){
            CollectionStructs.Pair<Pointer,Float> pointerPair = new CollectionStructs.Pair<Pointer, Float>();
            pointerPair.object1 = p;
            pointerPair.object2 = Core.MathFunctions.getDistance(x,y,p.x,p.y);
            pointerPairs.add(pointers.indexOf(p),pointerPair);
        }

        int pointerPairsBeforeLen = pointerPairs.size();
        pointerPairs = sortPairsLTG(pointerPairs.size(),pointerPairs); // convert "sort" to use array list
        int pointerPairsAfterLen = pointerPairs.size();

        if(pointerPairsBeforeLen != pointerPairsAfterLen){ // Sanity Check
            LogBot.log(LogBot.Status.WARNING,"Pointer List is of different size");
        }

        for(int i = 0; i < pointerPairs.size(); i++){
            CollectionStructs.Pair<Pointer,Float> pair = pointerPairs.get(i);
            if(pair.object2 <= maxdistance || maxdistance == -1){
                return pair.object1;
            }
        }

        return null;
    }

    // Ripped from FlatPixelCore...
    private ArrayList<CollectionStructs.Pair<Pointer,Float>> sortPairsLTG(int resolution, ArrayList<CollectionStructs.Pair<Pointer,Float>> dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.size() - 1; i++) {
                CollectionStructs.Pair<Pointer,Float> pair_i = dat.get(i);
                CollectionStructs.Pair<Pointer,Float> pair_nxt = dat.get(i + 1);

                // Greatest to least
                if (pair_nxt.object2 < pair_i.object2) {
                    dat.set(i + 1,pair_i);
                    dat.set(i,pair_nxt);
                }
            }
        }
        return dat;
    }

    public interface InputListener{
        void onInputEvent(InputType t, InputManager m);
    }

    /*Bindable input events*/

    public static class InputEventManager implements InputListener {
        private HashMap<String, InputCondition> definedActions = new HashMap<String, InputCondition>();
        private ArrayList<InputEvent> inputEvents = new ArrayList<InputEvent>();
        private String id = UUID.randomUUID().toString();

        public boolean FLAG_VERBOSE_OUTPUT = false;

        public InputEventManager(){
            InputManager.getInstance().addListener(this);
        }

        public void dispose(){
            InputManager.getInstance().removeListener(this);
        }

        public void define(String event, InputCondition c){
            if(!definedActions.containsKey(event)) {
                if(FLAG_VERBOSE_OUTPUT){
                    LogBot.log(LogBot.Status.DEVELOPER, "Define '" + event + "' for " + toString());
                }
                definedActions.put(event,c);
            }
        }

        public int bind(InputEvent e){
            if(FLAG_VERBOSE_OUTPUT) {
                LogBot.log(LogBot.Status.DEVELOPER, "Binding '" + e.name + "' to " + toString());
            }
            inputEvents.add(e);
            return  inputEvents.indexOf(e);
        }

        public String toString(){
            return "InputStateEngine: " + this.id;
        }

        @Override
        public void onInputEvent(InputType t, InputManager m) {
            Object[] keys = this.definedActions.keySet().toArray();
            Object[] vals = this.definedActions.values().toArray();
            for(int i = 0; i < vals.length; i++){
                InputCondition c = (InputCondition) vals[i];
                if(c.testCondition(m,t)){
                    for(InputEvent e : inputEvents){
                        if(e.name == keys[i]){
                            if(FLAG_VERBOSE_OUTPUT){
                                LogBot.log(LogBot.Status.DEVELOPER,"[" + id + "] Bound Event [" + e.name + "] Fired");
                            }
                            e.onEvent(m);
                        }
                    }
                }
            }
        }
    }

    public static abstract class InputCondition{
        public abstract boolean testCondition(InputManager m, InputType t);
    }

    public abstract static class InputEvent{
        public String name = "";
        public InputEvent(String name){
            this.name = name;
        }
        public abstract void onEvent(InputManager m);
    }
}
