package jp.naist.se.stigmata.hook;

import jp.naist.se.stigmata.BirthmarkContext;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date
 */
public interface StigmataRuntimeHook{
    public void onHook(Phase phase, BirthmarkContext context);
}