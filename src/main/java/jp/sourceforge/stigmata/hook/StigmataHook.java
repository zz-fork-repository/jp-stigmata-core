package jp.sourceforge.stigmata.hook;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public interface StigmataHook{
    public void onHook(Phase phase, BirthmarkEnvironment env);
}