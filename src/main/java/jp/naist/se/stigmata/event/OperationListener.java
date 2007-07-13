package jp.naist.se.stigmata.event;

import java.util.EventListener;

public interface OperationListener extends EventListener{
    public void operationStart(OperationEvent e);

    public void subOperationStart(OperationEvent e);

    public void subOperationDone(OperationEvent e);

    public void operationDone(OperationEvent e);
}
