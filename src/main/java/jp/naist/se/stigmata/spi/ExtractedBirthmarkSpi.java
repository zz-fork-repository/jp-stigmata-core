package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkHistory;

/**
 * This service provider interface manages extracted birthmark histories.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface ExtractedBirthmarkSpi{
    /**
     * finds and returns history from given parameter.
     * @param parameter search base.
     * @return found history.
     */
    public ExtractedBirthmarkHistory getHistory(String parameter);

    public ExtractionResultSet getResultSet(String id);

    public ExtractionResultSet createResultSet(BirthmarkContext context);

    public BirthmarkStoreTarget getTarget();
}
