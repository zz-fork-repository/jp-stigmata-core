package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkElementClassNotFoundException extends
               BirthmarkExtractionException {
    private static final long serialVersionUID = 3256723476854L;

    private List<String> classnames = new ArrayList<String>();

    public void addClassName(String name){
       classnames.add(name);
    }

    @Override
    public boolean isFailed(){
       return super.isFailed() || classnames.size() > 0;
    }

    public synchronized String[] getClassNames(){
       return classnames.toArray(new String[classnames.size()]);
    }

    public String getMessage(){
       StringBuffer sb = new StringBuffer();
       boolean first = true;
       for(String value: classnames){
               if(!first){
                       sb.append(", ");
               }
               sb.append(value);
       }
       return new String(sb);
    }
}