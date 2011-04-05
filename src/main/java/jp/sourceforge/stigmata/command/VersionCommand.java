package jp.sourceforge.stigmata.command;

import java.util.ResourceBundle;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.Stigmata;

/**
 * 
 * @author Haruaki Tamada
 */
public class VersionCommand extends AbstractStigmataCommand{
    @Override
    public String getCommandString(){
        return "version";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        ResourceBundle helpResource = ResourceBundle.getBundle("resources.options");
        Package p = getClass().getPackage();
        System.out.printf("%s %s%n", helpResource.getString("cli.version.header"), p.getImplementationVersion());
    }
}
