// LSA.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file uses DataParser's parse function to process a source file into a map from each term
// in the file to the number of times it appears in the file.  From this map, it obtains an array
// of term counts, using it to build a correlation matrix.  The eigenvalues and eigenvectors of this
// matrix can then be used to weight each individual term's contribution to a certain topic.

import breeze.linalg._

object LSA {
	
	def run(sourceFile: String) = {
		val trainingDataMap = DataParser.parse(sourceFile)
		val trainingCounts = trainingDataMap.getCounts()
		val trainingCountsVector = DenseVector(trainingCounts:_*)
		val correlationMatrix = trainingCountsVector * trainingCountsVector.t
	}

}
