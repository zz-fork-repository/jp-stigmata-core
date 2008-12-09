package jp.sourceforge.stigmata.command;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.StigmataCommand;
import jp.sourceforge.stigmata.hook.Phase;
import jp.sourceforge.stigmata.hook.StigmataHookManager;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public abstract class AbstractStigmataCommand implements StigmataCommand{
    public boolean isAvailableArguments(String[] args){
        return true;
    }

    public void setUp(BirthmarkEnvironment env){
        StigmataHookManager.getInstance().runHook(Phase.SETUP, env);
    }

    public void tearDown(BirthmarkEnvironment env){
        StigmataHookManager.getInstance().runHook(Phase.TEAR_DOWN, env);
    }

    public abstract String getCommandString();
}
