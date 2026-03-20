package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

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

//Clase que representa a toda la jerarquia de layouts. Es decir a estos componentes que almacenan dentro de si mismos mas cosas
public abstract class CompiledContenedor extends CompiledForm {

    /*Constructor*/
    public CompiledContenedor(Number width, Number height, Number pointX, Number pointY, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY,estilos,fila,columna);
    }

    /*Metodo que permite que cada clase empaquete su propio estilo en los contenedores*/
    @Override
    protected void empaquetaEstilos() {

        if (this.estilos == null) {

            this.estilosProcesados = new EstilosProcesados(new int[]{0, 0, 0}, new int[]{255, 255, 255}, TipoLetra.MONO, null, null);
            return;
        }

        /*presets si en dado caso uno esta nulo*/
        int[] backgroudColor = new int[]{0, 0, 0};
        int[] textColor = new int[]{255, 255, 255};
        TipoLetra fontFamilly = TipoLetra.MONO;
        Number textSize = null;
        CompiledBorder border = null;

        for (CompiledEstilos estilo : this.estilos) {
            if (estilo instanceof CompiledBackground) {

                CompiledColor background = ((CompiledBackground) estilo).getBackgroundColor();
                if(background != null){
                    backgroudColor = background.evaluarColor();
                }

            } else if (estilo instanceof CompiledTextColor) {

                CompiledColor colorText = ((CompiledTextColor) estilo).getColorTexto();

                if(colorText != null){
                    textColor = colorText.evaluarColor();
                }

            } else if (estilo instanceof CompiledFontFamily) {

                fontFamilly = ((CompiledFontFamily) estilo).getFontFamily();

            } else if (estilo instanceof CompiledTextSize) {

                textSize = ((CompiledTextSize) estilo).getTextSize();
            }
            else if (estilo instanceof CompiledBorder) {
                border = ((CompiledBorder) estilo);
            }
        }

        this.estilosProcesados = new EstilosProcesados(backgroudColor, textColor, fontFamilly, textSize,border);
    }

}
