package jp.naist.se.stigmata.printer;

import java.io.PrintWriter;

interface Printer{
    public void printHeader(PrintWriter out);

    public void printFooter(PrintWriter out);
}
