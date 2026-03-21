package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.EstilosProcesados;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase de maxima jerarquia que representa a todas las preguntas que puede tener un formulario
public abstract class CompiledQuestions extends CompiledForm {

    public CompiledQuestions(Number width, Number height, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, 0, 0, estilos, fila, columna);
    }

    /*Metodo que permite que cada clase empaquete su propio estilo en quests y textos*/
    @Override
    protected void empaquetaEstilos() {

        if (this.estilos == null) {

            this.estilosProcesados = new EstilosProcesados(new int[]{255, 255, 255}, new int[]{0, 0, 0}, null, -1);
            return;
        }

        /*presets si en dado caso uno esta nulo*/
        int[] backgroudColor = new int[]{255, 255, 255};
        int[] textColor = new int[]{0, 0, 0};
        TipoLetra fontFamilly = null;
        Number textSize = -1;

        for (CompiledEstilos estilo : this.estilos) {
            if (estilo instanceof CompiledBackground) {

                CompiledColor background = ((CompiledBackground) estilo).getBackgroundColor();
                if (background != null) {
                    backgroudColor = background.evaluarColor();
                }

            } else if (estilo instanceof CompiledTextColor) {

                CompiledColor colorText = ((CompiledTextColor) estilo).getColorTexto();

                if (colorText != null) {
                    textColor = colorText.evaluarColor();
                }

            } else if (estilo instanceof CompiledFontFamily) {

                fontFamilly = ((CompiledFontFamily) estilo).getFontFamily();

            } else if (estilo instanceof CompiledTextSize) {

                textSize = ((CompiledTextSize) estilo).getTextSize();
            }
        }

        this.estilosProcesados = new EstilosProcesados(backgroudColor, textColor, fontFamilly, textSize);
    }

}/*Created by Pablo*/
