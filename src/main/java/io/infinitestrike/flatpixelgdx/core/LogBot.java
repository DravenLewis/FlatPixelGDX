package io.infinitestrike.flatpixelgdx.core;

import sun.rmi.runtime.Log;

public class LogBot {

    public enum Status{

        INFO("INFO"),
        DEVELOPER("DEVEL"),
        WARNING("WARN"),
        EXCEPTION("EXPT"),
        ERROR("ERRO"),
        FATAL("FATL"),
        NONE("UNKN");

        private String statusType;
        Status(String type) {this.statusType = type;}
        public String toString() {return this.statusType;}
    }

    public static String log(String message, String... args){
        return LogBot.log(Status.NONE, message, args);
    }

    public static String log(String message){
        return LogBot.log(Status.NONE, message);
    }
    public static String log(String message, Object... args){return  LogBot.log(Status.NONE,message,args);}

    public static String log(Status status, String message){
        String mess = "["+status.toString()+"][FlatPixel] : " + message;
        System.out.println(mess);
        return mess;
    }

    public static String log(Status status, String message, Object... args){
        String mess = "["+status.toString()+"][FlatPixel] : " + String.format(message, args);
        System.out.println(mess);
        return mess;
    }

    public static String logException(Exception e, String message){
        return LogBot.log(Status.EXCEPTION,message + "{" + e.getClass().getName() + ":" + e.getMessage() + "}");
    }

    public static String logException(Exception e, String message, Object... args){
        return LogBot.log(Status.EXCEPTION,message + "{" + e.getClass().getName() + ":" + e.getMessage() + "}", args);
    }
}
