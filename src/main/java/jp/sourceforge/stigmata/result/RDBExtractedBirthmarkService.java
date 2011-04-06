package jp.sourceforge.stigmata.result;

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
 */
public class RDBExtractedBirthmarkService implements ExtractedBirthmarkSpi{
    private DataSource source;

    public RDBExtractedBirthmarkService(){
    }

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return new RDBExtractionResultSet(context);
    }

    @Override
    public ExtractedBirthmarkHistory getHistory(String parameter){
        return new RDBExtractedBirthmarkHistory(source);
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return new RDBExtractionResultSet(source, id);
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.RDB;
    }
}
