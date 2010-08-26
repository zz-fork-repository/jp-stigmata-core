package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkExtractionFailedException;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Abstract class for extracting birthmark.
 * @author  Haruaki TAMADA
 * @version  $Revision$ 
 */
public abstract class AbstractBirthmarkExtractor implements BirthmarkExtractor{
    /**
     * provider.
     */
    private BirthmarkSpi spi;

    /**
     * default constructor.
     * @deprecated this constructor does not support service provider.
     */
    public AbstractBirthmarkExtractor(){
    }

    /**
     * constructor.
     * @param spi service provider.
     */
    public AbstractBirthmarkExtractor(BirthmarkSpi spi){
        this.spi = spi;
    }

    /**
     * returns the provider of this extractor.
     */
    @Override
    public BirthmarkSpi getProvider(){
        return spi;
    }

    /**
     * extract birthmark given stream with given environment.
     */
    @Override
    public final Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), in, context);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    @Override
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(birthmark, new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    @Override
    public final Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given stream with given environment.
     */
    @Override
    public abstract Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException;

    /**
     * create birthmark.
     * @see jp.sourceforge.stigmata.BirthmarkExtractor#createBirthmark()
     */
    @Override
    public Birthmark createBirthmark(){
        return new PlainBirthmark(getProvider().getType());
    }

    @Override
    public abstract ExtractionUnit[] getAcceptableUnits();

    @Override
    public boolean isAcceptable(ExtractionUnit unit){
        ExtractionUnit[] units = getAcceptableUnits();

        for(int i = 0; i < units.length; i++){
            if(units[i] == unit){
                return true;
            }
        }
        return false;
    }
}
