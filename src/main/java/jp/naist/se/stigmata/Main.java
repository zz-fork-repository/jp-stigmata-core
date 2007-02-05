package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import jp.cafebabe.commons.xmlcli.CommandLinePlus;
import jp.cafebabe.commons.xmlcli.OptionsBuilder;
import jp.cafebabe.commons.xmlcli.builder.OptionsBuilderFactory;
import jp.naist.se.stigmata.format.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.format.BirthmarkServiceListFormat;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

/**
 * Front end class.
 *
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class Main{
    private FormatManager manager = FormatManager.getInstance();
    private BirthmarkContext context;

    /**
     * main process.
     */
    public Main(String[] args) throws ParseException{
        Options options = buildOptions();
        CommandLineParser parser = new PosixParser();
        CommandLinePlus commandLine = new CommandLinePlus(parser.parse(options, args, false));
        boolean exitFlag = executeOption(commandLine, options);
        if(!exitFlag){
            Stigmata stigmata = Stigmata.getInstance();
            context = stigmata.getDefaultContext();
            addClasspath(context.getBytecodeContext(), commandLine);
            configuration(context, commandLine.getOptionValue("config-file"));

            String[] birthmarks = getTargetBirthmarks(commandLine);
            String[] arguments = commandLine.getArgs();

            String mode = commandLine.getOptionValue("mode");
            String format = commandLine.getOptionValue("format");

            if(format == null) format = "xml";
            if(mode == null) mode = "gui";

            if(!("gui".equals(mode) || "list".equals(mode))
               && (arguments == null || arguments.length == 0)){
                printHelp(options);
                return;
            }

            if(mode.equals("list")){
                listBirthmarks(stigmata, format);
            }
            else if(mode.equals("extract")){
                extractBirthmarks(stigmata, birthmarks, arguments, format);
            }
            else if(mode.equals("compare")){
                compareBirthmarks(stigmata, birthmarks, arguments, format);
            }
            else if(mode.equals("gui")){
                StigmataFrame frame = new StigmataFrame(stigmata, context);
                frame.setVisible(true);
            }
        }
    }

    /**
     * extract birthmarks.
     */
    private void extractBirthmarks(Stigmata stigmata, String[] birthmarks, String[] args, String format){
        try {
            BirthmarkSet[] holders = stigmata.extract(birthmarks, args, context);

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkExtractionResultFormat formatter = spi.getExtractionResultFormat();
            formatter.printResult(new PrintWriter(System.out), holders);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    private void compareBirthmarks(Stigmata stigmata, String[] birthmarks, String[] args, String format){
        try{
            BirthmarkSet[] holders = stigmata.extract(birthmarks, args, context);
            ComparisonResultSet resultset = stigmata.compare(holders, context);

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkComparisonResultFormat formatter = spi.getComparisonResultFormat();
            formatter.printResult(new PrintWriter(System.out), resultset);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void listBirthmarks(Stigmata stigmata, String format){
        try{
            BirthmarkSpi[] spis = stigmata.createContext().findServices();
            ResultFormatSpi spi = manager.getService(format);
            BirthmarkServiceListFormat formatter = spi.getBirthmarkServiceListFormat();

            formatter.printResult(new PrintWriter(System.out), spis);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void configuration(BirthmarkContext context, String path){
        InputStream target = null;

        if(path == null){
            File file = new File("birthmark.xml");
            if(!file.exists()){
                file = new File(System.getProperty("user.home"), ".birthmark.xml");
                if(!file.exists()){
                    file = null;
                }
            }
            if(file != null){
                try {
                    target = new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    // never throwed this exception;
                    throw new InternalError(ex.getMessage());
                }
            }
        }
        if(target == null){
            target = getClass().getResourceAsStream("/resources/birthmark.xml");
        }

        configuration(context, target);
    }

    private void configuration(BirthmarkContext context, InputStream in){
        try {
            ConfigFileParser parser = new ConfigFileParser(context);
            parser.parse(in);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getTargetBirthmarks(CommandLinePlus cl){
        String[] birthmarks = cl.getOptionValues("birthmark");
        if(birthmarks == null || birthmarks.length == 0){
            birthmarks = new String[] { "cvfv", "uc", "is", "smc", };
        }
        return birthmarks;
    }

    private void addClasspath(ClasspathContext context, CommandLinePlus commandLine){
        String[] classpath = commandLine.getOptionValues("classpath");

        if(classpath != null){
            for(String cp: classpath){
                try {
                    File f = new File(cp);
                    if(f.exists()){
                        context.addClasspath(f.toURI().toURL());
                    }
                } catch (MalformedURLException ex) {
                }
            }
        }
    }

    private boolean executeOption(CommandLinePlus commandLine, Options options){
        boolean exitFlag = false;
        if(commandLine.hasOption("help")){
            printHelp(options);
            exitFlag = true;
        }
        if(commandLine.hasOption("version")){
            printVersion();
            exitFlag = true;
        }
        if(commandLine.hasOption("license")){
            printLicense();
            exitFlag = true;
        }
        return exitFlag;
    }

    private Options buildOptions(){
        try {
            OptionsBuilderFactory factory = OptionsBuilderFactory.getInstance();
            URL location = getClass().getResource("/resources/options.xml");
            OptionsBuilder builder = factory.createBuilder(location);
            Options options = builder.buildOptions();

            return options;
        } catch (DOMException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void printHelp(Options options){
        Package p = getClass().getPackage();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar stigmata-" + p.getImplementationVersion() + ".jar <OPTIONS> <TARGETS>",
                "TARGETS is allowed as jar files, war files, class files, and classpath directory.", options, "");
        System.out.println();
        System.out.println("Copyright (C) by Haruaki Tamada, Ph.D.");
        System.out.println("Please notify us some bugs and requests to <birthmark-analysis@se.aist-nara.ac.jp>");
    }

    private void printLicense(){
        try {
            InputStream in = getClass().getResourceAsStream("/META-INF/license.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void printVersion(){
        Package p = getClass().getPackage();
        System.out.println("stigmata version " + p.getImplementationVersion());
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}
