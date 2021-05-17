package esi.g52854.projet

data class Recette(val id:String ,val titre: String,val difficulty: String,val people: Int,val time: Int,
                   val timeTotal: Int, val steps : List<String>, val ingredients : List<String>)
