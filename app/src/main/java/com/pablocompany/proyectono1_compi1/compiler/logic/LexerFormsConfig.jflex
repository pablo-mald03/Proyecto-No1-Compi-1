/********************** paquetes y otros ***********************/
package com.pablocompany.proyectono1_compi1.compiler.logic;

/*P*/

import java_cup.runtime.*;
import java.util.*;

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
    string = new StringBuilder();

%init}

/********************** Macros ***********************************/


%{
    /****************** Codigo de usuario (codigo java) ***********************/

    //StringBuilder para cadenas de texto
    private StringBuilder string;

 /*-----------------------------------------------
                   Codigo del lexer
             -------------------------------------------------*/

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

