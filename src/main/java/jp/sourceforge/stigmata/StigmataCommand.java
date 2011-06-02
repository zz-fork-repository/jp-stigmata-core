package jp.sourceforge.stigmata;

/**
 *
 * @author Haruaki Tamada
 */
public interface StigmataCommand{
    public String getCommandString();

    public String[] getMessages();

    public int getMessageSize();

    public boolean isAvailableArguments(String[] args);

    public boolean perform(Stigmata stigmata, BirthmarkContext context, String[] args);

    public boolean perform(Stigmata stigmata, String[] args);

    public void putMessage(String message);

    public void setUp(BirthmarkEnvironment env);

    public void tearDown(BirthmarkEnvironment env);
}
