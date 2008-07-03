package jp.sourceforge.stigmata.spi;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.hook.StigmataHook;
import jp.sourceforge.stigmata.hook.StigmataRuntimeHook;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface StigmataHookSpi{
    public StigmataHook onSetup();

    public StigmataHook onTearDown();

    public StigmataRuntimeHook beforeExtraction();

    public StigmataRuntimeHook afterExtraction();

    public StigmataRuntimeHook beforeComparison();

    public StigmataRuntimeHook afterComparison();

    public StigmataRuntimeHook beforeFiltering();

    public StigmataRuntimeHook afterFiltering();
}