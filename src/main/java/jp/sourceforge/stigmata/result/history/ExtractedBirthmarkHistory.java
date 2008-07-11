package jp.sourceforge.stigmata.result.history;

/*
 * $Id$
 */

import java.util.Iterator;

import jp.sourceforge.stigmata.ExtractionResultSet;

/**
 * This interface indicates extracted history management.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public interface ExtractedBirthmarkHistory extends Iterable<String>{
    /**
     * returns a list of histor ids as iterator.
     */
    public Iterator<String> iterator();

    /**
     * returns an array of history ids.
     */
    public String[] getResultSetIds();

    /**
     * returns an extraction result set corresponding id.
     */
    public ExtractionResultSet getResultSet(String id);

    /**
     * deletes all histories this instance is managed.
     */
    public void deleteAllResultSets();

    /**
     * deletes an extraction result set corresponding id.
     */
    public void deleteResultSet(String id);

    /**
     * refreshes histories.
     */
    public void refresh();
}
