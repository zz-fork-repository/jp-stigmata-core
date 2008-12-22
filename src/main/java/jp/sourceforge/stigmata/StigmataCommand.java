package jp.sourceforge.stigmata;

/*
 * $Id$
 */

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$
 */
public interface StigmataCommand{
    public boolean isAvailableArguments(String[] args);

    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args);

    public void perform(Stigmata stigmata, String[] args);

    public void setUp(BirthmarkEnvironment env);

    public void tearDown(BirthmarkEnvironment env);

    public String getCommandString();
}
