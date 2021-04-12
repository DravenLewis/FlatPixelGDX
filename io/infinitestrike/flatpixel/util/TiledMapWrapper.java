package io.infinitestrike.flatpixel.util;



import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import io.infinitestrike.flatpixel.core.CameraController;
import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.entity.MapEntity;
import io.infinitestrike.flatpixel.event.EventDispacher;
import io.infinitestrike.flatpixel.grafx.Color;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.Image;
import io.infinitestrike.flatpixel.grafx.View;
import io.infinitestrike.flatpixel.math.Vector;
import io.infinitestrike.flatpixel.state.RenderState;


public class TiledMapWrapper {

    private final RenderState parent;
    private TiledMap m;
    private OrthogonalTiledMapRenderer r;
    private MapProperties props = null;
    private MapLayers layers = null;

    private float scale = 1;

    private View surface = null;

    public Vector.Vector2f position = new Vector.Vector2f();

    private Image renderedMap = null;

    private ArrayList<ArrayList<Rectangle>> rectangleList = new ArrayList<ArrayList<Rectangle>>();
    public ArrayList<ArrayList<TiledMapObject>> objectList = new ArrayList<ArrayList<TiledMapObject>>();
    private EventDispacher disp = EventDispacher.getInstance();

    public TiledMapWrapper(String location, RenderState parent){
        this.parent = parent;
        try{
            OrthographicCamera c = CameraController.getInstance().get2DCamera();
            if(c != null){
                TmxMapLoader loader = new TmxMapLoader();
                TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
                // Load once for accurate data
                params.flipY = false;
                m = loader.load(location,params);

                props = m.getProperties();
                layers = m.getLayers();

                this.loadObjects();
                this.loadCollisionObjects();

                // load again for accurate graphics

                params.flipY = true;
                m = loader.load(location,params);

                r = new OrthogonalTiledMapRenderer(m);

                this.surface = new View(this.getWidth(),this.getHeight());
                this.renderedMap = this.render();

                disp.fire("map_load",this);
            }else{
                throw new IllegalStateException("Current Camera is not type '2D'");
            }
        }catch (Exception e){
            LogBot.logException(e,"Cannot Load Map");
        }
    }

    public Image getRenderedMap(){
        return this.renderedMap;
    };

    public void loadObjects(){
        for(int i = 0; i < this.layers.size(); i++){
            MapLayer layer = this.layers.get(i);
            MapObjects mapObjects = layer.getObjects();
            int c = 0;
            ArrayList<TiledMapObject> r = new ArrayList<TiledMapObject>();
            if((c = mapObjects.getCount()) > 0){
                for(int j = 0; j < c; j++){
                    MapObject o = mapObjects.get(j);
                    MapProperties props = o.getProperties();

                    float x = props.get("x",Float.class);
                    float y = props.get("y",Float.class);
                    float w = props.get("width",Float.class);
                    float h = props.get("height",Float.class);

                    TiledMapObject object = new TiledMapObject(x,y,w,h,this);
                    object.setLayerIndex(i);
                    Iterator<String> keySet = props.getKeys();
                    while(keySet.hasNext()){
                        String key = keySet.next();
                        object.setProperty(key,props.get(key));
                    }
                    r.add(object);
                }
            }
            this.objectList.add(r);
        }
    }

    public void loadCollisionObjects(){
        for(int i = 0; i < this.layers.size(); i++){
            MapLayer layer = this.layers.get(i);
            MapObjects mapObjects = layer.getObjects();
            int c = 0;
            ArrayList<Rectangle> r = new ArrayList<Rectangle>();
            if((c = mapObjects.getCount()) > 0){
                for(int j = 0; j < c; j++){
                    MapObject o = mapObjects.get(j);
                    if(o instanceof RectangleMapObject){
                        RectangleMapObject mobj = (RectangleMapObject) o;
                        Boolean solid = mobj.getProperties().get("solid",Boolean.class);
                        if(solid != null && solid) {
                            r.add(mobj.getRectangle());
                        }
                    }
                }
            }
            this.rectangleList.add(r);
        }
    };

    public RenderState getParent(){
        return this.parent;
    }

    public void draw(Graphics g){

        if(this.parent != null) {
            for (Entity e : this.getParent().getEntityManager().getEntities()) {
                if (e instanceof MapEntity) {
                    MapEntity em = (MapEntity) e;
                    em.setOffsetX((this.position.x + em.getLocalX() * this.scale) - em.getLocalX());
                    em.setOffsetY((this.position.y + em.getLocalY() * this.scale) - em.getLocalY());
                    em.setScale(this.scale);
                }
            }

            for (TiledMapObject tiledMapObject : this.getAllMapObjects()) {
                for (Entity e : this.parent.getEntityManager().getEntities()) {
                    if (e == null || tiledMapObject == null) continue;
                    if (tiledMapObject.getBounds().overlaps(e.getBounds()) ||
                            tiledMapObject.getBounds().contains(e.getBounds())) {
                        disp.fire("walk_on", e, tiledMapObject);
                    }
                }
            }
        }

        if(this.renderedMap != null) {
            g.drawImage(this.renderedMap, (int) position.x, (int) position.y, scale);
        }else{
            this.renderedMap = this.render();
        }
    }

    public Image render(){

        this.surface.begin();

        OrthographicCamera camera = null;
        camera = new OrthographicCamera();
        camera.setToOrtho(true,getWidth(),getHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        Graphics g = this.surface.getGraphics();
        try{
            if(camera != null) {
                r.setView(camera);
                r.render();
                if(parent==null){
                    g.setColor(Color.RED);
                    g.drawString("Invalid Parent", 30,30);
                }
            }else {
                throw new IllegalStateException("Current Camera is not type '2D'");
            }
        }catch (Exception e){
            LogBot.logException(e,"Error While Drawing Map.");
        }
        this.surface.end();

        return this.surface.getImage().getFlippedCopy(false,true);
    }

    public int getHorizontalCellCount(){
        return this.props.get("width",Integer.class);
    }

    public int getVerticalCellCount(){
        return this.props.get("height",Integer.class);
    }

    public int getWidth(){
        return this.props.get("width",Integer.class) * this.props.get("tilewidth",Integer.class);
    }

    public int getHeight(){
        return this.props.get("height",Integer.class) * this.props.get("tileheight",Integer.class);
    }

    public int getTileSizeX(){
        return this.props.get("tilewidth",Integer.class);
    }

    public int getTileSizeY(){
        return  this.props.get("tileheight",Integer.class);
    }

    public void setScale(float scale){
        this.scale =  scale;
    }

    public float getScale(){
        return this.scale;
    }

    /*public void redraw(){
        this.redraw = true;
    }*/

    public Vector.Vector2f getPosition(){
        return this.position;
    }

    public void setPosition(Vector.Vector2f c){
        this.position = c;
    }

    public void setPosition(float x, float y){
        this.setPosition(new Vector.Vector2f(x,y));
    }


    public void translate(float x, float y, float scale){
        Vector.Vector2f pos = this.getPosition();
        pos.x += x * scale;
        pos.y += y * scale;
        this.setPosition(pos);
    }

    public void translate(float x, float y){
        this.translate(x,y,1);
    }


    //=================================================================================
    public TiledMapObject[] getAllMapObjects(){
        ArrayList<TiledMapObject> mapObjects = new ArrayList<TiledMapObject>();

        for(int layer = 0; layer < this.layers.size(); layer++) {
            for (TiledMapObject o : this.objectList.get(layer)) {
                Rectangle rectangle = new Rectangle( // Project from World Space to Screen Space.
                        position.x + (((Float) o.getProperty("x", 0))) * scale,
                        position.y + (((Float) o.getProperty("y", 0))) * scale,
                        (((Float) o.getProperty("width", 0))) * scale,
                        (((Float) o.getProperty("height", 0))) * scale
                );

                o.setBounds(rectangle);
                mapObjects.add(o);
            }
        }

        TiledMapObject[] objects = new TiledMapObject[mapObjects.size()];
        System.arraycopy(mapObjects.toArray(),0,objects,0,mapObjects.size());
        return objects;
    }

    public TiledMapObject[] getMapObjectByName(String name, int layer){
        ArrayList<TiledMapObject> mapObjects = new ArrayList<TiledMapObject>();

        if(layer < 0 || layer > this.objectList.size() -1){
            LogBot.log(LogBot.Status.WARNING,"TiledMapWrapper::getMapObjects(String, int) Invalid index '%s' expected [0 - %s] Size: %s",layer,this.objectList.size() - 1,this.objectList.size());
            return null;
        }

        for(TiledMapObject o : this.objectList.get(layer)){
            if(o.hasProperty("name")){
                if(o.getProperty("name","").equals(name)){
                    Rectangle rectangle = new Rectangle( // Project from World Space to Screen Space.
                            position.x + (((Float) o.getProperty("x", 0))) * scale,
                            position.y + (((Float) o.getProperty("y", 0))) * scale,
                            (((Float) o.getProperty("width", 0))) * scale,
                            (((Float) o.getProperty("height", 0))) * scale
                    );

                    o.setBounds(Core.MathFunctions.flipRectangle(rectangle,false,true));
                    mapObjects.add(o);
                }
            }
        }

        TiledMapObject[] objects = new TiledMapObject[mapObjects.size()];
        System.arraycopy(mapObjects.toArray(),0,objects,0,mapObjects.size());
        return objects;
    }

    public TiledMapObject[] getMapObjectByTag(String tagName){
        ArrayList<TiledMapObject> mapObjects = new ArrayList<TiledMapObject>();

        for(int i = 0; i < this.layers.size(); i++){
            TiledMapObject[] objs = this.getMapObjects(i);
            if(objs != null) {
                for(TiledMapObject o : objs){
                    if(o.hasProperty("tag")){
                        if(o.getProperty("tag","").equals(tagName)){
                            mapObjects.add(o);
                        }
                    }
                }
            }
        }

        TiledMapObject[] objects = new TiledMapObject[mapObjects.size()];
        System.arraycopy(mapObjects.toArray(),0,objects,0,mapObjects.size());
        return objects;
    }

    public TiledMapObject[] getMapObjectByName(String name){
        ArrayList<TiledMapObject> mapObjects = new ArrayList<TiledMapObject>();


        for(int i = 0; i < this.layers.size(); i++){
            TiledMapObject[] objs = getMapObjectByName(name,i);
            if(objs != null) {
                if (objs.length > 0) {
                    mapObjects.addAll(Arrays.asList(objs));
                }
            }
        }

        TiledMapObject[] objects = new TiledMapObject[mapObjects.size()];
        System.arraycopy(mapObjects.toArray(),0,objects,0,mapObjects.size());
        return objects;
    }

    public TiledMapObject[] getMapObjects(int layer){
        ArrayList<TiledMapObject> mapObjects = new ArrayList<TiledMapObject>();

        if(layer < 0 || layer > this.objectList.size() -1){
            LogBot.log(LogBot.Status.ERROR,"TiledMapWrapper::getMapObjects(int) Invalid index '%s' expected [0 - %s] Size: %s",layer,this.objectList.size() - 1,this.objectList.size());
            return null;
        }

        for(TiledMapObject o : this.objectList.get(layer)){
            // Make Changes - to grid
            Rectangle rectangle = new Rectangle( // Project from World Space to Screen Space.
                    position.x + (((Float) o.getProperty("x", 0))) * scale,
                    position.y + (((Float) o.getProperty("y", 0))) * scale,
                    (((Float) o.getProperty("width", 0))) * scale,
                    (((Float) o.getProperty("height", 0))) * scale
            );

            o.setBounds(Core.MathFunctions.flipRectangle(rectangle,false,true));
            mapObjects.add(o);
        }

        mapObjects.addAll(this.objectList.get(layer));

        TiledMapObject[] objects = new TiledMapObject[mapObjects.size()];
        System.arraycopy(mapObjects.toArray(),0,objects,0,mapObjects.size());
        return objects;
    }

    public TiledMapObject[] getMapObjects(String layerName){
        int index = -1;
        for(int i = 0; i < this.layers.size(); i++){
            MapLayer l = this.layers.get(i);
            if(l.getName().toLowerCase().equals(layerName.toLowerCase())){
                index = i;
            }
        }
        return getMapObjects(index);
    }

    public Rectangle[] getLayerCollision(int layer){
        if(layer < 0 || layer >= this.rectangleList.size()){
            LogBot.log(LogBot.Status.WARNING,"Invalid Index '%s' expected [0-%s]", layer, this.rectangleList.size() - 1);
            return null;
        }

        ArrayList<Rectangle> rect = new ArrayList<Rectangle>(this.rectangleList.get(layer));
        TiledMapObject[] objectLayer = this.getMapObjects(layer);
        for(int i = 0; i < objectLayer.length; i++){
            if(objectLayer[i].hasProperty("solid")){
                if(objectLayer[i].getProperty("solid",false,Boolean.class)){
                    TiledMapObject o = objectLayer[i];
                    Rectangle rectangle = new Rectangle( // Project from World Space to Screen Space.
                            position.x + (((Float) o.getProperty("x", 0))) * scale,
                            position.y + (((Float) o.getProperty("y", 0))) * scale,
                            (((Float) o.getProperty("width", 0))) * scale,
                            (((Float) o.getProperty("height", 0))) * scale
                    );
                    rect.add(rectangle);
                }
            }
        }
        Rectangle[] rArray = new Rectangle[rect.size()];
        System.arraycopy(rect.toArray(),0,rArray,0,rArray.length);
        return rArray;
    }

    public Rectangle[] getLayerCollision(String name){
        int index = -1;
        for(int i = 0; i < this.layers.size(); i++){
            MapLayer l = this.layers.get(i);
            if(l.getName().toLowerCase().equals(name.toLowerCase())){
                index = i;
            }
        }
        return getLayerCollision(index); // if no layer;
    };

    public void centerOn(Rectangle rect){
        Rectangle mapBounds = new Rectangle(this.position.x,this.position.y,this.getWidth() * scale,this.getHeight() * scale);

        mapBounds.x = rect.x - (mapBounds.width - rect.width) / 2;
        mapBounds.y = rect.y - (mapBounds.height - rect.height) / 2;

        this.position.x = mapBounds.x;
        this.position.y = mapBounds.y;
    }

    public void dispose(){
        this.r.dispose();
        this.surface.dispose();
        this.renderedMap.dispose();
    }
}
