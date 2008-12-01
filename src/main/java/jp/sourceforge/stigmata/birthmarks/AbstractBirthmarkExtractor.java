package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
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
    public BirthmarkSpi getProvider(){
        return spi;
    }

    /**
     * extract birthmark given stream.
     */
    public final Birthmark extract(InputStream in) throws BirthmarkExtractionFailedException{
        return extract(in, new BirthmarkContext(BirthmarkEnvironment.getDefaultEnvironment()));
    }

    /**
     * extract birthmark given byte array.
     */
    public final Birthmark extract(byte[] bytecode) throws BirthmarkExtractionFailedException{
        return extract(bytecode, new BirthmarkContext(BirthmarkEnvironment.getDefaultEnvironment()));
    }

    /**
     * extract birthmark given stream.
     */
    public final Birthmark extract(Birthmark birthmark, InputStream in) throws BirthmarkExtractionFailedException{
        return extract(birthmark, in, new BirthmarkContext(BirthmarkEnvironment.getDefaultEnvironment()));
    }

    /**
     * extract birthmark given byte array.
     */
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode) throws BirthmarkExtractionFailedException{
        return extract(birthmark, bytecode, new BirthmarkContext(BirthmarkEnvironment.getDefaultEnvironment()));
    }

    /**
     * extract birthmark given stream with given environment.
     */
    public final Birthmark extract(InputStream in, BirthmarkContext environment) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), in, environment);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext environment) throws BirthmarkExtractionFailedException{
        return extract(birthmark, new ByteArrayInputStream(bytecode), environment);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    public final Birthmark extract(byte[] bytecode, BirthmarkContext environment) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), new ByteArrayInputStream(bytecode), environment);
    }

    /**
     * extract birthmark given stream with given environment.
     */
    public abstract Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext environment) throws BirthmarkExtractionFailedException;

    /**
     * create birthmark.
     * @see jp.sourceforge.stigmata.BirthmarkExtractor#createBirthmark()
     */
    public Birthmark createBirthmark(){
        return new PlainBirthmark(getProvider().getType());
    }

    public abstract ExtractionUnit[] getAcceptableUnits();

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
