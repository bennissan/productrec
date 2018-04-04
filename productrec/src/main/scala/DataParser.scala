// DataParser.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses an internal DataMap class to process a source file into into
// a map from each term in the file to the number of times it appears in the file.
// A term is currently defined as a single word, stripped of surrounding punctuation.

import scala.io.Source
import play.api.libs.json._

object DataParser {

	class DataMap {
		val map = collection.mutable.Map[String, Int]()

		def addTerms(terms : Array[String]) = {
			for (t <- terms) {
				var counts = map.getOrElse(t, 0)
				counts += 1
				map.update(t, counts)
			}
		}

		def getTerms() : Array[String] = {
			map.keys.toArray
		}

		def getCounts() : Array[Int] = {
			map.values.toArray
		}
	}

	def parse(sourceFile: String) : DataMap = {
		val dataMap = new DataMap()
		val source = Source.fromFile(sourceFile)
		val reviews = source.getLines
		val numReviews = reviews.size

		source.close
		
		while (reviews.hasNext) {
			val review = reviews.next()
			val json = Json.parse(review)
			val reviewText = Json.stringify((json \ "reviewText").get)
			val terms = reviewText.replaceAll("[.!?,;:]", "").split(" ")
			dataMap.addTerms(terms)
		}

		return dataMap
	}

}
