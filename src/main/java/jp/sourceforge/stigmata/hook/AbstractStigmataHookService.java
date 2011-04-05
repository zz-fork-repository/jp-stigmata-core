package jp.sourceforge.stigmata.hook;

import jp.sourceforge.stigmata.spi.StigmataHookSpi;

/**
 * 
 * 
 * @author Haruaki Tamada
 */
public abstract class AbstractStigmataHookService implements StigmataHookSpi{
    @Override
    public StigmataRuntimeHook afterComparison(){
        return null;
    }

    @Override
    public StigmataRuntimeHook afterExtraction(){
        return null;
    }

    @Override
    public StigmataRuntimeHook afterFiltering(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeComparison(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeExtraction(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeFiltering(){
        return null;
    }

    @Override
    public StigmataHook onSetup(){
        return null;
    }

    @Override
    public StigmataHook onTearDown(){
        return null;
    }
}
