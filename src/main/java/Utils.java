public class Utils {

    public static String get2DArrayPrintDouble(double[][] matrix) {
        StringBuilder output = new StringBuilder();
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) {
                output.append(aDouble).append("\t");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
