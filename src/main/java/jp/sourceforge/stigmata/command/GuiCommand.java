package jp.sourceforge.stigmata.command;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.Stigmata;
import jp.sourceforge.stigmata.hook.Phase;
import jp.sourceforge.stigmata.hook.StigmataHookManager;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki Tamada
 */
public class GuiCommand extends AbstractStigmataCommand{
    @Override
    public boolean perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        StigmataFrame frame = new StigmataFrame(stigmata, context.getEnvironment());
        frame.setVisible(true);
        return true;
    }

    @Override
    public String getCommandString(){
        return "gui";
    }

    @Override
    public void tearDown(final BirthmarkEnvironment env){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                StigmataHookManager.getInstance().runHook(
                    Phase.TEAR_DOWN, env
                );
            }
        });        
    }
}
