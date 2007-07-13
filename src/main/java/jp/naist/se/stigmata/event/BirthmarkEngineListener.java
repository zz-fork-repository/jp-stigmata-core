package jp.naist.se.stigmata.event;

/*
 * $Id$
 */

import java.util.EventListener;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface BirthmarkEngineListener extends EventListener{
    public void operationStart(BirthmarkEngineEvent e);

    public void subOperationStart(BirthmarkEngineEvent e);

    public void subOperationDone(BirthmarkEngineEvent e);

    public void operationDone(BirthmarkEngineEvent e);
}
