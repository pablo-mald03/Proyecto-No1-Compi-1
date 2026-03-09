package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questionrecursos;

public enum TipoQuestion {
    OPEN("OPEN_QUESTION"),
    DROP("DROP_QUESTION"),
    SELECT("SELECT_QUESTION"),
    MULTIPLE("MULTIPLE_QUESTION");

    private final String tipo;

    TipoQuestion(String tipo) {
        this.tipo = tipo;
    }

    public String getString() {
        return tipo;
    }
}

/*Created by Pablo*/
