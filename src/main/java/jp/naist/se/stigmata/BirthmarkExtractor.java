package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.InputStream;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * extract birthmarks from given Java bytecode stream.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
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
    public Birthmark extract(InputStream in) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given byte array.
     */
    public Birthmark extract(byte[] bytecode) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given stream with given context.
     */
    public Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given byte array with given context.
     */
    public Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given stream and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, InputStream in) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given byte array and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, byte[] bytecode) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given stream with given context and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionException;

    /**
     * extract birthmark from given byte array with given context and add element to given birthmark object.
     */
    public Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionException;
}
