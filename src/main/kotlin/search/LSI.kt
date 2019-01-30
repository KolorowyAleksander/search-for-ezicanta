package search

import Jama.Matrix
import Jama.SingularValueDecomposition

fun lsi(mM: Matrix, mQ: Matrix) {

    val svd = SingularValueDecomposition(mM)
//        For an m-by-n matrix A with m >= n, the singular value decomposition is
//        an m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and
//        an n-by-n orthogonal matrix V so that A = U*S*V'.
//        u -> k
//        s -> s
//        v -> d
    val mK = svd.u
    val mS = svd.s
    val mD = svd.v

    // set number of largest singular values to be considered
    val s = 4

    // cut off appropriate columns and rows from K, S, and D
    val Ss = mS.getMatrix(0, s - 1, 0, s - 1)
    val Ks = mK.getMatrix(0, mK.rowDimension - 1, 0, s - 1)
    val Ds = mD.getMatrix(0, mD.rowDimension - 1, 0, s - 1).transpose()

    // transform the query vector

    val qT = mQ.transpose()

    // compute similarity of the query and each of the documents, using cosine measure
    val qs = qT * Ks * Ss.inverse()
    println("Qs:")
    qs.print(3, 2)
    val qsLen = qs.norm2()
    for (i in 0 until Ds.columnDimension) {
        val dRow = Ds.getMatrix(0, Ds.rowDimension - 1, i, i)
        var prod = 0.0
        for (v in 0 until Ds.rowDimension) {
            prod += dRow.get(v, 0) * qs.get(0, v)
        }
        val sim: Double = prod / (dRow.norm2() * qsLen)
        println("Doc ${i + 1}: ${String.format("%.3f", sim)}")
    }
}