package jp.sourceforge.stigmata;

import jp.sourceforge.stigmata.digger.ClassFileArchive;

public interface BirthmarkPreprocessor{
    public void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
