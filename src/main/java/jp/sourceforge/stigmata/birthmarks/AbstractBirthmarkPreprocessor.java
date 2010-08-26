package jp.sourceforge.stigmata.birthmarks;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.digger.ClassFileArchive;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

public abstract class AbstractBirthmarkPreprocessor implements BirthmarkPreprocessor{
    private BirthmarkSpi service;

    /**
     * default constructor.
     */
    @Deprecated
    public AbstractBirthmarkPreprocessor(){
    }

    public AbstractBirthmarkPreprocessor(BirthmarkSpi service){
        this.service = service;
    }

    public BirthmarkSpi getProvider(){
        return service;
    }

    @Override
    public abstract void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
