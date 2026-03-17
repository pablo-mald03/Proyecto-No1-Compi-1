package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que represena al tipo de letra recibido en los estilos
public class NodoTipoLetra extends Nodo {

    //Atributos
    private TipoLetra letraTipo;

    public NodoTipoLetra(String expresion, int linea, int columna) {
        super(linea, columna);

        try{
            this.letraTipo = TipoLetra.valueOf(expresion.toUpperCase());
        }catch (Exception e){
            this.letraTipo = TipoLetra.NOT_FOUND;
        }

    }

    //Metodo que permite validar semantica del tipo de letra (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.letraTipo == null || this.letraTipo == TipoLetra.NOT_FOUND) {
            listaErrores.add(new ErrorAnalisis("font family", "Semantico",
                    "El tipo de letra es invalida o no ha sido definida correctamente.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.VOID;
    }

    //Metodo que permite clonar
    public NodoTipoLetra clonar() {
        return new NodoTipoLetra(this.letraTipo.toString(), getLinea(), getColumna());
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion (PENDIENTE)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this.letraTipo;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "font family: " + this.letraTipo.toString();
    }
    /*--Metodo propio de la clase que permite contar los comodines que tienen en los estilos--*/
    public  int contarComodines(){
        return 0;
    }
}
