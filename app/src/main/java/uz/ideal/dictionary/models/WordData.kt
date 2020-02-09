package uz.ideal.dictionary.models

class WordData(
    var id: Int,
    var word: String,
    var translation: String,
    var description: String,
    var isFav: Int,
    var isSeen: Int,
    var isDel: Int
)