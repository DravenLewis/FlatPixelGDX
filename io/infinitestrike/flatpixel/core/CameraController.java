package io.infinitestrike.flatpixel.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;


public class CameraController {

    private static CameraController instance = new CameraController();

    private Camera camera;
    private Viewport viewport;

    public CameraController(){};

    public void make2DGame(){
        this.camera = new OrthographicCamera();
        ((OrthographicCamera) this.camera).setToOrtho(true,this.getGameWidth(),this.getGameHeight());
        this.viewport = new StretchViewport(this.getGameWidth(),this.getGameHeight(),camera);
        this.viewport.apply();
        this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        this.camera.update();
        LogBot.log(LogBot.Status.INFO,"Set game render type to '2D'");
    }

    public void make3DGame(){
        this.camera = new PerspectiveCamera(67,this.getGameWidth(),this.getGameHeight());
        camera.position.set(0, 0, 0);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        this.viewport = new StretchViewport(this.getGameWidth(),this.getGameHeight(),camera);
        this.viewport.apply();
        this.camera.update();
        LogBot.log(LogBot.Status.INFO,"Set game render type to '3D'");
    }

    public void makeCustomGame(Camera c, Viewport v){
        this.camera = c;
        this.viewport = v;
        this.camera.update();
        LogBot.log(LogBot.Status.INFO,"Set game render type to 'Custom'");
    }

    public OrthographicCamera get2DCamera(){
        if(camera == null) return null;
        if(camera instanceof OrthographicCamera){
            return (OrthographicCamera) camera;
        }else{
            LogBot.log(LogBot.Status.WARNING,"Current Camera mode is 3D Call 'make2DGame()'");
            return null;
        }
    }

    public PerspectiveCamera get3DCamera(){
        if(camera == null) return null;
        if(camera instanceof PerspectiveCamera){
            return (PerspectiveCamera) camera;
        }else{
            LogBot.log(LogBot.Status.WARNING,"Current Camera mode is 2D Call 'make3DGame()'");
            return null;
        }
    }

    public Camera getCamera(){
        return this.camera;
    }

    public Viewport getViewport(){
        return this.viewport;
    }

    public int getGameWidth() {
        return Gdx.graphics.getWidth();
    }

    public int getGameHeight() {
        return Gdx.graphics.getHeight();
    }

    public static CameraController getInstance(){
        return CameraController.instance;
    }
}
