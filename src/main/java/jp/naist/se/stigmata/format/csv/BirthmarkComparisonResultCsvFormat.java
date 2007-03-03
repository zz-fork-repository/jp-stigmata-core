package jp.naist.se.stigmata.format.csv;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairElement;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.format.AbstractBirthmarkComparisonResultFormat;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkComparisonResultCsvFormat extends AbstractBirthmarkComparisonResultFormat{
    private BirthmarkExtractionResultCsvFormat list;

    public BirthmarkComparisonResultCsvFormat(BirthmarkExtractionResultCsvFormat list){
        this.list = list;
    }

    @Override
    public void printResult(PrintWriter out, ComparisonPair pair){
        list.printBirthmarkHolder(out, pair.getTarget1());
        list.printBirthmarkHolder(out, pair.getTarget2());

        for(ComparisonPairElement element: pair){
            out.print("compare,");
            out.print(element.getType());
            out.print(",");
            out.println(element.getSimilarity());
        }
        out.flush();
    }

    @Override
    public void printResult(PrintWriter out, ComparisonResultSet resultset){
        if(resultset instanceof CertainPairComparisonResultSet){
            printResultImpl(out, (CertainPairComparisonResultSet)resultset);
        }
        else{
            printResultImpl(out, resultset);
        }
    }

    private void printResultImpl(PrintWriter out, CertainPairComparisonResultSet resultset){
        for(ComparisonPair pair: resultset){
            out.print(pair.getTarget1().getClassName());
            out.print(",");
            out.print(pair.getTarget1().getClassName());
            out.print(",");
            out.println(pair.calculateSimilarity());
        }
    }

    private void printResultImpl(PrintWriter out, ComparisonResultSet resultset){
        Map<String, Map<String, Double>> map = new LinkedHashMap<String, Map<String, Double>>();
        List<String> names = new ArrayList<String>();

        for(ComparisonPair pair: resultset){
            Map<String, Double> val = map.get(pair.getTarget1().getClassName());
            if(val == null){
                val = new HashMap<String, Double>();
            }
            val.put(pair.getTarget2().getClassName(), new Double(pair.calculateSimilarity()));
            if(!names.contains(pair.getTarget2().getClassName())){
                names.add(pair.getTarget2().getClassName());
            }
            map.put(pair.getTarget1().getClassName(), val);
        }

        for(String name: names){
            out.print(",");
            out.print(name);
        }
        out.println();
        for(String key: map.keySet()){
            Map<String, Double> element = map.get(key);
            out.print(key);
            for(String name: names){
                out.print(",");
                Double v = element.get(name);
                if(v != null){
                    out.print(v.doubleValue());
                }
            }
            out.println();
        }
        out.flush();
    }
}
