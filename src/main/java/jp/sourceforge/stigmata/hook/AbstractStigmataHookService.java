package jp.sourceforge.stigmata.hook;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.spi.StigmataHookSpi;

/**
 * 
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public abstract class AbstractStigmataHookService implements StigmataHookSpi{
    public StigmataRuntimeHook afterComparison(){
        return null;
    }

    public StigmataRuntimeHook afterExtraction(){
        return null;
    }

    public StigmataRuntimeHook afterFiltering(){
        return null;
    }

    public StigmataRuntimeHook beforeComparison(){
        return null;
    }

    public StigmataRuntimeHook beforeExtraction(){
        return null;
    }

    public StigmataRuntimeHook beforeFiltering(){
        return null;
    }

    public StigmataHook onSetup(){
        return null;
    }

    public StigmataHook onTearDown(){
        return null;
    }
}
