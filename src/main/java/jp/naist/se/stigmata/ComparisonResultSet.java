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
    /**
     * returns the birthmark environment.
     */
    public BirthmarkEnvironment getEnvironment();

    public BirthmarkContext getContext();

    /**
     * returns a iterator for {@link ComparisonPair <code>ComparisonPair</code>}.
     */
    public Iterator<ComparisonPair> iterator();

    public ComparisonPair getPairAt(int index);

    public ComparisonPair[] getPairs();

    public int getPairCount();

    public BirthmarkSet[] getPairSources();

    public Iterator<BirthmarkSet> pairSources();
}
