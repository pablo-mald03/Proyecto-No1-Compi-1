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
%ignorecase
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

//Metadatos

MetaDatos = "###" ( [^#] | "#" [^#] | "##" [^#] )* "###"

HexColor = "#"[0-9A-Fa-f]{6}

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

/*Ignorar metadatos*/

{MetaDatos}     {/*ignorar*/}



/*---APARTADO DE COLORES---*/

{HexColor} { return symbol(SymCompiled.COLOR_HEX, yytext()); }

/*---RECONOCIMIENTO DE COLORES---*/

"RED"       { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"BLUE"      { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"GREEN"     { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"PURPLE"    { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"SKY"       { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"YELLOW"    { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"BLACK"     { return symbol(SymCompiled.COLOR_PRESET, yytext()); }
"WHITE"     { return symbol(SymCompiled.COLOR_PRESET, yytext()); }

/*---FIN DEL APARTADO DE COLORES---*/


/*=====***--DEFINICION DE EXPRESIONES MATEMATICAS-***====*/

"-" {return symbol(SymCompiled.RESTA, yytext());}


/*=====***--DEFINICION DE EXPRESIONES MATEMATICAS-***====*/


/*=====***--DEFINICION DE CARACTERES ESPECIALES DE ETIQUETAS--***====*/

"="      { return symbol(SymCompiled.IGUAL); }

","      { return symbol(SymCompiled.COMA); }

"{"      { return symbol(SymCompiled.LLAVE_APERTURA); }

"}"      { return symbol(SymCompiled.LLAVE_CIERRE); }



/*=====***--DEFINICION DE CARACTERES ESPECIALES DE ETIQUETAS--***====*/



/*---APARTADO DE PALABRAS RESERVADAS---*/

"style"      { return symbol(SymCompiled.STYLE); }

"content"    { return symbol(SymCompiled.CONTENT); }

"color"      { return symbol(SymCompiled.COLOR_KEY); }

"background" { return symbol(SymCompiled.BACKGROUND); }

"font"       { return symbol(SymCompiled.FONT); }

"family"     { return symbol(SymCompiled.FAMILY); }

"size"       { return symbol(SymCompiled.SIZE); }

"border"     { return symbol(SymCompiled.BORDER); }

"section"    { return symbol(SymCompiled.SECTION); }

"table"      { return symbol(SymCompiled.TABLE); }

"text"       { return symbol(SymCompiled.TEXT); }

"line"       { return symbol(SymCompiled.LINE, yytext()); }

"element"    { return symbol(SymCompiled.ELEMENT); }

"open"       { return symbol(SymCompiled.OPEN); }

"drop"       { return symbol(SymCompiled.DROP); }

"select"     { return symbol(SymCompiled.SELECT); }

"multiple"   { return symbol(SymCompiled.MULTIPLE); }


/*=====***--FIN DEL APARTADO DE ETIQUETAS COMPUESTAS--***====*/



/*------*****--------APARTADO DE SIMBOLOS ESPECIALES--------*****------*/

"/>"     { return symbol(SymCompiled.CIERRE_ETIQUETA); }

"</"     { return symbol(SymCompiled.INICIO_ETIQUETA); }

"("       {return symbol(SymCompiled.PARENT_APERTURA);}

")"       {return symbol(SymCompiled.PARENT_CIERRE);}

"<"      { return symbol(SymCompiled.MENOR); }

">"      { return symbol(SymCompiled.MAYOR); }

"/"      { return symbol(SymCompiled.DIAGONAL); }


/*------*****--------FIN DEL APARTADO DE SIMBOLOS ESPECIALES--------*****------*/



/*=====***--DEFINICION DE TIPOS DE LETRA Y PRESETS--***====*/

/*---CONFIGURACIONES DE DOCK---*/
"VERTICAL"      {return symbol(SymCompiled.CONFIG_DOCK, yytext());}

"HORIZONTAL"    {return symbol(SymCompiled.CONFIG_DOCK, yytext());}

/*---TIPOGRAFIAS DE LETRAS---*/

"MONO"      {return symbol(SymCompiled.TIPOGRAFIA, yytext());}

"SANS_SERIF"      {return symbol(SymCompiled.TIPOGRAFIA, yytext());}

"CURSIVE"     {return symbol(SymCompiled.TIPOGRAFIA, yytext());}


/*---GROSOR DE LINEA---*/

"DOTTED"    {return symbol(SymCompiled.GROSOR_LINEA, yytext());}

"DOUBLE"    {return symbol(SymCompiled.GROSOR_LINEA, yytext());}



/*=====***--FIN DE LA DEFINICION DE TIPOS DE LETRA Y PRESETS--***====*/


/*==========ER CON CONTEXTO DE VALORES EN EL LENGUAJE============*/

{Decimal}  {return symbol(SymCompiled.DECIMAL, Double.parseDouble(yytext()));}

{Numero} {return symbol(SymCompiled.ENTERO, Integer.parseInt(yytext()));}

/*----RECONOCIMIENTO DE CADENAS DE TEXTO-----*/

\"          { yybegin(STRING);
              return symbol(SymCompiled.INICIO_CADENA, yytext()); }

}

/*----ESTADO DE CADENAS DE TEXTO INCLUIDO RECONOCIMIENTO DE EMOJIS-----*/
<STRING> {

    /*-----FIN DE LA CADENA DE TEXTO-----*/

    \" {
        yybegin(YYINITIAL);
        return symbol(SymCompiled.FIN_CADENA, yytext());
    }

    /*-----EMOJIS DINAMICOS-----*/
    ( "@[:" ")"+ "]" | "@[:smile:]" )       { return symbol(SymCompiled.EMOJI_SMILE, yytext()); }

    ( "@[:" "("+ "]" | "@[:sad:]" )         { return symbol(SymCompiled.EMOJI_SAD, yytext()); }

    ( "@[:" "|"+ "]" | "@[:serious:]" )     { return symbol(SymCompiled.EMOJI_SERIOUS, yytext()); }

    ( "@[" "<"+ "3"+ "]" | "@[:heart:]" )   { return symbol(SymCompiled.EMOJI_HEART, yytext()); }

    "@[:star:]"                             { return symbol(SymCompiled.EMOJI_STAR, yytext()); }

    ( "@[:star:" {Numero} ":]" | "@[:star-" {Numero} ":]" )     { return symbol(SymCompiled.EMOJI_MULTI_STAR, yytext()); }

    ( "@[:^^:]" | "@[:cat:]" )               { return symbol(SymCompiled.EMOJI_CAT, yytext()); }


    /*-----Texto normal (OBVIA @):-----*/

    [^\n\r\"\\@]+ { return symbol(SymCompiled.TEXTO_PLANO, yytext()); }

    /*-----RECONOCE COMO TEXTO NORMAL A @-----*/

    "@" { return symbol(SymCompiled.TEXTO_PLANO, "@"); }

    /*-----Escapes de cadenas siguen devolviendo texto plano------*/

    \\\" { return symbol(SymCompiled.TEXTO_PLANO, "\""); }
    \\n  { return symbol(SymCompiled.TEXTO_PLANO, "\n"); }
}


[^]               {
                    reportError("Simbolo no existe en el lenguaje", yytext());
                }

<<EOF>>         {
                    return symbol(SymCompiled.EOF);
                }
