package jp.sourceforge.stigmata.resolvers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StigmataHomeManager{
    private String home;
    private List<StigmataHomeResolver> resolvers = new ArrayList<StigmataHomeResolver>();

    public StigmataHomeManager(){
        resolvers.add(new DefaultStigmataHomeResolver());
        resolvers.add(new MacOSXStigmataHomeResolver());
        resolvers.add(new WindowsStigmataHomeResolver());
        resolvers.add(new PropertyStigmataHomeResolver());
    }

    public String getStigmataHome(){
        if(home == null){
            String home = null;
            String osName = System.getProperty("os.name");
            for(StigmataHomeResolver resolver: resolvers){
                if(resolver.isTarget(osName)){
                    try{
                        home = resolver.getStigmataHome();
                    } catch(IOException e){
                    }
                }
                if(home != null){
                    this.home = home;
                    break;
                }
            }
        }
        return home;
    }
}
