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

[\p{C}&&[^\n\r\t]]  { /* ignorar */ }

/* Espacios y tabs */

{WhiteSpace} { return symbol(sym.WHITESPACE, yytext()); }

/* Saltos de línea */

{LineTerminator} { return symbol(sym.NEWLINE, yytext()); }


/*--Comentarios--*/
"$".*      {/*Ignorado*/}

{ComentarioBloque}      {/*Ignorado*/}


/*=========APARTADO DE ER QUE IGNORAN O REPRESENTAN ESPACIOS EL CODIGO==========*/

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

/*---RECONOCIMIENTO DE COLORES---*/

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES ARITMETICOS EN EL LENGUAJE==========*/

"+" {return symbol(sym.SUMA);}

"-" {return symbol(sym.RESTA);}

"*" {return symbol(sym.MULTIPLICACION);}

"/" {return symbol(sym.DIVISION);}

"^" {return symbol(sym.POTENCIA);}

"%" {return symbol(sym.MODULO);}

"("       {return symbol(sym.PARENT_APERTURA);}

")"       {return symbol(sym.PARENT_CIERRE);}

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES ARITMETICOS EN EL LENGUAJE==========*/


/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE COMPARACION EN EL LENGUAJE==========*/

"=="    {return symbol(sym.IGUALDAD);}

"!!" {return symbol(sym.DIFERENTE);}

">"    {return symbol(sym.MAYOR);}

"<"    {return symbol(sym.MENOR);}

">="    {return symbol(sym.MAYOR_IGUAL);}

"<="    {return symbol(sym.MENOR_IGUAL);}

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE COMPARACION EN EL LENGUAJE==========*/

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE LOGICOS EN EL LENGUAJE==========*/

"&&"    {return symbol(sym.AND);}

"||"    {return symbol(sym.OR);}

"~"    {return symbol(sym.NOT);}

/*=========APARTADO DE ER QUE REPRESENTAN OPERADORES DE LOGICOS EN EL LENGUAJE==========*/


/*=========APARTADO DE ER QUE REPRESENTAN LOS TIPOS DE VARIABLES EN EL LENGUAJE==========*/

"number"        {return symbol(sym.VAR_NUMERO);}

"string"        {return symbol(sym.VAR_STRING);}

"special"       {return symbol(sym.VAR_ESPECIAL);}


/*=========APARTADO DE ER QUE REPRESENTAN LOS TIPOS DE VARIABLES EN EL LENGUAJE==========*/

/*=========APARTADO DE ER QUE REPRESENTAN LOS LAS DECLARACIONES DE VARIABLES O TIENEN ALGUN CONTEXTO DE ASIGNACION EN EL LENGUAJE==========*/

"="    {return symbol(sym.IGUALACION);}

"?"    {return symbol(sym.COMODIN);}

","    {return symbol(sym.COMA);}


/*=========APARTADO DE ER QUE REPRESENTAN LOS LAS DECLARACIONES DE VARIABLES O TIENEN ALGUN CONTEXTO DE ASIGNACION EN EL LENGUAJE==========*/



/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

{Decimal}  {return symbol(sym.DECIMAL, Double.parseDouble(yytext()));}

{Numero} {return symbol(sym.ENTERO, Integer.parseInt(yytext()));}

{Id} { return symbol(sym.ID, yytext()); }


/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

/*----RECONOCIMIENTO DE CADENAS DE TEXTO-----*/

\"          { yybegin(STRING);
              return symbol(sym.INICIO_CADENA); }

}

/*----ESTADO DE CADENAS DE TEXTO INCLUIDO RECONOCIMIENTO DE EMOJIS-----*/
<STRING> {

    /*-----FIN DE LA CADENA DE TEXTO-----*/

    \" {
        yybegin(YYINITIAL);
        return symbol(sym.FIN_CADENA);
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


.               {
                    reportError("Simbolo no existe en el lenguaje", yytext());
                    return symbol(sym.ERROR, yytext());
                }

<<EOF>>         {
                    return symbol(sym.EOF);
                }


