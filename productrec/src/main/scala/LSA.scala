// this file will use the dataParser's parseFromFileName function to get an array of terms ("termsArray"). 
// By multiplying correlationMatrix = termsArray*transpose(termsArray), we can take the eigenvalues+eigenvectors of this correlationMatrix
// eigenvectors will be the weighting of each individual term's contribution to a certain topic (i.e MusicalInstruments_5)

import breeze.linalg._

object LSA {
	
	def run(sourceFile: String) = {
		val parserInstance = DataParser
		val trainingDataMap = parserInstance.preprocess(sourceFile)
		val trainingTermsArray = parserInstance.getTermsArrayFromMap(trainingDataMap)
		val trainingOccurencesArray = parserInstance.getOccurencesArrayFromMap(trainingDataMap)

		val correlationMatrix = getCorrelationMatrix(DenseVector(trainingOccurencesArray:_*))
		// println(DenseVector(trainingOccurencesArray:_*))
		// return (DenseVector(trainingOccurencesArray:_*))
		// val eigenvectors = getEigenvectors(correlationMatrix)
		// val eigenvalues =
		// getEigenvalues(correlationMatrix)
		// val (er, ei, _) = eig(correlationMatrix)

		// val eig(a)._3
	}

	def getCorrelationMatrix(trainingOccurencesArray: DenseVector[Int]) : DenseMatrix[Int] = {
		println (trainingOccurencesArray.length)
		val correlationMatrix = trainingOccurencesArray * trainingOccurencesArray.t
		println(correlationMatrix)
		return correlationMatrix
	}


	def getEigenvalues(correlationMatrix: DenseMatrix[Int]) = {
		// eig(correlationMatrix)
	}


	def getEigenvectors(correlationMatrix: DenseMatrix[Int]) = {

	}


}

