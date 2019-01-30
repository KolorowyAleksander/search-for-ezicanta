package search

import opennlp.tools.stemmer.PorterStemmer

// uses the Porter stemmer to search.stem a single string
fun stem(input: String): String {
    return PorterStemmer().stem(input)
}