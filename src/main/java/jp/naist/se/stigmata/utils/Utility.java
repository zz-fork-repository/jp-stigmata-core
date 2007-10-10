package jp.naist.se.stigmata.utils;

import java.io.File;

public class Utility{
    /**
     * no instance is created
     */
    private Utility(){
    }

    public static void deleteDirectory(File dir){
        File[] files = dir.listFiles();
        for(File file: files){
            if(file.isDirectory()){
                deleteDirectory(file);
            }
            else{
                file.delete();
            }
        }
        dir.delete();
    }

    public static String array2String(String[] values){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < values.length; i++){
            if(i != 0)
                builder.append(", ");
            builder.append(values[i]);
        }
        return new String(builder);
    }
}
