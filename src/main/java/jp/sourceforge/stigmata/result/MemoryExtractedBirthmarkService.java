package jp.sourceforge.stigmata.result;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.result.history.MemoryExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.spi.ExtractedBirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class MemoryExtractedBirthmarkService implements ExtractedBirthmarkService{
    private MemoryExtractedBirthmarkHistory history = new MemoryExtractedBirthmarkHistory();

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        MemoryExtractionResultSet mers = new MemoryExtractionResultSet(context);
        history.addResultSet(mers);
        return mers;
    }

    @Override
    public ExtractedBirthmarkHistory getHistory(String parameter){
        return history;
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return history.getResultSet(id);
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.MEMORY;
    }

    @Override
    public String getType(){
        return "memory";
    }

    @Override
    public String getDescription(){
        return "Store birthmarks in memory";
    }

}
