object Main extends App {

    val sourceFile = args(0)
    val k = args(1).toInt

    val tdm = DataParser.parse(sourceFile)

    val termTopics = LSA.getTermTopics(tdm, k)
    val documentTopics = LSA.getDocumentTopics(tdm, k)

    var i = 0

    for (i <- 0 to termTopics.length - 1) {
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

}