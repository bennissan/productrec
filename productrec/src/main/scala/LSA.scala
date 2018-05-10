// LSA.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses DataParser's parse function to process a source
// file into a term-document matrix.  It then derives the single value
// decomposition of this matrix to obtain a form useful for Latent
// Semantic Analysis and outputs topics obtained by the LSA algorithm.

import DataParser.TermDocumentMatrix

import breeze.linalg.*
import breeze.linalg.DenseMatrix
import breeze.linalg.DenseVector
import breeze.linalg.diag
import breeze.linalg.qr
import breeze.linalg.svd
import breeze.linalg.functions.cosineDistance
import breeze.stats.DescriptiveStats.percentile

object LSA {

    def fastTruncatedSVD(A: DenseMatrix[Double], k: Int)
                      : (DenseMatrix[Double], DenseVector[Double], DenseMatrix[Double]) = {
            val Omega = DenseMatrix.rand(A.cols, k)
            val Q = qr(A * Omega).q(::, 0 until k)
            val svdB = svd(Q.t * A)
            val U = Q * svdB.U
            return (U, svdB.S, svdB.Vt(0 until k, ::))
    }
	
	def getTermTopics(u: DenseMatrix[Double], terms: Array[String], k: Int) : Array[Array[String]] = {
        var termTopics = Array[Array[String]]()

        for (i <- 0 until u.cols) {
            val topic = u(::, i).toArray
            val threshold = percentile(topic.filter(_ >= 0), .995)
            val indices = topic.zipWithIndex.filter(_._1 >= threshold).map(_._2)
            val topicTerms = indices.map(terms)

            termTopics :+= topicTerms
        }

        return termTopics
	}

    def getDocumentTopics(vt: DenseMatrix[Double], k: Int) : Array[Array[Int]] = {
        var documentTopics = Array[Array[Int]]()

        for (i <- 0 until vt.rows) {
            val topic = vt(i, ::).t.toArray
            val threshold = percentile(topic.filter(_ >= 0), .995)
            val indices = topic.zipWithIndex.filter(_._1 >= threshold).map(_._2)

            documentTopics :+= indices
        }

        return documentTopics

    }

    def getTermTopicMatrix(terms: Array[String], termTopics: Array[Array[String]]) : DenseMatrix[Double] = {
        val a = terms.map(term => termTopics.map(topic => if (topic.contains(term)) 1d else 0d))
        return DenseMatrix(a:_*)
    }

    def getTestLSA(u: DenseMatrix[Double], s: DenseVector[Double], q: DenseVector[Double])
                    : DenseVector[Double] = {
        return diag(s.map(x => 1 / x)) * u.t * q
    }

    def getComparisonScores(qk: DenseVector[Double], vt: DenseMatrix[Double]) : DenseVector[Double] = {
        return vt(::, *).map(col => cosineDistance(col, qk)).t
    }

}
