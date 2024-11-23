package br.edu.ifsp.dmo.listadecontatos.model

class Contact(
    val name: String,
    val phone: String
) {
    override fun toString() = "Contato { name: '$name', phone: '$phone'"
}