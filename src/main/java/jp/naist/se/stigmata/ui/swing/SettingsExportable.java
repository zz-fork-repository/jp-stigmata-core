package jp.naist.se.stigmata.ui.swing;

import java.io.IOException;
import java.io.PrintWriter;

public interface SettingsExportable{
    public void exportSettings(PrintWriter out) throws IOException;
}
