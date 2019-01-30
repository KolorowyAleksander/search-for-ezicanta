import opennlp.tools.stemmer.PorterStemmer

// uses the Porter Stemmer to stem a single string
fun stem(input: String): String {
    return PorterStemmer().stem(input)
}