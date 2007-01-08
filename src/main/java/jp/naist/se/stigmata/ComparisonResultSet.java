package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.Iterator;

/**
 * result set of birthmark comparison.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ComparisonResultSet extends Iterable<ComparisonPair>{
    public BirthmarkContext getContext();

    public Iterator<ComparisonPair> iterator();

    public int getComparisonCount();
}
