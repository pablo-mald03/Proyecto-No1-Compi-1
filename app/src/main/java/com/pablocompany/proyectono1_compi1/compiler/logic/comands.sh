#!/bin/bash

JFLEX_JAR="/home/pablo03/compilator_java_resources/jflex-full-1.9.1.jar"
CUP_JAR="/home/pablo03/compilator_java_resources/java-cup-11b.jar"

# --- BLOQUE 1: GENERADOR DE FORMULARIOS ---
echo "Generando Lexer y Parser para FORMULARIOS (Lenguaje)..."

# Entramos a la carpeta para que JFlex y CUP vean los archivos locales
cd formulario/

java -jar "$JFLEX_JAR" LexerFormsConfig.jflex
java -jar "$CUP_JAR" -package com.pablocompany.proyectono1_compi1.compiler.logic.formulario -parser ParserForms -symbols sym ParserFormsConfig.cup

cd ..

# --- BLOQUE 2: CÓDIGO COMPILADO ---
echo "Generando Lexer y Parser para COMPILADO (Compilado)..."

cd fuente/

java -jar "$JFLEX_JAR" LexerCompiledConfig.jflex
java -jar "$CUP_JAR" -package com.pablocompany.proyectono1_compi1.compiler.logic.fuente -parser ParserCompiled -symbols SymCompiled ParserCompiledConfig.cup

cd ..

echo "¡Listo! Todo organizado en sus carpetas."