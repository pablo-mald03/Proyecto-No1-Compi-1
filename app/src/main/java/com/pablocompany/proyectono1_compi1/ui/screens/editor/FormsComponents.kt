package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.TipoEmoji
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadena
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledEmoji
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledText
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledSection
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledTable
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.EstilosProcesados

/*Clase composable utilizada para poder recrear los elementos en la UI*/

@Composable
fun RenderComponent(component: CompiledForm) {
    when (component) {
        is CompiledSection -> RenderSection(component)
        is CompiledTable -> RenderTable(component)
        is CompiledText -> RenderText(component)
    }
}

/*Composable para poder insertar secciones*/
@Composable
fun RenderSection(section: CompiledSection) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .applyPosition(section)
    ) {

        val baseModifier = Modifier
            .applySize(section)
            .applyStyles(section.estilosProcesados)

        if (section.orientation == TipoOrientacion.VERTICAL) {

            Column(modifier = baseModifier) {
                section.elementos.forEach { child ->

                    val weightModifier =
                        if (child.height?.toFloat() == -1f) {
                            Modifier.weight(1f)
                        } else Modifier

                    Box(modifier = weightModifier) {
                        RenderComponent(child)
                    }
                }
            }

        } else {

            Row(modifier = baseModifier) {
                section.elementos.forEach { child ->

                    val weightModifier =
                        if (child.width?.toFloat() == -1f) {
                            Modifier.weight(1f)
                        } else Modifier

                    Box(modifier = weightModifier) {
                        RenderComponent(child)
                    }
                }
            }
        }
    }
}

/*Composable que representa a una tabla o grid */
@Composable
fun RenderTable(table: CompiledTable) {

    Box(
        modifier = Modifier
            .applyPosition(table)
    ) {

        Column(
            modifier = Modifier
                .applySize(table)
                .applyStyles(table.estilosProcesados)
        ) {
            table.getElementos().forEach { fila ->

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    fila.forEach { cell ->

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .applySize(cell)
                                .applyStyles(cell.estilosProcesados)
                        ) {
                            RenderComponent(cell)
                        }
                    }

                    val faltantes = table.getTotalColumnas() - fila.size
                    repeat(faltantes) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/*Funcion auxiliar para poder aplicar estilos a los componentes*/
fun Modifier.applyStyles(estilos: EstilosProcesados?): Modifier {
    if (estilos == null) return this

    var modifier = this

    estilos.backgroudColor?.let {
        modifier = modifier.background(it.toComposeColor())
    }

    estilos.textSize?.let {
        // esto luego lo aplicamos en Text, no aquí
    }


    estilos.border?.let { border ->
        modifier = modifier.border(
            width = border.width.toFloat().dp,
            color = border.color.evaluarColor().toComposeColor()
        )
    }

    return modifier
}

/*Funcion que permite transformar un color array en un Color de Compose*/
fun IntArray.toComposeColor(): Color {
    return Color(
        red = this[0] / 255f,
        green = this[1] / 255f,
        blue = this[2] / 255f,
        alpha = (if (this.size > 3) this[3] else 255) / 255f
    )
}

/*Funcion que permite aplicar un tamanio a un componente*/
fun Modifier.applySize(component: CompiledForm): Modifier {
    var modifier = this

    val width = component.width?.toFloat()
    val height = component.height?.toFloat()

    if (width != null) {
        modifier = when (width) {
            -1f -> modifier.fillMaxWidth()
            else -> modifier.width(width.dp)
        }
    }

    if (height != null) {
        modifier = when (height) {
            -1f -> modifier.fillMaxHeight()
            else -> modifier.height(height.dp)
        }
    }

    return modifier
}

fun Modifier.applyPosition(component: CompiledForm): Modifier {
    var modifier = this

    val x = component.pointX?.toFloat() ?: 0f
    val y = component.pointY?.toFloat() ?: 0f

    if (x != 0f || y != 0f) {
        modifier = modifier.offset(x.dp, y.dp)
    }

    return modifier
}

/*Funcion que permite convertir a string los emojis y cadenas de texto*/
fun CompiledCadenaTexto.toDisplayString(): String {
    val builder = StringBuilder()

    this.getTexto().forEach { fragmento ->

        when (fragmento) {

            is CompiledCadena -> {
                builder.append(fragmento.getStringCompiled())
            }

            is CompiledEmoji -> {
                val emoji = fragmento.tipoEmoji.toUnicode()
                repeat(fragmento.getVeces()) {
                    builder.append(emoji)
                }
            }
        }
    }

    return builder.toString()
}

/*Implementacion de emojis*/
fun TipoEmoji.toUnicode(): String {
    return when (this) {
        TipoEmoji.SMILE -> "😊"
        TipoEmoji.SAD -> "😢"
        TipoEmoji.SERIOUS -> "😐"
        TipoEmoji.HEART -> "❤️"
        TipoEmoji.STAR -> "⭐"
        TipoEmoji.CAT -> "🐱"
        TipoEmoji.MULTI_STAR -> "⭐"
    }
}

/*Funcion que representa aun texto*/
@Composable
fun RenderText(text: CompiledText) {

    val contenido = text.texto.toDisplayString()

    Text(
        text = contenido,
        modifier = Modifier
            .applySize(text)
            .applyStyles(text.estilosProcesados),
        color = text.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
        fontSize = (text.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
    )
}

