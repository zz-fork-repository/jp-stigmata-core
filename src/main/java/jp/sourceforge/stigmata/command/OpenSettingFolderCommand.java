package jp.sourceforge.stigmata.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.Stigmata;

public class OpenSettingFolderCommand extends AbstractStigmataCommand{

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            Desktop.getDesktop().open(new File(BirthmarkEnvironment.getStigmataHome()));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandString(){
        return "open-setting-folder";
    }
}
