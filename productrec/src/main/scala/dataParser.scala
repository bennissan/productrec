
object dataParser{
// ignore this file for now 


// 	val k = 3
// 	val m = 2
// 	val sigma = 0.5

// 	type Label = String
// 	type Term = String
// 	val labels: List[Label] = List("NOUN","VERB","OTHER")
	
// 	import scala.util.parsing.json._

	import play.api.libs.json._
	import play.api.libs.json.Reads._
	import play.api.libs.functional.syntax._
	
	def parseJson(jsonString:String) = {
// refer to here for the example
// https://github.com/playframework/play-json

// 		val jsonString2 = ("""
//   {"name": "Naoki",  "lang": ["Java", "Scala"]}
// """)

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
// returns {"name":"Bigwig","age":6,"role":"Owsla"}


		//  json match {
		//   case Some(e) => println(e) // => Map(name -> Naoki, lang -> List(Java, Scala))
		//   case None => println("Failed.")
		// }


		println(jsonString3)
	}

// 		import scala.collection.mutable.ArrayBuffer
// 	def getLabelsArray(filePath:String) : Array[Term] = {
// 		val readmeText : Iterator[String] = scala.io.Source.fromFile(filePath).getLines()
// 		// var arrayIndex = 0
		
// 		var labelsArray = ArrayBuffer[Label]()
// 		while (readmeText.hasNext) {
// 			  var lineText =  readmeText.next()
// 			  if(lineText.isEmpty()){

// 			  }else{
// 			  var label = getLabelFromLineText(lineText)
// 			  labelsArray += label
// 				}
// 			}

// 			// println(labelsArray)
			
// 			return labelsArray.toArray
// 	}


// 	def getTermsArray(filePath:String) : Array[Label] = {
// 		val readmeText : Iterator[String] = scala.io.Source.fromFile(filePath).getLines()
// 		// var arrayIndex = 0
		
// 		var termsArray = ArrayBuffer[Label]()
// 		while (readmeText.hasNext) {

// 			  var lineText =  readmeText.next()
// 			  if(lineText.isEmpty()){

// 			  }else{
// 			  	var word = getWordFromLineText(lineText)
// 			    termsArray += word
// 			  }
// 			}

// 			// println(termsArray)
			
// 			return termsArray.toArray
// 	}



// 	def getWordFromLineText(lineText:String) :String = {
// 			  val splitArray = lineText.split(" ")
// 			  val word = splitArray(0)
// 			  return word
// 	}

// 	def getLabelFromLineText(lineText:String) :Label = {
// 			  val splitArray = lineText.split(" ")
// 			  val prelabel = splitArray(1)
// 			  // println(prelabel)
// 			  var label = "OTHER"
// 			  if (prelabel.charAt(0).toString == "V"){
// 			  	 label = "VERB"
// 			  }else if(prelabel.charAt(0).toString == "N"){
// 			  	 label = "NOUN"
// 			  }

// 			  return label
// 	}



// 	def trainForThetas() :DenseVector[Double]= {

// 		val lbfgs = new LBFGS[DenseVector[Double]](maxIter=100, m=3)



// 		val f = new DiffFunction[DenseVector[Double]]{
// 			def calculate(x: DenseVector[Double]) = {

// 				println("current ThetaVector = " + x)
// 				(likelihood(x),gradient(x))
// 			}
// 		}


// 		val initialPoint:DenseVector[Double] = DenseVector(0,0)
// 		val optimum = lbfgs.minimize(f,initialPoint)
// 		println(optimum)
// 		return optimum
// 	}

}

