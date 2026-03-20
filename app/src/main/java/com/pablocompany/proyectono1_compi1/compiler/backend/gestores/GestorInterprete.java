package com.pablocompany.proyectono1_compi1.compiler.backend.gestores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CodigoInterpretado;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase delegada para ejecutar la respectiva validacion y organizacion del codigo que se va a retornar interpretado
public class GestorInterprete {

    //Atributo que permite definir el codigo interpretado
    private CodigoInterpretado codigo;

    private List<ErrorAnalisis> listaErrores;

    /*Constructor*/
    public GestorInterprete(CodigoInterpretado codigo, List<ErrorAnalisis> listaErrores) {
        this.codigo = codigo;
        this.listaErrores = listaErrores;
    }

    /*Metodo final que termina de setear los valores de los atributos*/
    public void ejecutarCodigoInterpretado() {

        if (this.codigo == null) {
            return;
        }

        if (this.codigo.getCodigo().isEmpty()) {
            return;
        }

        /*Termina de validar el codigo de estilos*/
        for (CompiledForm form : codigo.getCodigo()) {
            if (form != null) {
                form.validarEstilos(this.listaErrores);

            }
        }

        /*Permite que cada clase empaquete su propio estilo*/
        for (CompiledForm form : codigo.getCodigo()) {
            if (form != null) {
                form.delegarEstilos();
            }
        }

    }


    /*Metodos getteres para obtener los atributos*/

    /*Obtiene la lista del codigo interpretado*/
    public List<CompiledForm> getCodigoInterpretado() {
        return codigo.getCodigo();
    }

    /*Obtiene la lista de errores finales*/
    public List<ErrorAnalisis> getListadoErrores() {
        return listaErrores;
    }

}
