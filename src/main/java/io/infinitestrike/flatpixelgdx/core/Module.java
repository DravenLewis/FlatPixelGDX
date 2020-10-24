package io.infinitestrike.flatpixelgdx.core;

/**
 * Class Module
 *
 * Module is the Super Class for all FlatPixelComponents, if a module fails,
 * then it can be loaded with a new instance and re-started.
 *
 */
public abstract class Module {
    private long moduleVersionID = -1;

    public final void setModuleVersionID(long id){
        this.moduleVersionID = id;
    }

    public final long getModuleVersionID(){
        return this.moduleVersionID;
    }
}
