package jp.naist.se.stigmata.ui.swing.graph;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MultiDimensionalScalingMethod{
    private Matrix target;
    private Matrix coordinate;
    private int[] indexes;

    public MultiDimensionalScalingMethod(Matrix matrix){
        this.target = matrix;
    }

    protected Matrix getCenteredInnerProductMatrix(){
        Matrix centering = getCenteringMatrix(target.getColumnDimension());
        Matrix trans = centering.transpose();

        return centering.times(target).times(trans).times(-1);
    }

    public double[] getCoordinate(int axis){
        if(coordinate == null){
            getCoordinateMatrix();
        }
        double[] v = new double[coordinate.getRowDimension()];
        for(int i = 0; i < v.length; i++){
            v[i] = coordinate.get(i, indexes[axis]);
        }
        return v;
    }

    public Matrix getCoordinateMatrix(){
        Matrix mat = getCenteredInnerProductMatrix();
        EigenvalueDecomposition eigen = mat.eig();
        Matrix eigenVectors = eigen.getV();
        Matrix eigenValues  = eigen.getD();

        for(int i = 0; i < mat.getRowDimension(); i++){
            for(int j = 0; j < mat.getColumnDimension(); j++){
                double v = eigenVectors.get(i, j);
                v = v * Math.sqrt(eigenValues.get(j, j));
                eigenVectors.set(i, j, v);
            }
        }
        sortValues(eigenVectors);
        this.coordinate = eigenVectors;

        return eigenVectors;
    }

    protected static Matrix getCenteringMatrix(int n){
        Matrix matrix = Matrix.identity(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                matrix.set(i, j, matrix.get(i, j) - (1d / n));
            }
        }
        return matrix;
    }

    private void sortValues(Matrix m){
        double[] v = new double[m.getColumnDimension()];
        int[] index = new int[v.length];
        for(int i = 0; i < v.length; i++){
            v[i] = m.get(i, i);
            index[i] = i;
        }

        for(int i = 0; i < v.length; i++){
            for(int j = i + 1; j < v.length; j++){
                if(v[i] < v[j]){
                    double tmpValue = v[j];
                    v[j] = v[i];
                    v[i] = tmpValue;
                    int tmpIndex = index[j];
                    index[j] = index[i];
                    index[i] = tmpIndex;
                }
            }
        }
        indexes = index;
    }

    public static void main(String[] args){
        Matrix matrix = new Matrix(new double[][]{
            {    0,  587, 1212,  701, 1936,  604,  748, 2139, 2182,  543, },
            {  587,    0,  920,  940, 1745, 1188,  713, 1858, 1737,  597, },
            { 1212,  920,    0,  879,  831, 1726, 1631,  949, 1021, 1494, },
            {  701,  940,  879,    0, 1374,  968, 1420, 1645, 1891, 1220, },
            { 1936, 1745,  831, 1374,    0, 2339, 2451,  347,  959, 2300, },
            {  604, 1188, 1726,  968, 2339,    0, 1092, 2592, 2734,  923, },
            {  748,  713, 1631, 1420, 2451, 1092,    0, 2571, 2408,  205, },
            { 2139, 1858,  949, 1645,  347, 2594, 2571,    0,  678, 2442, },
            { 2182, 1737, 1021, 1891,  959, 2734, 2408,  678,    0, 2329, },
            {  543,  597, 1494, 1220, 2300,  923,  205, 2442, 2329,    0, },
        });
        MultiDimensionalScalingMethod mds = new MultiDimensionalScalingMethod(matrix);
        System.out.println(mds.getCoordinateMatrix());
    }
}
