package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import javax.sql.DataSource;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.naist.se.stigmata.result.history.RDBExtractedBirthmarkHistory;
import jp.naist.se.stigmata.spi.ExtractedBirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class RDBExtractedBirthmarkService implements ExtractedBirthmarkSpi{
    private DataSource source;

    public RDBExtractedBirthmarkService(){
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return new RDBExtractionResultSet(context);
    }

    public ExtractedBirthmarkHistory getHistory(String parameter){
        return new RDBExtractedBirthmarkHistory(source);
    }

    public ExtractionResultSet getResultSet(String id){
        return new RDBExtractionResultSet(source, id);
    }

    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.RDB;
    }
}
