package jp.naist.se.stigmata.reader;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * abstract presentation of class file archive's location.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ClassFileArchive{
    public URL getLocation();

    public InputStream getInputStream(ClassFileEntry entry) throws IOException;

    public Iterator<ClassFileEntry> entries();

    public boolean hasEntry(String className);

    public ClassFileEntry getEntry(String className) throws ClassNotFoundException;
}
