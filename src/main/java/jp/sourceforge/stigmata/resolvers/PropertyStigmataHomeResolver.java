package jp.sourceforge.stigmata.resolvers;

import java.io.IOException;

class PropertyStigmataHomeResolver implements StigmataHomeResolver{
    public String getStigmataHome() throws IOException{
        String stigmataHome = System.getProperty("stigmata.home");
        if(stigmataHome == null){
            stigmataHome = System.getenv("STIGMATA_HOME");
        }
        return stigmataHome;
    }

    public boolean isTarget(String osName){
        return true;
    }
}
