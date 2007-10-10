package jp.naist.se.stigmata.result.history;

/*
 * $Id$
 */

import java.util.Iterator;

import jp.naist.se.stigmata.ExtractionResultSet;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface ExtractedBirthmarkHistory extends Iterable<String>{
    /**
     * returns a list of histor ids as iterator.
     */
    public Iterator<String> iterator();

    /**
     * returns an array of history ids.
     */
    public String[] getIds();

    /**
     * returns an extraction result set corresponding id.
     */
    public ExtractionResultSet getExtractionResultSet(String id);

    /**
     * deletes all histories this instance is managed.
     */
    public void deleteAll();

    /**
     * deletes an extraction result set corresponding id.
     */
    public void delete(String id);

    /**
     * refreshes histories.
     */
    public void refresh();
}
