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

		def getTFMatrix() : DenseMatrix[Double] = {
			matrix.values.reduceLeft(DenseMatrix.vertcat(_, _))
		}

		def getTFIDFMatrix() : DenseMatrix[Double] = {
			val tfMatrix = getTFMatrix()
			val occurences = tfMatrix.map(x => if (x > 0) 1d else 0d)
			val idfVector = (sum(occurences(*, ::)) / size.toDouble).map(-1 * log(_))
			diag(idfVector) * tfMatrix
		}


	}

	def parse(sourceFile: String) : TermDocumentMatrix = {
		val source = scala.io.Source.fromFile(sourceFile)
		var reviews = source.getLines.toArray
		val tdm = new TermDocumentMatrix(reviews.size)
		var i = 0
		
		for (review <- reviews) {
			val json = Json.parse(review)
			val reviewText = Json.stringify((json \ "reviewText").get)
			val terms = reviewText.replaceAll("[.!?,;:*\"()~+\\\\/]", " ").toLowerCase.split(" ")
			tdm.addTerms(terms, i)
			i += 1
		}

		source.close

		return tdm
	}

}
