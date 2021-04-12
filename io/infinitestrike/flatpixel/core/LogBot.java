package io.infinitestrike.flatpixel.core;


import java.util.Stack;

public class LogBot {

    private static final Stack<String> logData = new Stack<String>();


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

    public static String log(Object message){
        return LogBot.log(Status.NONE, message);
    }
    public static String log(Object message, Object... args){return  LogBot.log(Status.NONE,message,args);}

    public static String log(Object status, String message){
        String mess = "["+status.toString()+"][FlatPixel] : " + message;
        System.out.println(mess);
        LogBot.logData.push(mess);
        return mess;
    }

    public static String log(Status status, Object message, Object... args){
        String mess = "["+status.toString()+"][FlatPixel] : " + String.format(message + "", args);
        System.out.println(mess);
        LogBot.logData.push(mess);
        return mess;
    }

    public static String logException(Exception e, Object message){
        return LogBot.log(Status.EXCEPTION,message + "{" + e.getClass().getName() + ":" + e.getMessage() + "}");
    }

    public static String logException(Exception e, Object message, Object... args){
        return LogBot.log(Status.EXCEPTION,message + "{" + e.getClass().getName() + ":" + e.getMessage() + "}", args);
    }

    public static Stack<String> getLogData(){
        return LogBot.logData;
    }
}
