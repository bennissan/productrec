
object dataParser{
// this file will allow you to parse http://jmcauley.ucsd.edu/data/amazon/ 5-core json files

	import play.api.libs.json._
	import play.api.libs.json.Reads._
	import play.api.libs.functional.syntax._
	

import scala.io.Source
import java.io._

type dataMap = collection.mutable.Map[String, Int]
// within Sbt's RIPL try running this line and see how the map counts every term's occurence within Musical_Instruments_5.json set of reviews
// dataParser.parseFromFileName("C:/_____/Desktop/productrec/productrec/src/resources/Musical_Instruments_5.json")

	def parseFromFileName(filePath:String):dataMap =  {
		
		// "C:/______/Desktop/productrec/productrec/src/resources/Musical_Instruments_5.json"
		
		// val source: String = Source.fromFile(filePath).getLines.mkString
        var TrainingDataMap = collection.mutable.Map[String, Int]()
		var inputTermsMap = collection.mutable.Map[String, Int]()

        val totalNumberOfReviews = scala.io.Source.fromFile(filePath).getLines.size
		val readmeText : Iterator[String] = 	scala.io.Source.fromFile(filePath).getLines()
		var reviewIndex: Int = 0



		while (readmeText.hasNext) {
			// debugIterator = debugIterator + 1


 			 var lineText =  readmeText.next()
			val json: JsValue = Json.parse(lineText)
			val reviewText = (json \ "reviewText" ).get
			val inputTermsArray = splitTextIntoTermsArray(Json.stringify(reviewText))
			inputTermsMap =  addTermsToTermsMap(inputTermsArray, inputTermsMap, reviewIndex, totalNumberOfReviews)
			reviewIndex = reviewIndex + 1

			}

			TrainingDataMap = inputTermsMap
			return TrainingDataMap 
		}

		def getTermsArrayFromMap( inputTermsMap:dataMap): Array[String] = {
			val pairs = inputTermsMap.toSeq.sortBy(_._1)
			val (keys, vals) = pairs.unzip
			println (keys.toArray)
			return keys.toArray
			
		}

		def getOccurencesArrayFromMap( inputTermsMap:dataMap): Array[Int] = {
			val pairs = inputTermsMap.toSeq.sortBy(_._1)
			val (keys, vals) = pairs.unzip
			println (vals.toArray)
			return vals.toArray
		}

		// import scala.collection.mutable.ArrayBuffer
	def splitTextIntoTermsArray(reviewText:String):Array[String] = {
		// needs improved splitting of text based on exclamations, periods, comas, etcs
		 val termsArray = reviewText.split(" ")
		 return termsArray
	}

	def addTermsToTermsMap(inputTermsArray:Array[String],inputTermsMap:dataMap, reviewIndex:Int, totalNumberOfReviews:Int):dataMap = {
		// iterate through the inputTermsArray and add it to the map of termsArray
		// if there are a total of 4 total documents, then Map should be i.e key = "dog" value = [0, 1, 0, 1] if documents 2 and 4 have the term dog
		for( a <- 0 to inputTermsArray.length-1){

 			 var keyString = inputTermsArray(a)
				if( inputTermsMap.contains( keyString )) {
			     }  else {
			     	val emptyArray = 
			     		inputTermsMap += (keyString ->  0 )
			     		// inputTermsMap += (keyString ->  Array.fill(1)(0) )
			           // inputTermsMap += (keyString ->  Array.fill(1)(0) )
			       		// println(inputTermsMap(keyString)(1))

			      }
			      var mappedValue  = inputTermsMap(keyString)
			     	// mappedValue(reviewIndex) = mappedValue(reviewIndex) + 1
			     	mappedValue = mappedValue + 1
			     	inputTermsMap(keyString) = mappedValue
			     	// println(keyString)
			     	// println(mappedValue)
			}
			return inputTermsMap
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

