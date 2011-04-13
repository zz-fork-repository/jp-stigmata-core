package jp.sourceforge.stigmata.spi;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkHistory;

/**
 * This service provider interface manages extracted birthmark histories.
 * 
 * @author Haruaki Tamada
 */
public interface ExtractedBirthmarkService{
    /**
     * finds and returns history from given parameter.
     * @param parameter search base.
     * @return found history.
     */
    public ExtractedBirthmarkHistory getHistory(String parameter);

    public ExtractionResultSet getResultSet(String id);

    public ExtractionResultSet createResultSet(BirthmarkContext context);

    public BirthmarkStoreTarget getTarget();

    public String getType();

    public String getDescription();
}
