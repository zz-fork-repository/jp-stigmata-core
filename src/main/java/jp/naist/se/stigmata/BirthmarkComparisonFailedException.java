package jp.naist.se.stigmata;

/*
 * $Id$
 */

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class BirthmarkComparisonFailedException extends BirthmarkException{
    private static final long serialVersionUID = 3194872113405859851L;

    public BirthmarkComparisonFailedException(){
    }

    public BirthmarkComparisonFailedException(String message){
        super(message);
    }

    public BirthmarkComparisonFailedException(Throwable cause){
        super(cause);
    }

    public BirthmarkComparisonFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
