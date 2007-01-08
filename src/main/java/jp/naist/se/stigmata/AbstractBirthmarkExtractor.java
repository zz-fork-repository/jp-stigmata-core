package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Abstract class for extracting birthmark.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
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
    public Birthmark extract(InputStream in) throws IOException{
        return extract(in, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given byte array.
     */
    public Birthmark extract(byte[] bytecode) throws IOException{
        return extract(bytecode, BirthmarkContext.getDefaultContext());
    }

    /**
     * extract birthmark given stream with given context.
     */
    public abstract Birthmark extract(InputStream in, BirthmarkContext context) throws IOException;

    /**
     * extract birthmark given byte array with given context.
     */
    public Birthmark extract(byte[] bytecode, BirthmarkContext context) throws IOException{
        return extract(new ByteArrayInputStream(bytecode), context);
    }
}
