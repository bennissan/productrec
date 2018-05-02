// LSA.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses DataParser's parse function to process a source
// file into a term-document matrix.  It then derives the single value
// decomposition of this matrix to obtain a form useful for Latent Semantic Analysis.

import breeze.linalg._
import DataParser.TermDocumentMatrix

object LSA {

    def fastTruncatedSVD(A: DenseMatrix[Double], k: Int)
                      : (DenseMatrix[Double], DenseVector[Double], DenseMatrix[Double]) = {
            val Omega = DenseMatrix.rand(A.cols, k)
            val Q = qr(A * Omega).q(::, 0 to k - 1)
            val svdB = svd(Q.t * A)
            val U = Q * svdB.U
            return (U, svdB.S, svdB.Vt(0 to k - 1, ::))
    }
	
	def run(tdm: TermDocumentMatrix) = {
        var k = 1

        val A = tdm.getMatrix

        for (k <- 1 to 100) {
            val (u, s, vt) = fastTruncatedSVD(A, k)
            val svdMatrix = u * diag(s) * vt

            println(A(::, *).map(x => norm(x)) - svdMatrix(::, *).map(x => norm(x)))
            println
        }
	}

}
