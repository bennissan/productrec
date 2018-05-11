// Main.scala
// Ben Nissan and Cuong Nguyen
// Natural Language Processing
//
// This file runs Latent Semantic Analysis on the provided source file
// with the provided SVD constant.  To run in SBT: run [filepath] [k],
// where k must be equal to or less than the number of reviews in the
// source file.

object Main extends App {

    val trainFile = args(0)
    val testFile = args(1)
    val k = args(2).toInt

    val data = DataParser.parse(trainFile, testFile)

    val terms = data.tdm.getTerms
    val (u, s, vt) = LSA.fastTruncatedSVD(data.tdm.getTFIDFSubMatrix(data.tdm.size - 1), k)
    
    val termTopics = LSA.getTermTopics(u, terms, k)
    val documentTopics = LSA.getDocumentTopics(vt, k)

    val ttm = LSA.getTermTopicMatrix(terms, termTopics)

    val Aq = data.tdm.getTFIDFMatrix
    val q = Aq(::, Aq.cols - 1)
    val qk = LSA.getTestLSA(ttm, s, q)

    val comparisonScores = LSA.getComparisonScores(qk, vt)

    var i = 0

    for (i <- 0 until termTopics.length) {
        print("Topic %d:".format(i))
        for (term <- termTopics(i)) {
            print(" " + term)
        }
        println
    }

    println

    for (i <- 0 to documentTopics.length - 1) {
        print("Topic %d:".format(i))
        for (document <- documentTopics(i)) {
            print(" D" + document)
        }
        println
    }

    println(comparisonScores)
    println

    val comparisonScoresArray = comparisonScores.toArray
    val min = comparisonScoresArray.sortWith(_ < _)(0)
    println("min: " + min + " at index " + comparisonScoresArray.indexOf(min))
    println

    println("mean: " + breeze.stats.mean(comparisonScores))
    println
    println("sigma: " + breeze.stats.stddev(comparisonScores))
    println

    val recommendation = LSA.getRecommendations(comparisonScores, data.asins)

    println("Based on your review for " + data.asins.last + ", you might like " + recommendation + ".")

}