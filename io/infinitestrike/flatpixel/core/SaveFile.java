package io.infinitestrike.flatpixel.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import java.io.*;
import java.util.*;

public class SaveFile {

    private final String fileName;
    private final HashMap<String,Object> data;


    private SaveFile(String path){
        this.fileName = path;
        this.data = new HashMap<String, Object>();
    }

    private SaveFile(String path, HashMap<String, Object> data){
        this.fileName = path;
        this.data = data;
    }

    public void putValue(String name, Object value){
        this.data.put(name,value);
    }

    public Object getValue(String name, Object def){
        if(data.containsKey(name)){
            return data.get(name);
        }else{
            return def;
        }
    }

    public Object hasKey(String name){
        return this.data.containsKey(name);
    }

    public void save(){
        FileHandle handle = Gdx.files.local(this.fileName);
        File f = handle.file();
        try {
            if (!handle.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.data);
            fos.flush();
            oos.flush();
            fos.close();
            oos.close();
        }catch (Exception e){
            LogBot.logException(e,"Error Saving File");
        }
    }

    public static SaveFile loadFile(String path){
        FileHandle h = Gdx.files.local(path);
        HashMap<String, Object> data = null;
        try {
            FileInputStream fis = new FileInputStream(h.file());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            fis.close();
            ois.close();
            if(o instanceof HashMap){
                data = (HashMap) o;
                return new SaveFile(path,data);
            }
        } catch (Exception e) {
            LogBot.logException(e,"Error Loading File.");
        }
        return null;
    }

    public static SaveFile createFile(String s){
        return new SaveFile(s);
    }
}
