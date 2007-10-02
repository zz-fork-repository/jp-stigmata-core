package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.naist.se.stigmata.event.BirthmarkEngineAdapter;
import jp.naist.se.stigmata.event.BirthmarkEngineEvent;
import jp.naist.se.stigmata.event.WarningMessages;
import jp.naist.se.stigmata.format.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.format.BirthmarkServiceListFormat;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;
import jp.naist.se.stigmata.utils.ConfigFileExporter;
import jp.sourceforge.talisman.xmlcli.CommandLinePlus;
import jp.sourceforge.talisman.xmlcli.OptionsBuilder;
import jp.sourceforge.talisman.xmlcli.builder.OptionsBuilderFactory;

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
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public final class Main{
    private FormatManager manager = FormatManager.getInstance();

    /**
     * main process.
     * @throws org.apache.commons.cli.ParseException 
     */
    public Main(String[] args) throws ParseException{
        Options options = buildOptions();
        CommandLineParser parser = new PosixParser();
        CommandLinePlus commandLine = new CommandLinePlus(parser.parse(options, args, false));

        Stigmata stigmata = Stigmata.getInstance();
        stigmata.configuration(commandLine.getOptionValue("config-file"));

        String[] arguments = commandLine.getArgs();

        String mode = commandLine.getOptionValue("mode");
        String format = commandLine.getOptionValue("format");

        if(format == null){
            format = "xml";
        }
        if(mode == null){
            mode = "gui";
        }
        BirthmarkContext context = stigmata.createContext();
        updateContext(context, commandLine);

        boolean exitFlag = executeOption(context.getEnvironment(), commandLine, options);

        if(!exitFlag){
            if(!("gui".equals(mode) || "list".equals(mode)) && (arguments == null || arguments.length == 0)){

                printHelp(context.getEnvironment(), options);
                return;
            }

            if(mode.equals("list")){
                listBirthmarks(context, format);
            }
            else if(mode.equals("extract")){
                extractBirthmarks(stigmata, arguments, format, context);
            }
            else if(mode.equals("compare")){
                compareBirthmarks(stigmata, arguments, format, context);
            }
            else if(mode.equals("gui")){
                StigmataFrame frame = new StigmataFrame(stigmata, context.getEnvironment());
                frame.setVisible(true);
            }
        }
    }

    /**
     * extract birthmarks.
     */
    private void extractBirthmarks(Stigmata stigmata, String[] args, String format,
            BirthmarkContext context){
        try{
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_SAME_PAIR);
            BirthmarkEngine engine = new BirthmarkEngine(context.getEnvironment());

            engine.addBirthmarkEngineListener(new BirthmarkEngineAdapter(){
                public void operationDone(BirthmarkEngineEvent e){
                    WarningMessages warnings = e.getMessage();
                    for(Iterator<Exception> i = warnings.exceptions(); i.hasNext(); ){
                        i.next().printStackTrace();
                    }
                }
            });
            ExtractionResultSet ers = engine.extract(args, context);

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkExtractionResultFormat formatter = spi.getExtractionResultFormat();
            formatter.printResult(new PrintWriter(System.out), ers);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 
     */
    private void compareBirthmarks(Stigmata stigmata, String[] args, String format,
            BirthmarkContext context){
        try{
            BirthmarkEngine engine = new BirthmarkEngine(context.getEnvironment());
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_SAME_PAIR);
            engine.addBirthmarkEngineListener(new BirthmarkEngineAdapter(){
                public void operationDone(BirthmarkEngineEvent e){
                    WarningMessages warnings = e.getMessage();
                    for(Iterator<Exception> i = warnings.exceptions(); i.hasNext(); ){
                        i.next().printStackTrace();
                    }
                }
            });

            ExtractionResultSet rs = engine.extract(args, context);
            ComparisonResultSet resultset = engine.compare(rs);
            if(context.hasFilter()){
                resultset = engine.filter(resultset);
            }

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkComparisonResultFormat formatter = spi.getComparisonResultFormat();
            formatter.printResult(new PrintWriter(System.out), resultset);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void listBirthmarks(BirthmarkContext context, String format){
        try{
            BirthmarkSpi[] spis = context.getEnvironment().findServices();
            ResultFormatSpi spi = manager.getService(format);
            BirthmarkServiceListFormat formatter = spi.getBirthmarkServiceListFormat();

            formatter.printResult(new PrintWriter(System.out), spis);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updateContext(BirthmarkContext context, CommandLinePlus cl){
        BirthmarkEnvironment env = context.getEnvironment();

        String[] birthmarks = getTargetBirthmarks(env, cl);
        for(int i = 0; i < birthmarks.length; i++){
            context.addBirthmarkType(birthmarks[i]);
        }
        if(cl.hasOption("filter")){
            String[] filters = cl.getOptionValues("filter");
            for(int i = 0; i < filters.length; i++){
                context.addFilterType(filters[i]);
            }
        }
        if(cl.hasOption("extraction-unit")){
            ExtractionUnit unit = ExtractionUnit.valueOf(cl.getOptionValue("extraction-unit"));
            context.setExtractionUnit(unit);
        }

        addClasspath(env.getClasspathContext(), cl);
    }

    private String[] getTargetBirthmarks(BirthmarkEnvironment env, CommandLinePlus cl){
        String[] birthmarks = cl.getOptionValues("birthmark");
        if(birthmarks == null || birthmarks.length == 0){
            List<String> birthmarkList = new ArrayList<String>();
            for(BirthmarkSpi service: env.getServices()){
                if(!service.isExpert()){
                    birthmarkList.add(service.getType());
                }
            }
            birthmarks = birthmarkList.toArray(new String[birthmarkList.size()]);
        }
        return birthmarks;
    }

    private void addClasspath(ClasspathContext context, CommandLinePlus commandLine){
        String[] classpath = commandLine.getOptionValues("classpath");

        if(classpath != null){
            for(String cp: classpath){
                try{
                    File f = new File(cp);
                    if(f.exists()){
                        context.addClasspath(f.toURI().toURL());
                    }
                }catch(MalformedURLException ex){
                }
            }
        }
    }

    private boolean executeOption(BirthmarkEnvironment env, CommandLinePlus commandLine, Options options){
        boolean exitFlag = false;
        if(commandLine.hasOption("help")){
            printHelp(env, options);
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
        if(commandLine.hasOption("export-config")){
            exportConfiguration(env, commandLine.getOptionValue("export-config"));
            exitFlag = true;
        }
        return exitFlag;
    }

    private Options buildOptions(){
        try{
            OptionsBuilderFactory factory = OptionsBuilderFactory.getInstance();
            URL location = getClass().getResource("/resources/options.xml");
            OptionsBuilder builder = factory.createBuilder(location);
            Options options = builder.buildOptions();

            return options;
        }catch(DOMException ex){
            ex.printStackTrace();
        }catch(SAXException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void exportConfiguration(BirthmarkEnvironment env, String file){
        try{
            PrintWriter out;
            if(file == null){
                out = new PrintWriter(System.out);
            }
            else{
                if(!file.endsWith(".xml")){
                    file = file + ".xml";
                }
                out = new PrintWriter(new FileWriter(file));
            }

            new ConfigFileExporter(env).export(out);
            out.close();
        }catch(IOException e){
        }
    }

    public void printHelp(BirthmarkEnvironment env, Options options){
        Package p = getClass().getPackage();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            String.format(
                "java -jar stigmata-%s.jar <OPTIONS> <TARGETS>%n" + 
                "TARGETS is allowed as jar files, war files, class files, and classpath directory.",
                p.getImplementationVersion()
            ),
            options
        );
        System.out.println();
        System.out.println("Available birthmarks:");
        for(BirthmarkSpi service: env.getServices()){
            if(!service.isExpert()){
                System.out.printf("    %-5s (%s): %s%n", service.getType(),
                        service.getDisplayType(), service.getDescription());
            }
        }
        System.out.println();
        System.out.println("Available filers:");
        for(ComparisonPairFilterSet filterset: env.getFilterManager()
                .getFilterSets()){
            System.out.printf("    %s (%s)%n", filterset.getName(), filterset.isMatchAll()? "match all": "match any");
            for(ComparisonPairFilter filter: filterset){
                System.out.printf("        %s%n", filter);
            }
        }
        System.out.println();
        System.out.println("Copyright (C) by Haruaki Tamada, Ph.D.");
        System.out.println("Please notify us some bugs and requests to <stigmata-info[ at ]lists.sourceforge.jp>");
    }

    public void printLicense(){
        try{
            InputStream in = getClass().getResourceAsStream("/META-INF/license.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void printVersion(){
        Package p = getClass().getPackage();
        System.out.println("stigmata version " + p.getImplementationVersion());
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}
