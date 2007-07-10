package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkExtractionException;
import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Abstract class for extracting birthmark.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
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
    public final Birthmark extract(InputStream in) throws BirthmarkExtractionException{
        return extract(in, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given byte array.
     */
    public final Birthmark extract(byte[] bytecode) throws BirthmarkExtractionException{
        return extract(bytecode, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given stream.
     */
    public final Birthmark extract(Birthmark birthmark, InputStream in) throws BirthmarkExtractionException{
        return extract(birthmark, in, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given byte array.
     */
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode) throws BirthmarkExtractionException{
        return extract(birthmark, bytecode, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given stream with given context.
     */
    public final Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionException{
        return extract(createBirthmark(), in, context);
    }

    /**
     * extract birthmark given byte array with given context.
     */
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionException{
        return extract(birthmark, new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given byte array with given context.
     */
    public final Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionException{
        return extract(createBirthmark(), new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given stream with given context.
     */
    public abstract Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionException;

    /**
     * create birthmark.
     * @see jp.naist.se.stigmata.BirthmarkExtractor#createBirthmark()
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