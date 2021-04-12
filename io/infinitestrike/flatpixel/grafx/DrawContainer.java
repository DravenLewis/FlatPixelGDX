package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.utils.Disposable;

public abstract class DrawContainer implements Disposable {
    private Image renderImage = null;
    private View renderView = null;
    private boolean isCreated = false;

    public void create(int width, int height){
        this.renderView = new View(width,height);
        this.isCreated = true;
        this.update();
    }

    public void update(){
        if(!this.isCreated){
            throw new IllegalStateException("Draw Containers must be created, call create();");
        }
        this.renderImage = this.render();
    }

    private Image render(){
        if(!this.isCreated){
            throw new IllegalStateException("Draw Containers must be created, call create();");
        }
        this.renderView.begin();
        this.onDraw(this.renderView.getGraphics());
        this.renderView.end();
        return this.renderView.getImage();
    }

    public Image getImage(){
        return this.renderImage;
    }

    public void dispose(){
        if(!this.isCreated){
            throw new IllegalStateException("Draw Containers must be created, call create();");
        }
        this.renderView.dispose();
    }

    public boolean isCreated(){
        return this.isCreated;
    }

    public abstract void onDraw(Graphics graphics);
}
