package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.naist.se.stigmata.result.history.MemoryExtractedBirthmarkHistory;
import jp.naist.se.stigmata.spi.ExtractedBirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class MemoryExtractedBirthmarkService implements ExtractedBirthmarkSpi{
    private MemoryExtractedBirthmarkHistory history = new MemoryExtractedBirthmarkHistory();

    public ExtractionResultSet createResultSet(BirthmarkContext context){
        MemoryExtractionResultSet mers = new MemoryExtractionResultSet(context);
        history.addResultSet(mers);
        return mers;
    }

    public ExtractedBirthmarkHistory getHistory(String parameter){
        return history;
    }

    public ExtractionResultSet getResultSet(String id){
        return history.getExtractionResultSet(id);
    }

    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.MEMORY;
    }

}
