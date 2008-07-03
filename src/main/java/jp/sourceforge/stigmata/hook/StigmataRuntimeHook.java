package jp.sourceforge.stigmata.hook;

import jp.sourceforge.stigmata.BirthmarkContext;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date
 */
public interface StigmataRuntimeHook{
    public void onHook(Phase phase, BirthmarkContext context);
}