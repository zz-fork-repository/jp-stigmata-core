package jp.sourceforge.stigmata.resolvers;

import java.io.File;
import java.io.IOException;

class WindowsStigmataHomeResolver extends DefaultStigmataHomeResolver{
    @Override
    public String getStigmataHome() throws IOException{
        String home = getUserHome();

        if(home.startsWith("C:\\Documents and Settings\\")){
            home = home + File.separator + "Application Data" + File.separator + "Stigmata";
        }
        return home;
    }

    @Override
    public boolean isTarget(String osName){
        return osName != null && osName.toLowerCase().contains("windows");
    }
}
