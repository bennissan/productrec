
object dataParser{
// this file will allow you to parse http://jmcauley.ucsd.edu/data/amazon/ 5-core json files

	import play.api.libs.json._
	import play.api.libs.json.Reads._
	import play.api.libs.functional.syntax._
	

import scala.io.Source
import java.io._


// within Sbt's RIPL try running this line and see how the map counts every term's occurence within Musical_Instruments_5.json set of reviews
// dataParser.parseFromFileName("C:/_____/Desktop/productrec/productrec/src/resources/Musical_Instruments_5.json")

	def parseFromFileName(filePath:String):collection.mutable.Map[String, Array[Int]] =  {
		
		// "C:/______/Desktop/productrec/productrec/src/resources/Musical_Instruments_5.json"
		
		// val source: String = Source.fromFile(filePath).getLines.mkString
        var TrainingDataMap = collection.mutable.Map[String, Array[Int]]() 
		var inputTermsMap = collection.mutable.Map[String, Array[Int]]() 

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


		// import scala.collection.mutable.ArrayBuffer
	def splitTextIntoTermsArray(reviewText:String):Array[String] = {
		// var labelsArray = ArrayBuffer[String]()
		 val termsArray = reviewText.split(" ")
		 return termsArray
	}

	def addTermsToTermsMap(inputTermsArray:Array[String],inputTermsMap:collection.mutable.Map[String, Array[Int]], reviewIndex:Int, totalNumberOfReviews:Int):collection.mutable.Map[String, Array[Int]] = {
		// iterate through the inputTermsArray and add it to the map of termsArray
		// if there are a total of 4 total documents, then Map should be i.e key = "dog" value = [0, 1, 0, 1] if documents 2 and 4 have the term dog
		for( a <- 0 to inputTermsArray.length-1){

 			 var keyString = inputTermsArray(a)
				if( inputTermsMap.contains( keyString )) {
			     }  else {
			     	val emptyArray = 
			     		inputTermsMap += (keyString ->  Array.fill(1)(0) )
			           // inputTermsMap += (keyString ->  Array.fill(1)(0) )
			       		// println(inputTermsMap(keyString)(1))

			      }
			      var valueArray  = inputTermsMap(keyString)
			     	// valueArray(reviewIndex) = valueArray(reviewIndex) + 1
			     	valueArray(0) = valueArray(0) + 1
			     	inputTermsMap(keyString) = valueArray
			     	println(keyString)
			     	println(valueArray(0))
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

