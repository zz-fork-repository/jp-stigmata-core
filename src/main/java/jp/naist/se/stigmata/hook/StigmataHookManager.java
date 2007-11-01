package jp.naist.se.stigmata.hook;

/*
 * $Id$
 */

import java.util.Iterator;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.spi.StigmataHookSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date
 */
public class StigmataHookManager{
    private static final StigmataHookManager manager = new StigmataHookManager();

    /**
     * private constructor for singleton pattern.
     */
    private StigmataHookManager(){
    }

    public static StigmataHookManager getInstance(){
        return manager;
    }

    public void runHook(Phase phase, BirthmarkContext context){
        StigmataRuntimeHook hook = buildHook(phase, context.getEnvironment());
        hook.onHook(phase, context);
    }

    public void runHook(Phase phase, BirthmarkEnvironment env){
        StigmataHook hook = buildHook(phase, env);
        hook.onHook(phase, env);
    }

    private MultipleStigmataHook buildHook(Phase phase, BirthmarkEnvironment env){
        MultipleStigmataHook hooks = new MultipleStigmataHook();

        for(Iterator<StigmataHookSpi> i = env.lookupProviders(StigmataHookSpi.class); i.hasNext(); ){
            StigmataHookSpi service = i.next();

            switch(phase){
            case SETUP:
                hooks.addHook(service.onSetup());
                break;
            case TEAR_DOWN:
                hooks.addHook(service.onTearDown());
                break;
            case BEFORE_EXTRACTION:
                hooks.addRuntimeHook(service.beforeExtraction());
                break;
            case AFTER_EXTRACTION:
                hooks.addRuntimeHook(service.afterExtraction());
                break;
            case BEFORE_COMPARISON:
                hooks.addRuntimeHook(service.beforeComparison());
                break;
            case AFTER_COMPARISON:
                hooks.addRuntimeHook(service.afterComparison());
                break;
            case BEFORE_FILTERING:
                hooks.addRuntimeHook(service.beforeFiltering());
                break;
            case AFTER_FILTERING:
                hooks.addRuntimeHook(service.afterFiltering());
                break;
            default:
                throw new InternalError("invalid phase: " + phase);
            }
        }
        return hooks;
    }
}