package jp.sourceforge.stigmata;

public interface StigmataCommand{
    public boolean isAvailableArguments(String[] args);

    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args);

    public void setUp(BirthmarkEnvironment env);

    public void tearDown(BirthmarkEnvironment env);

    public String getCommandString();
}
