package jp.sourceforge.stigmata;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.digger.ClassFileArchive;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public interface BirthmarkPreprocessor{
    public void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
