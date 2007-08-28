package jp.naist.se.stigmata;

/*
 * $Id$
 */

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class FilterNotFoundException extends BirthmarkException{
    private static final long serialVersionUID = -3981002035876805953L;

    public FilterNotFoundException(){
    }

    public FilterNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public FilterNotFoundException(String message){
        super(message);
    }

    public FilterNotFoundException(Throwable cause){
        super(cause);
    }
}
