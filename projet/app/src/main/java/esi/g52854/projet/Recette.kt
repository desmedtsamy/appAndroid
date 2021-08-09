package esi.g52854.projet

data class Recette(val id:String ,val titre: String,val difficulty: String,val people: String,val prepaduration: String,
                   val time: String, val steps : List<String>, val ingredients : List<String>, var imageId : String, var imagePath :String) {
}
