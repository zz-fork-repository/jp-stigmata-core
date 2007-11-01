package jp.naist.se.stigmata.hook;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date
 */
public interface StigmataHook{
    public void onHook(Phase phase, BirthmarkEnvironment env);
}