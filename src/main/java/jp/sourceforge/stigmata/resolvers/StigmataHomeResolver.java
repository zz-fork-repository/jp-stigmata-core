package jp.sourceforge.stigmata.resolvers;

import java.io.IOException;

interface StigmataHomeResolver{
    public boolean isTarget(String osName);

    public String getStigmataHome() throws IOException;
}
