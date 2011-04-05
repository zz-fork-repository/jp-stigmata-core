package jp.sourceforge.stigmata.hook;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 */
public interface StigmataHook{
    public void onHook(Phase phase, BirthmarkEnvironment env);
}