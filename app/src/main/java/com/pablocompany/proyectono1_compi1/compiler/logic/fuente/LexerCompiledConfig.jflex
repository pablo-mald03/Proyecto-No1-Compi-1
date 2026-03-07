/****************** packages and imports **************/
package com.pablocompany.proyectono1_compi1.compiler.logic.fuente;

/*P*/

import java_cup.runtime.*;
import java.util.*;

/*Mis imports*/
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

%% //separador de area

/********************* Declaraciones de jflex ******************/
%public
%unicode
%class LexerCompiled
%cup
%line
%column
%state STRING

/*P*/
/********************** Estados *********************************/

%init{
    /****************** codigo dentro del constructor ******************/
    errorLexList = new ArrayList<>();
    string = new StringBuilder();

%init}

/********************** Macros ***********************************/
LineTerminator = \r\n|\r|\n
WhiteSpace = [ \t\f]+
Numero = [0-9]+
Decimal = {Numero}"."{Numero}
jletter = [:jletter:]
jletterdigit = [:jletterdigit:]
Id = {jletter}{jletterdigit}*

%{
    /****************** Codigo de usuario (codigo java) ***********************/

    //StringBuilder para cadenas de texto
    private StringBuilder string;

 /*-----------------------------------------------
                   Codigo del lexer
             -------------------------------------------------*/

    private List<ErrorAnalisis> errorLexList;

    public List<ErrorAnalisis> getLexicalErrors(){
        return this.errorLexList;
    }

    private void reportError(String message, String text){
         errorLexList.add(new ErrorAnalisis(text,"Lexico",message,(yyline+1),(yycolumn+1)));
    }

    /*-------------------------- Codigo para el parser --------------------------------*/


     /*-----------------------------------------------
              Codigo para el parser
        -------------------------------------------------*/

    private Symbol symbol(int type, Object object){
        return new Symbol(type, yyline+1, yycolumn+1, object);
    }

    private Symbol symbol(int type){
        return new Symbol(type, yyline+1, yycolumn+1);
    }


    /*Created by Pablo*/
%}


%% //separador de area

/********************** Reglas lexicas **************************/

<YYINITIAL>{

/* Ignorar caracteres invisibles o de control no útiles */

[\u200B\u200C\u200D\uFEFF]        { /* ignorar */ }

/* Ignorar caracteres invisibles o de control no útiles */

[\p{C}&&[^\n\r\t]]  { /* ignorar */ }

/* Espacios y tabs */

{WhiteSpace} { /*ignorar*/ }

/* Saltos de línea */

{LineTerminator} { /*ignorar*/ }


/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

{Decimal}  {return symbol(SymCompiled.DECIMAL, Double.parseDouble(yytext()));}

{Numero} {return symbol(SymCompiled.ENTERO, Integer.parseInt(yytext()));}

{Id} { return symbol(SymCompiled.ID, yytext()); }


}

.               {
                    reportError("Simbolo no existe en el lenguaje", yytext());
                    return symbol(SymCompiled.ERROR, yytext());
                }

<<EOF>>         {
                    return symbol(SymCompiled.EOF);
                }
