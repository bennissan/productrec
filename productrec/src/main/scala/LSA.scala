// LSA.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses DataParser's parse function to process a source
// file into a term-document matrix.  It then derives the single value
// decomposition of this matrix to obtain a form useful for Latent
// Semantic Analysis and outputs topics obtained by the LSA algorithm.

import breeze.linalg._
import breeze.stats._
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
	
	def getTermTopics(tdm: TermDocumentMatrix, k: Int) : Array[Array[String]] = {
        val terms = tdm.getTerms
        val A = tdm.getTFIDFMatrix
        val (u, s, vt) = fastTruncatedSVD(A, k)
        var termTopics = Array[Array[String]]()

        for (i <- 0 to u.cols - 1) {
            val topic = u(::, i).toArray
            val threshold = DescriptiveStats.percentile(topic.filter(_ >= 0), .99)
            val indices = topic.zipWithIndex.filter(_._1 >= threshold).map(_._2)
            val topicTerms = indices.map(terms)

            termTopics :+= topicTerms
        }

        return termTopics
	}

    def getDocumentTopics(tdm: TermDocumentMatrix, k: Int) : Array[Array[Int]] = {
        val A = tdm.getTFIDFMatrix
        val (u, s, vt) = fastTruncatedSVD(A, k)
        var documentTopics = Array[Array[Int]]()

        for (i <- 0 to vt.rows - 1) {
            val topic = vt(i, ::).t.toArray
            val threshold = DescriptiveStats.percentile(topic.filter(_ >= 0), .8)
            val indices = topic.zipWithIndex.filter(_._1 >= threshold).map(_._2)

            documentTopics :+= indices
        }

        return documentTopics

    }

}
