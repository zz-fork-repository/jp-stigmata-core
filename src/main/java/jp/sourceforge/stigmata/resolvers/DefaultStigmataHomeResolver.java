package jp.sourceforge.stigmata.resolvers;

import java.io.File;
import java.io.IOException;

class DefaultStigmataHomeResolver implements StigmataHomeResolver{
    public String getStigmataHome() throws IOException{
        return getUserHome() + File.separator + ".stigmata";
    }

    public String getUserHome(){
        String userHome = System.getProperty("user.home");
        if(userHome == null){
            userHome = System.getenv("HOME");
        }
        if(userHome == null){
            userHome = ".";
        }
        return userHome;
    }

    public boolean isTarget(String osName){
        return true;
    }
}
