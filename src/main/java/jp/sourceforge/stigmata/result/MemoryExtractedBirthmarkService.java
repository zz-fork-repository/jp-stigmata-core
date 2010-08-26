package jp.sourceforge.stigmata.result;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.result.history.MemoryExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.spi.ExtractedBirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public class MemoryExtractedBirthmarkService implements ExtractedBirthmarkSpi{
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

}
