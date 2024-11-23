package br.edu.ifsp.dmo.listadecontatos.model

class Contact(
    private val name: String,
    private val phone: String
) {
    override fun toString() = "Contato { name: '$name', phone: '$phone'"
}