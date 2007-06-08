package jp.naist.se.stigmata.ui.swing.mds;

public class Coordinate{
    private double x, y, z;
    private String label;
    private String showName;
    private int groupId = 0;

    public Coordinate(String label, double x, double y){
        this(label, shortenLabel(label), x, y, Double.NaN);
    }

    public Coordinate(String label, double x, double y, double z){
        this(label, shortenLabel(label), x, y, z);
    }

    public Coordinate(String label, String showName, double x, double y){
        this(label, showName, x, y, Double.NaN);
    }

    public Coordinate(String label, String showName, double x, double y, double z){
        this.label = label;
        this.showName = showName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString(){
        return String.format("%s[%d] (%g, %g)", getLabel(), getGroupId(), getX(), getY());
    }

    public int getGroupId(){
        return groupId;
    }

    public void setGroupId(int groupId){
        this.groupId = groupId;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getShowName(){
        return showName;
    }

    public void setShowName(String showName){
        this.showName = showName;
    }

    public double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getZ(){
        return z;
    }

    public void setZ(double z){
        this.z = z;
    }

    private static String shortenLabel(String label){
        int index = label.lastIndexOf('/');
        int firstIndex = label.indexOf('.');
        int lastIndex = label.lastIndexOf('.');
        int length = label.length();

        if(index < 0 && (firstIndex != lastIndex && lastIndex != (length - 1))){
            index = label.lastIndexOf('.');
        }
        return label.substring(index + 1);
    }
}
