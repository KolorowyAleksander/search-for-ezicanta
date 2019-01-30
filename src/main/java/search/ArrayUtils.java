package search;

import Jama.Matrix;
import com.google.common.primitives.Doubles;

public class ArrayUtils {

  static Matrix arrayToMatrixInv(double[][] array) {
    return new Matrix(array).transpose();
  }

  public static double[][] matrixToArrayInv(Matrix m) {
    return m.transpose().getArray();
  }

  static void normalizeWordsArray(double[][] wordsMatrix) {
    // normalize array
    for (int i = 0; i < wordsMatrix.length; i++) {
      normalizeQueryVector(wordsMatrix[i]);
    }
  }

  static void normalizeQueryVector(double[] qw) {
    double max = Doubles.max(qw);

    for (int i = 0; i < qw.length; i++) {
      qw[i] /= max;
    }
  }
}
