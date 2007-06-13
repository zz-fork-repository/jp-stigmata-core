package jp.naist.se.stigmata;

/*
 * $Id$
 */

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ApplicationInitializationError extends Error{
    private static final long serialVersionUID = 32097456654328L;

    public ApplicationInitializationError(){
    }

    public ApplicationInitializationError(String message){
        super(message);
    }

    public ApplicationInitializationError(String message, Throwable cause){
        super(message, cause);
    }

    public ApplicationInitializationError(Throwable cause){
        super(cause);
    }
}
