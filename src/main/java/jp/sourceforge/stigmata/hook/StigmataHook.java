package jp.sourceforge.stigmata.hook;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date
 */
public interface StigmataHook{
    public void onHook(Phase phase, BirthmarkEnvironment env);
}