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
public class BirthmarkExtractionException extends Exception {
       private static final long serialVersionUID = 21932436457235L;

       private List<Throwable> causes = new ArrayList<Throwable>();

       public BirthmarkExtractionException() {
               super();
       }

       public BirthmarkExtractionException(String arg0, Throwable cause) {
               super(arg0, cause);
       }

       public BirthmarkExtractionException(String arg0) {
               super(arg0);
       }

       public BirthmarkExtractionException(Throwable cause) {
               super(cause);
       }

       public boolean isFailed(){
               return causes.size() != 0;
       }

       public void addCause(Throwable cause){
               causes.add(cause);
       }

       public void addCauses(Throwable[] causeList){
               for(Throwable throwable: causeList){
                       causes.add(throwable);
               }
       }

       public Throwable[] getCauses(){
               return causes.toArray(new Throwable[causes.size()]);
       }
}