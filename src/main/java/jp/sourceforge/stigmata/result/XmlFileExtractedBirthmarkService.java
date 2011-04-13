package jp.sourceforge.stigmata.result;

import java.io.File;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.result.history.XmlFileExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.spi.ExtractedBirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class XmlFileExtractedBirthmarkService implements ExtractedBirthmarkService{
    private File defaultBaseDirectory;

    public XmlFileExtractedBirthmarkService(){
        defaultBaseDirectory = new File(
            BirthmarkEnvironment.getStigmataHome(),
            "extracted_birthmarks"
        );
    }

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return createResultSet(context, defaultBaseDirectory);
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context, File base){
        return new XmlFileExtractionResultSet(
            context, new File(base, AbstractExtractionResultSet.generateId())
        );
    }

    @Override
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

    @Override
    public ExtractionResultSet getResultSet(String id){
        return new XmlFileExtractionResultSet(new File(id));
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.XMLFILE;
    }

    @Override
    public String getType(){
        return "xmlfile";
    }

    @Override
    public String getDescription(){
        return "Store birthmarks into Xml File";
    }
}
