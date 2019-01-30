package search;

import java.util.Arrays;
import java.util.List;

public class PageRankCounter {

    public static void countPageRank(List<Page> pages) {
        int numberOfIterations = 10;
        int numberOfPages = pages.size();

         double[][] stochasticMatrix = new double[numberOfPages][numberOfPages];

        for (int i = 0; i < numberOfPages; i++) {
            Page page = pages.get(i);

            for (int j = 0; j < numberOfPages; j++) {
                Page otherPage = pages.get(j);

                // page is pointed by the other page
                if (otherPage.links.contains(page.url)) {
                    stochasticMatrix[i][j] = 1.0;
                }
            }
        }

        clearDiagonal(stochasticMatrix);
        normalize(stochasticMatrix);

        double q = 0.15;

        double[] pageRankMatrix = new double[numberOfPages];
        Arrays.fill(pageRankMatrix, 1.0);

        for (int z = 0; z < numberOfIterations; z++) {
            double[] pageRankNextStep = new double[numberOfPages];

            for (int i = 0; i < numberOfPages; i++) {
                double modifier = 0.0;

                for (int j = 0; j < numberOfPages; j++) {
                    modifier += pageRankMatrix[j] * stochasticMatrix[i][j];
                }

                pageRankNextStep[i] = q + (1 - q) * modifier;
            }

            pageRankMatrix = pageRankNextStep;
        }


        for (int i = 0; i < numberOfPages; i++) {
            pages.get(i).pageRank = pageRankMatrix[i];
        }
    }

    static void clearDiagonal(double[][] m) {
        for (int i = 0; i < m.length; i++) {
            m[i][i] = 0.0;
        }
    }

    static void normalize(double[][] m) {

        for (int i = 0; i < m.length; i++) {
            int count = 0;
            for (int j = 0; j < m.length; j++) {
                if (m[i][j] != 0) {
                    count++;
                }
            }

            for (int j = 0; j < m.length; j++) {
                m[i][j] /= count;
            }
        }
    }
}
