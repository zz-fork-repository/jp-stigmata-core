package jp.sourceforge.stigmata.resolvers;

import java.io.File;
import java.io.IOException;

class MacOSXStigmataHomeResolver extends DefaultStigmataHomeResolver{
    @Override
    public String getStigmataHome() throws IOException{
        String home = getUserHome();

        if(home.startsWith("/Users/")){
            home = home + File.separator + "Library/Application Support" + File.separator + "Stigmata";
        }

        return home;
    }

    @Override
    public boolean isTarget(String osName){
        return osName != null && osName.toLowerCase().contains("mac");
    }
}
