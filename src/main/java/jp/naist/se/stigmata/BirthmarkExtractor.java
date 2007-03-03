package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.InputStream;

/**
 * extract birthmarks from given Java bytecode stream.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkExtractor{
    /**
     * extract birthmark given stream.
     */
    public Birthmark extract(InputStream in) throws BirthmarkExtractionException;

    /**
     * extract birthmark given byte array.
     */
    public Birthmark extract(byte[] bytecode) throws BirthmarkExtractionException;

    /**
     * extract birthmark given stream with given context.
     */
    public Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionException;

    /**
     * extract birthmark given byte array with given context.
     */
    public Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionException;
}
