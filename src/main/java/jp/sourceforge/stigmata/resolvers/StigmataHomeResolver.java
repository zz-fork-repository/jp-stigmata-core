package jp.sourceforge.stigmata.resolvers;


interface StigmataHomeResolver{
    public boolean isTarget(String osName);

    public String getStigmataHome();
}
