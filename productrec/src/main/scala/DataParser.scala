// DataParser.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses an internal TermDocumentMatrix class to process a
// source file of reviews into a term-document matrix mapping each term
// in the file to its term frequency–inverse document frequency. A term
// is defined as a single word, stripped of surrounding punctuation.
// A document is defined as a single review.

import scala.io.Source
import scala.math.log

import play.api.libs.json._

import breeze.linalg.*
import breeze.linalg.DenseMatrix
import breeze.linalg.diag
import breeze.linalg.sum


object DataParser {

	class TermDocumentMatrix(val s: Int) {
		val matrix = collection.mutable.Map[String, DenseMatrix[Double]]()
		val size = s

		def addTerms(terms : Array[String], d: Int) = {
			for (t <- terms) {
				var documents = matrix.getOrElse(t, DenseMatrix.zeros[Double](1, size))
				documents(0, d) += 1
				matrix.update(t, documents)
			}
		}

		def getTerms() : Array[String] = {
			matrix.keys.toArray
		}

		def getTFSubMatrix(cols: Int) : DenseMatrix[Double] = {
			matrix.values.reduceLeft(DenseMatrix.vertcat(_, _))(::, 0 to cols)
		}

		def getTFIDFSubMatrix(cols: Int) : DenseMatrix[Double] = {
			val tfMatrix = getTFSubMatrix(cols - 1)
			val occurences = tfMatrix.map(x => if (x > 0) 1d else 0d)
			val idfVector = (sum(occurences(*, ::)) / size.toDouble).map(-1 * log(_)).map(x => if (x.isInfinite) 0 else x)
			diag(idfVector) * tfMatrix
		}

		def getTFMatrix() : DenseMatrix[Double] = {
			getTFSubMatrix(size)
		}

		def getTFIDFMatrix() : DenseMatrix[Double] = {
			getTFIDFSubMatrix(size)
		}
	}

	class ReviewData(val s: Int) {
		val tdm = new TermDocumentMatrix(s)
		var asins = Array[String]()
	}

	def parse(trainFile: String, testFile: String) : ReviewData = {
		val train = scala.io.Source.fromFile(trainFile)
		val test = scala.io.Source.fromFile(testFile)

		var trainReviews = train.getLines.toArray
		var testReviews = test.getLines.toArray
		var reviews = trainReviews ++ testReviews
		
		val data = new ReviewData(reviews.size)
		var i = 0
		
		for (review <- reviews) {
			val json = Json.parse(review)
			val asin = Json.stringify((json \ "asin").get)
			val reviewText = Json.stringify((json \ "reviewText").get)
			val terms = reviewText.replaceAll("[.!?,;:*\"()~+\\\\/]", " ").toLowerCase.split(" ")

			data.tdm.addTerms(terms, i)
			data.asins :+= asin
			i += 1
		}

		train.close
		test.close

		return data
	}

}
