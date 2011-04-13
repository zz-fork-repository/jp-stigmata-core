package jp.sourceforge.stigmata;

import jp.sourceforge.stigmata.digger.ClassFileArchive;

/**
 * 
 * @author Haruaki Tamada
 */
public interface BirthmarkPreprocessor{
    public void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
