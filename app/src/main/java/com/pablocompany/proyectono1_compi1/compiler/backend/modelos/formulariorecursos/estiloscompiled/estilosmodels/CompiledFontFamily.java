package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;

//Clase que permite representar a la familia de letra
public class CompiledFontFamily extends CompiledEstilos {

    //Atributo
    private TipoLetra fontFamily;

    //Constructor para inicializar los atributos de layouts
    public CompiledFontFamily( String fontFamilia) {
        this.setTipoLetra(fontFamilia);
    }

    /*Metodo utilizado para reconocer el tipo de letra*/
    private void setTipoLetra(String tipo) {
        try {
            this.fontFamily = TipoLetra.valueOf(tipo.toUpperCase());
        } catch (Exception e) {
            this.fontFamily = TipoLetra.MONO;
        }
    }

    /*Metodo getter para el tipo de letra*/
    public TipoLetra getFontFamily() {
        return fontFamily;
    }
}
