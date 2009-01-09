package jp.sourceforge.stigmata.command;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Stigmata;
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
    /**
     * {@link perform(Stigmata, BirthmarkContext, String[]) <code>perform(stigmata, stigmata.createContext(), args)</code>}.
     */
    public void perform(Stigmata stigmata, String[] args){
        perform(stigmata, stigmata.createContext(), args);
    }

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