/********************** paquetes y otros ***********************/
package com.pablocompany.proyectono1_compi1.compiler.logic.formulario;

/*P*/

import java_cup.runtime.*;
import java.util.*;

/*Mis imports*/
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

%% //separador de area

/********************* Declaraciones de jflex ******************/
%public
%unicode
%class LexerForms
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
HexColor = "#"[0-9A-Fa-f]{6}

ComentarioBloque = "/*" ( [^*] | "*"+ [^/*] )* "*"+ "/"

%{
    /****************** Codigo de usuario (codigo java) ***********************/

    //StringBuilder para cadenas de texto
    private StringBuilder string;

    //Variable que permite ignorar espacios en el modo parser
    private boolean modoParser = false;

    //Constructor sobreescrito
    public LexerForms(java.io.Reader in, boolean modoParser) {
        this(in);
        this.modoParser = modoParser;
    }

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

/*=========APARTADO DE ER QUE IGNORAN O REPRESENTAN ESPACIOS EL CODIGO==========*/

/* Ignorar caracteres invisibles o de control no útiles */

[\u200B\u200C\u200D\uFEFF]        { /* ignorar */ }

/* Ignorar caracteres invisibles o de control no útiles */

[\p{C}&&[^\n\r\t]]      { /* ignorar */ }

/* Espacios y tabs */

{WhiteSpace}    {   if(!modoParser){
                        return symbol(sym.WHITESPACE, yytext());
                    }
                    /* ignorar */
                }

/* Saltos de línea */

{LineTerminator}    {   if(!modoParser){
                            return symbol(sym.NEWLINE, yytext());
                        }
                        /* ignorar */
                    }


/*--Comentarios--*/
"$".*      {    if(!modoParser){
                    return symbol(sym.COMENTARIO_TEXTO, yytext());
                }
                /* ignorar */
            }

{ComentarioBloque}      {   if(!modoParser){
                                return symbol(sym.COMENTARIO_TEXTO, yytext());
                            }
                        }


/*=========FIN DEL APARTADO DE ER QUE IGNORAN O REPRESENTAN ESPACIOS EL CODIGO==========*/

/*---RECONOCIMIENTO DE COLORES---*/

{HexColor} { return symbol(sym.COLOR_HEX, yytext()); }
"RED"       { return symbol(sym.COLOR_PRESET, yytext()); }
"BLUE"      { return symbol(sym.COLOR_PRESET, yytext()); }
"GREEN"     { return symbol(sym.COLOR_PRESET, yytext()); }
"PURPLE"    { return symbol(sym.COLOR_PRESET, yytext()); }
"SKY"       { return symbol(sym.COLOR_PRESET, yytext()); }
"YELLOW"    { return symbol(sym.COLOR_PRESET, yytext()); }
"BLACK"     { return symbol(sym.COLOR_PRESET, yytext()); }
"WHITE"     { return symbol(sym.COLOR_PRESET, yytext()); }

/*---FIN DEL RECONOCIMIENTO DE COLORES---*/

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES ARITMETICOS EN EL LENGUAJE==========*/

"+" {return symbol(sym.SUMA, yytext());}

"-" {return symbol(sym.RESTA, yytext());}

"*" {return symbol(sym.MULTIPLICACION, yytext());}

"/" {return symbol(sym.DIVISION, yytext());}

"^" {return symbol(sym.POTENCIA, yytext());}

"%" {return symbol(sym.MODULO, yytext());}

"("       {return symbol(sym.PARENT_APERTURA, yytext());}

")"       {return symbol(sym.PARENT_CIERRE, yytext());}

/*=========FIN DEL APARTADO DE ER QUE REPRESENTAN OPERADORES ARITMETICOS EN EL LENGUAJE==========*/
/*Created by Pablo*/

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE COMPARACION EN EL LENGUAJE==========*/

"=="    {return symbol(sym.IGUALDAD, yytext());}

"!!" {return symbol(sym.DIFERENTE, yytext());}

">"    {return symbol(sym.MAYOR, yytext());}

"<"    {return symbol(sym.MENOR, yytext());}

">="    {return symbol(sym.MAYOR_IGUAL, yytext());}

"<="    {return symbol(sym.MENOR_IGUAL, yytext());}

/*=========FIN DEL APARTADO DE ER QUE REPRESENTAN OPERADORES DE COMPARACION EN EL LENGUAJE==========*/

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE LOGICOS EN EL LENGUAJE==========*/

"&&"    {return symbol(sym.AND, yytext());}

"||"    {return symbol(sym.OR, yytext());}

"~"    {return symbol(sym.NOT, yytext());}

/*=========FIN DEL APARTADO DE ER QUE REPRESENTAN OPERADORES DE LOGICOS EN EL LENGUAJE==========*/


/*=========APARTADO DE ER QUE REPRESENTAN LOS TIPOS DE VARIABLES EN EL LENGUAJE==========*/

"number"        {return symbol(sym.VAR_NUMERO, yytext());}

"string"        {return symbol(sym.VAR_STRING, yytext());}

"special"       {return symbol(sym.VAR_ESPECIAL, yytext());}


/*=========FIN DEL APARTADO DE ER QUE REPRESENTAN LOS TIPOS DE VARIABLES EN EL LENGUAJE==========*/

/*=========APARTADO DE ER QUE REPRESENTAN LOS LAS DECLARACIONES DE VARIABLES O TIENEN ALGUN CONTEXTO DE ASIGNACION EN EL LENGUAJE==========*/

"="    {return symbol(sym.IGUALACION, yytext());}

"?"    {return symbol(sym.COMODIN, yytext());}

","    {return symbol(sym.COMA, yytext());}

"["    {return symbol(sym.CORCHETE_APERTURA, yytext());}

"]"    {return symbol(sym.CORCHETE_CIERRE, yytext());}

"{"    {return symbol(sym.LLAVE_APERTURA, yytext());}

"}"    {return symbol(sym.LLAVE_CIERRE, yytext());}

"."    {return symbol(sym.PUNTO, yytext());}

":"    {return symbol(sym.DOS_PUNTOS, yytext());}


/*====***===APARTADO DE PALABRAS RESERVADAS DEL LENGUAJE====**====*/

"IF"    {return symbol(sym.IF, yytext());}

"ELSE"    {return symbol(sym.ELSE, yytext());}

"WHILE"    {return symbol(sym.WHILE, yytext());}

"FOR"    {return symbol(sym.FOR, yytext());}

"DO"    {return symbol(sym.DO, yytext());}


/*===---****ATRIBUTO ESPECIAL DE LA SEGUNDA MANERA DEL FOR***---====*/
"in"    {return symbol(sym.IN, yytext());}


/*Created by Pablo*/
/*=========FIN DEL APARTADO DE ER QUE REPRESENTAN LOS LAS DECLARACIONES DE VARIABLES O TIENEN ALGUN CONTEXTO DE ASIGNACION EN EL LENGUAJE==========*/

/*=========APARTADO EN EL QUE SE REGISTRAN LAS PALABRAS QUE CONFIGURAN ESTILOS (COMPONENTES INICIALES)==========*/

"width"         {return symbol(sym.WIDTH, yytext());}

"height"         {return symbol(sym.HEIGHT, yytext());}

"label"         {return symbol(sym.LABEL, yytext());}

"pointX"         {return symbol(sym.POINT_X, yytext());}

"pointY"         {return symbol(sym.POINT_Y, yytext());}

"orientation"         {return symbol(sym.ORIENTATION, yytext());}

"elements"         {return symbol(sym.ELEMENTS, yytext());}

/*--Define estilos--*/
"styles"    {return symbol(sym.STYLES, yytext());}

/*Define el contenido de un texto*/
"content"    {return symbol(sym.CONTENT, yytext());}


/*=========FIN DEL APARTADO EN EL QUE SE REGISTRAN LAS PALABRAS QUE CONFIGURAN ESTILOS==========*/



/*------EXPRESIONES CON UN CONTEXTO ESPECIAL PARA LOS ESTILOS DEL CODIGO COMPILADO-------*/

"\"color\""             {return symbol(sym.BACKGROUND_COLOR, yytext());}

"\"background color\""        {return symbol(sym.COLOR_TEXTO, yytext());}

"\"font family\""              {return symbol(sym.FONT_FAMILY, yytext());}

"\"text size\""         {return symbol(sym.TEXT_SIZE, yytext());}

"\"border\""         {return symbol(sym.BORDER, yytext());}


/*------FIN DE LAS EXPRESIONES CON UN CONTEXTO ESPECIAL PARA LOS ESTILOS DEL CODIGO COMPILADO-------*/


/*------EXPRESIONES CON UN CONTEXTO ESPECIAL PARA GENERACION DEL CODIGO COMPILADO-------*/

"draw"      {return symbol(sym.DRAW, yytext());}

"who_is_that_pokemon" {return symbol(sym.WHO_IS_THAT_POKEMON, yytext());}

"NUMBER"        {return symbol(sym.NUMBER_REQUEST, yytext());}

/*---CONFIGURACIONES DE DOCK---*/
"VERTICAL"      {return symbol(sym.CONFIG_DOCK, yytext());}

"HORIZONTAL"    {return symbol(sym.CONFIG_DOCK, yytext());}

/*Created by Pablo*/
/*---TIPOGRAFIAS DE LETRAS---*/

"MONO"      {return symbol(sym.TIPOGRAFIA, yytext());}

"SANS_SERIF"      {return symbol(sym.TIPOGRAFIA, yytext());}

"CURSIVE"     {return symbol(sym.TIPOGRAFIA, yytext());}


/*---GROSOR DE LINEA---*/
"LINE"      {return symbol(sym.GROSOR_LINEA, yytext());}

"DOTTED"    {return symbol(sym.GROSOR_LINEA, yytext());}

"DOUBLE"    {return symbol(sym.GROSOR_LINEA, yytext());}

/*------FIN DE LAS EXPRESIONES CON UN CONTEXTO ESPECIAL PARA GENERACION DEL CODIGO COMPILADO-------*/


/*Created by Pablo*/

/*=========APARTADO EN EL QUE SE REGISTRAN LAS PALABRAS RESERVADAS (PREGUNTAS O CONFIGURACIONES DEL FORMULARIO)==========*/

"SECTION"               {return symbol(sym.SECTION, yytext());}

"TABLE"                 {return symbol(sym.TABLE, yytext());}

"TEXT"                  {return symbol(sym.TEXT, yytext());}


/*--*--Seccion de perguntas--*--*/
"OPEN_QUESTION"         {return symbol(sym.OPEN_QUESTION, yytext());}

"DROP_QUESTION"         {return symbol(sym.DROP_QUESTION, yytext());}

"SELECT_QUESTION"       {return symbol(sym.SELECT_QUESTION, yytext());}

"MULTIPLE_QUESTION"     {return symbol(sym.MULTIPLE_QUESTION, yytext());}


/*------*---Apartado de contenido especial que tienen las preguntas----*--------*/
"correct"               {return symbol(sym.CORRECT, yytext());}

"options"               {return symbol(sym.OPTIONS, yytext());}


/*=========APARTADO EN EL QUE SE REGISTRAN LAS PALABRAS RESERVADAS (PREGUNTAS O CONFIGURACIONES DEL FORMULARIO)==========*/


/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

{Decimal}  {return symbol(sym.DECIMAL, Double.parseDouble(yytext()));}

{Numero} {return symbol(sym.ENTERO, Integer.parseInt(yytext()));}

{Id} { return symbol(sym.ID, yytext()); }


/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

/*----RECONOCIMIENTO DE CADENAS DE TEXTO-----*/

\"          { yybegin(STRING);
              return symbol(sym.INICIO_CADENA, yytext()); }

}

/*----ESTADO DE CADENAS DE TEXTO INCLUIDO RECONOCIMIENTO DE EMOJIS-----*/
<STRING> {

    /*-----FIN DE LA CADENA DE TEXTO-----*/

    \" {
        yybegin(YYINITIAL);
        return symbol(sym.FIN_CADENA, yytext());
    }

    /*-----EMOJIS DINAMICOS-----*/
    ( "@[:" ")"+ "]" | "@[:smile:]" )       { return symbol(sym.EMOJI_SMILE, yytext()); }

    ( "@[:" "("+ "]" | "@[:sad:]" )         { return symbol(sym.EMOJI_SAD, yytext()); }

    ( "@[:" "|"+ "]" | "@[:serious:]" )     { return symbol(sym.EMOJI_SERIOUS, yytext()); }

    ( "@[" "<"+ "3"+ "]" | "@[:heart:]" )   { return symbol(sym.EMOJI_HEART, yytext()); }

    "@[:star:]"                             { return symbol(sym.EMOJI_STAR, yytext()); }

    ( "@[:star:" {Numero} ":]" | "@[:star-" {Numero} ":]" )     { return symbol(sym.EMOJI_MULTI_STAR, yytext()); }

    ( "@[:^^:]" | "@[:cat:]" )               { return symbol(sym.EMOJI_CAT, yytext()); }


    /*-----Texto normal (OBVIA @):-----*/

    [^\n\r\"\\@]+ { return symbol(sym.TEXTO_PLANO, yytext()); }

    /*-----RECONOCE COMO TEXTO NORMAL A @-----*/

    "@" { return symbol(sym.TEXTO_PLANO, "@"); }

    /*-----Escapes de cadenas siguen devolviendo texto plano------*/

    \\\" { return symbol(sym.TEXTO_PLANO, "\""); }
    \\n  { return symbol(sym.TEXTO_PLANO, "\n"); }
}

/*----ESTADO DE CADENAS DE TEXTO-----*/
/*Created by Pablo*/

[^]               {
                    reportError("Simbolo no existe en el lenguaje", yytext());
                    if(!modoParser){
                        return symbol(sym.ERROR, yytext());
                    }
                  }

<<EOF>>         {
                    return symbol(sym.EOF);
                } /*Created by Pablo*/


