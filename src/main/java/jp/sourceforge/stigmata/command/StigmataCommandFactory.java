package jp.sourceforge.stigmata.command;

import java.util.HashMap;
import java.util.Map;

import jp.sourceforge.stigmata.StigmataCommand;

/**
 * 
 * @author Haruaki Tamada
 */
public class StigmataCommandFactory{
    private static final StigmataCommandFactory factory = new StigmataCommandFactory();
    private Map<String, StigmataCommand> commands = new HashMap<String, StigmataCommand>();

    private StigmataCommandFactory(){
        commands.put("compare", new CompareCommand());
        commands.put("export-config", new ExportConfigCommand());
        commands.put("extract", new ExtractCommand());
        commands.put("gui", new GuiCommand());
        commands.put("install", new InstallCommand());
        commands.put("license", new LicenseCommand());
        commands.put("list-birthmarks", new ListBirthmarksCommand());
        /* this command is not supported in Windows OS.
         * Deletion/Renaming is failed because plugin file is locked by system.
         * commands.put("uninstall", new UninstallCommand());
         */
        commands.put("version", new VersionCommand());
    }

    public void registerCommand(String commandString, StigmataCommand command){
        commands.put(commandString, command);
    }

    public static StigmataCommandFactory getInstance(){
        return factory;
    }

    public StigmataCommand getCommand(String command){
        return commands.get(command);
    }
}
