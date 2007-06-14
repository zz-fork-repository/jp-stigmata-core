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
import java.util.List;

import jp.cafebabe.commons.xmlcli.CommandLinePlus;
import jp.cafebabe.commons.xmlcli.HelpFormatterPlus;
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
import jp.naist.se.stigmata.utils.ConfigFileExporter;

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
    private BirthmarkContext context;

    /**
     * main process.
     */
    public Main(String[] args) throws ParseException{
        Options options = buildOptions();
        CommandLineParser parser = new PosixParser();
        CommandLinePlus commandLine = new CommandLinePlus(parser.parse(options, args, false));

        Stigmata stigmata = Stigmata.getInstance();
        stigmata.configuration(commandLine.getOptionValue("config-file"));
        context = stigmata.createContext();

        addClasspath(context.getClasspathContext(), commandLine);

        String[] birthmarks = getTargetBirthmarks(commandLine);
        String[] arguments = commandLine.getArgs();

        String mode = commandLine.getOptionValue("mode");
        String format = commandLine.getOptionValue("format");

        if(format == null)
            format = "xml";
        if(mode == null)
            mode = "gui";

        boolean exitFlag = executeOption(commandLine, options);

        if(!exitFlag){
            if(!("gui".equals(mode) || "list".equals(mode)) && (arguments == null || arguments.length == 0)){

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
                String[] filters = null;
                if(commandLine.hasOption("filter")){
                    filters = commandLine.getOptionValues("filter");
                }
                compareBirthmarks(stigmata, birthmarks, filters, arguments, format);
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
    private void extractBirthmarks(Stigmata stigmata, String[] birthmarks,
                                     String[] args, String format){
        try{
            BirthmarkSet[] sets = stigmata.extract(birthmarks, args, context);

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkExtractionResultFormat formatter = spi.getExtractionResultFormat();
            formatter.printResult(new PrintWriter(System.out), sets);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 
     */
    private void compareBirthmarks(Stigmata stigmata, String[] birthmarks,
                                     String[] filters, String[] args, String format){
        try{
            BirthmarkSet[] sets = stigmata.extract(birthmarks, args, context);
            ComparisonResultSet resultset = stigmata.compare(sets, context);
            if(filters != null){
                resultset = stigmata.filter(resultset, filters);
            }

            ResultFormatSpi spi = manager.getService(format);
            BirthmarkComparisonResultFormat formatter = spi.getComparisonResultFormat();
            formatter.printResult(new PrintWriter(System.out), resultset);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void listBirthmarks(Stigmata stigmata, String format){
        try{
            BirthmarkSpi[] spis = stigmata.createContext().findServices();
            ResultFormatSpi spi = manager.getService(format);
            BirthmarkServiceListFormat formatter = spi.getBirthmarkServiceListFormat();

            formatter.printResult(new PrintWriter(System.out), spis);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String[] getTargetBirthmarks(CommandLinePlus cl){
        String[] birthmarks = cl.getOptionValues("birthmark");
        if(birthmarks == null || birthmarks.length == 0){
            List<String> birthmarkList = new ArrayList<String>();
            for(BirthmarkSpi service: context.getServices()){
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
        if(commandLine.hasOption("export-config")){
            exportConfiguration(commandLine.getOptionValue("export-config"));
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

    private void exportConfiguration(String file){
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

            new ConfigFileExporter(context).export(out);
            out.close();
        }catch(IOException e){
        }
    }

    private void printHelp(Options options){
        Package p = getClass().getPackage();
        HelpFormatter formatter = new HelpFormatterPlus();
        System.out.println(options.getClass().getName());
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
        for(BirthmarkSpi service: context.getServices()){
            if(!service.isExpert()){
                System.out.printf("    %-5s (%s): %s%n", service.getType(),
                        service.getDisplayType(), service.getDescription());
            }
        }
        System.out.println();
        System.out.println("Available filers:");
        for(ComparisonPairFilterSet filterset: context.getFilterManager()
                .getFilterSets()){
            System.out.printf("    %s (%s)%n", filterset.getName(), filterset.isMatchAll()? "match all": "match any");
            for(ComparisonPairFilter filter: filterset){
                System.out.printf("        %s%n", filter);
            }
        }
        System.out.println();
        System.out.println("Copyright (C) by Haruaki Tamada, Ph.D.");
        System.out.println("Please notify us some bugs and requests to <birthmark-analysis[ at ]se.aist-nara.ac.jp>");
    }

    private void printLicense(){
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

    private void printVersion(){
        Package p = getClass().getPackage();
        System.out.println("stigmata version " + p.getImplementationVersion());
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}
