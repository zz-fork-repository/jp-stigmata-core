package jp.sourceforge.stigmata.birthmarks;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.digger.ClassFileArchive;
import jp.sourceforge.stigmata.spi.BirthmarkService;

public abstract class AbstractBirthmarkPreprocessor implements BirthmarkPreprocessor{
    private BirthmarkService service;

    /**
     * default constructor.
     */
    @Deprecated
    public AbstractBirthmarkPreprocessor(){
    }

    public AbstractBirthmarkPreprocessor(BirthmarkService service){
        this.service = service;
    }

    public BirthmarkService getProvider(){
        return service;
    }

    @Override
    public abstract void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
