object Main extends App {

    val sourceFile = args(0)

    val tdm = DataParser.parse(sourceFile)

    LSA.run(tdm)

}