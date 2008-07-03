package jp.sourceforge.stigmata.result;

/*
 * $Id$
 */

import java.io.File;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.result.history.XmlFileExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.spi.ExtractedBirthmarkSpi;

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
        return new XmlFileExtractionResultSet(
            context, new File(base, AbstractExtractionResultSet.generateId())
        );
    }

    public ExtractedBirthmarkHistory getHistory(String parameter){
        File file = defaultBaseDirectory;
        if(parameter != null){
            file = new File(parameter);
        }
        if(!file.exists()){
            file.mkdirs();
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
