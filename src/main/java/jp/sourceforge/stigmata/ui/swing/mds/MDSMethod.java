package jp.sourceforge.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class MDSMethod{
    private Matrix target;
    private Matrix coordinate;
    private Matrix eigenValues;
    private Matrix eigenVectors;
    private int[] indexes;

    public MDSMethod(Matrix matrix){
        this.target = matrix;
    }

    protected Matrix getCenteredInnerProductMatrix(){
        Matrix centering = getCenteringMatrix(target.getColumnDimension());
        Matrix trans = centering.transpose();

        return centering.times(target).times(trans).times(-1);
    }

    public Matrix getEigenValues(){
        if(eigenValues == null){
            getCoordinateMatrix();
        }
        return (Matrix)eigenValues.clone();
    }

    public Matrix getEigenVectors(){
        if(eigenVectors == null){
            getCoordinateMatrix();
        }
        return (Matrix)eigenVectors.clone();
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
        if(coordinate == null){
            Matrix mat = getCenteredInnerProductMatrix();
            EigenvalueDecomposition eigen = mat.eig();
            Matrix eigenVectors = eigen.getV();
            Matrix eigenValues  = eigen.getD();
            Matrix coordinate   = (Matrix)eigenVectors.clone();

            int col = mat.getColumnDimension();
            int row = mat.getRowDimension();
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    double v = coordinate.get(i, j);
                    v = v * Math.sqrt(eigenValues.get(j, j));
                    coordinate.set(i, j, v);
                }
            }
            sortValues(eigenValues);
            this.coordinate = coordinate;
            this.eigenVectors = eigenVectors;
            this.eigenValues  = eigenValues;
        }

        return coordinate;
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
                if(Math.abs(v[i]) < Math.abs(v[j])){
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
}
