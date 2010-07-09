package jp.sourceforge.stigmata.result;

/*
 * $Id$
 */

import javax.sql.DataSource;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.result.history.RDBExtractedBirthmarkHistory;
import jp.sourceforge.stigmata.spi.ExtractedBirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
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
