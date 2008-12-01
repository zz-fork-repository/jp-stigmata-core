package jp.sourceforge.stigmata;

/*
 * $Id$
 */

import java.io.InputStream;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * extract birthmarks from given Java bytecode stream.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public interface BirthmarkExtractor{
    /**
     * returns service provider interface of this extractor.
     */
    public BirthmarkSpi getProvider();

    /**
     * create new birthmark.
     */
    public Birthmark createBirthmark();

    /**
     * Does extractor accept given extraction unit. 
     */
    public boolean isAcceptable(ExtractionUnit unit);

    /**
     * returns accepted extraction unit list.
     */
    public ExtractionUnit[] getAcceptableUnits();

    /**
     * extract birthmark from given stream.
     */
    public Birthmark extract(InputStream in) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given byte array.
     */
    public Birthmark extract(byte[] bytecode) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given stream with given environment.
     */
    public Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given byte array with given environment.
     */
    public Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given stream and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, InputStream in) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given byte array and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, byte[] bytecode) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given stream with given environment and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException;

    /**
     * extract birthmark from given byte array with given environment and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException;
}
