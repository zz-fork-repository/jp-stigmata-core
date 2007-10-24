package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.io.File;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.naist.se.stigmata.result.history.XmlFileExtractedBirthmarkHistory;
import jp.naist.se.stigmata.spi.ExtractedBirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class XmlFileExtractedBirthmarkService implements ExtractedBirthmarkSpi{
    private File defaultBaseDirectory;

    public XmlFileExtractedBirthmarkService(){
        defaultBaseDirectory = new File(
            BirthmarkEnvironment.getStigmataHome(),
            "extracted_birthmarks"
        );
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return createResultSet(context, defaultBaseDirectory);
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context, File base){
        return new XmlFileExtractionResultSet(context, base);
    }

    public ExtractedBirthmarkHistory getHistory(String parameter){
        File file = defaultBaseDirectory;
        if(parameter != null){
            file = new File(parameter);
        }
        return new XmlFileExtractedBirthmarkHistory(file);
    }

    public ExtractionResultSet getResultSet(String id){
        return new XmlFileExtractionResultSet(new File(id));
    }

    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.XMLFILE;
    }
}
