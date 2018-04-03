// this file will allow you to parse http://jmcauley.ucsd.edu/data/amazon/ 5-core json files

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.io.Source
import java.io._

object DataParser {

	type DataMap = collection.mutable.Map[String, Int]
	// within Sbt's RIPL try running this line and see how the map counts every term's occurence within Musical_Instruments_5.json set of reviews
	// dataParser.parseFromFileName("C:/_____/Desktop/productrec/productrec/src/resources/Musical_Instruments_5.json")

	class TermDocumentMatrix (val s : Int) {
		val size = s
		val matrix = collection.mutable.Map[String, Array[Int]]()

		def addDocument(terms : Array[String], i : Int) = {
			for (t <- terms) {
				var occurrences = matrix.getOrElse(t, Array.fill[Int](size)(0))
				occurrences(i) = 1
				matrix.update(t, occurrences)
			}
		}

		def getTerms() : Array[String] = {
			matrix.keys.toArray
		}

		def getOccurences : Array[Array[Int]] = {
			matrix.values.toArray
		}
	}

	def preprocess(sourceFile: String) : DataMap = {
		var trainingDataMap : DataMap = collection.mutable.Map[String, Int]()
		
		val source = Source.fromFile(sourceFile)
		val reviews = source.getLines
		val numReviews = reviews.size
		
		var i = 0

		while (reviews.hasNext) {
			val review = reviews.next()
			val json = Json.parse(review)
			val reviewText = Json.stringify((json \ "reviewText").get)
			val terms = splitTerms(reviewText)
			trainingDataMap = addTermsToTermsMap(terms, trainingDataMap, i, numReviews)
			i += 1
		}

		return trainingDataMap
	}

	// needs improved splitting of text based on exclamations, periods, comas, etcs
	def splitTerms(reviewText: String) : Array[String] = {
		val termsArray = reviewText.split(" ")
		return termsArray
	}

	// iterate through the inputTermsArray and add it to the map of termsArray
	// if there are a total of 4 total documents, then Map should be i.e key = "dog" value = [0, 1, 0, 1] if documents 2 and 4 have the term dog
	def addTermsToTermsMap(inputTermsArray: Array[String], inputTermsMap: DataMap,
		                   reviewIndex: Int, totalNumberOfReviews: Int) : DataMap = {
		
		for(t <- inputTermsArray){
			if (inputTermsMap.contains(t )) {
		     }  else {
		     	val emptyArray = 
		     		inputTermsMap += (t ->  0 )
		     		// inputTermsMap += (t ->  Array.fill(1)(0) )
		           // inputTermsMap += (t ->  Array.fill(1)(0) )
		       		// println(inputTermsMap(t)(1))

		      }
		      var mappedValue  = inputTermsMap(t)
		     	// mappedValue(reviewIndex) = mappedValue(reviewIndex) + 1
		     	mappedValue = mappedValue + 1
		     	inputTermsMap(t) = mappedValue
		     	// println(t)
		     	// println(mappedValue)
			}
			return inputTermsMap
	}

	def getTermsArrayFromMap(inputTermsMap: DataMap): Array[String] = {
		val pairs = inputTermsMap.toSeq.sortBy(_._1)
		val (keys, vals) = pairs.unzip
		println (keys.toArray)
		return keys.toArray
		
	}

	def getOccurencesArrayFromMap(inputTermsMap: DataMap): Array[Int] = {
		val pairs = inputTermsMap.toSeq.sortBy(_._1)
		val (keys, vals) = pairs.unzip
		println (vals.toArray)
		return vals.toArray
	}

	def parseJsonExampleFunction(jsonString:String) = {
// refer to here for the example
// https://github.com/playframework/play-json

		val json: JsValue = Json.parse("""
			{
			  "name" : "Watership Down",
			  "location" : {
			    "lat" : 51.235685,
			    "long" : -1.309197
			  },
			  "residents" : [ {
			    "name" : "Fiver",
			    "age" : 4,
			    "role" : null
			  }, {
			    "name" : "Bigwig",
			    "age" : 6,
			    "role" : "Owsla"
			  } ]
			}
			""")

		// val json: JsValue = Json.parse(jsonString2)
		val jsonString3 = Json.stringify(json)


		val lat = (json \ "location" \ "lat").get
		println(lat)

		val bigwig = (json \ "residents" \ 1).get

		println(jsonString3)
	}


}

