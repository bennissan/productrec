// DataParser.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses an internal DataMap class to process a source file into into
// a map from each term in the file to the number of times it appears in the file.
// A term is currently defined as a single word, stripped of surrounding punctuation.

import scala.io.Source
import play.api.libs.json._
import breeze.linalg.DenseMatrix

object DataParser {

	class TermDocumentMatrix(val s: Int) {
		val matrix = collection.mutable.Map[String, DenseMatrix[Int]]()
		val size = s

		def addTerms(terms : Array[String], d: Int) = {
			for (t <- terms) {
				var documents = matrix.getOrElse(t, DenseMatrix.zeros[Int](1, size))
				documents(0, d) += 1
				matrix.update(t, documents)
			}
		}

		def getTerms() : Array[String] = {
			matrix.keys.toArray
		}

		def getCounts() : DenseMatrix[Int] = {
			matrix.values.reduceLeft(DenseMatrix.horzcat(_, _))
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
			val terms = reviewText.replaceAll("[.!?,;:]", "").split(" ")
			tdm.addTerms(terms, i)
			i += 1
		}

		source.close

		return tdm
	}

}
