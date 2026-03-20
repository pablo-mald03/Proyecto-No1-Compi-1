package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoBorde
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.TipoEmoji
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadena
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledEmoji
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledDropQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledMultipleQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledOpenQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledSelectQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledText
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledSection
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledTable
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.EstilosProcesados
import com.pablocompany.proyectono1_compi1.data.repository.FormViewModel

/*Clase composable utilizada para poder recrear los elementos en la UI*/

@Composable
fun RenderComponent(
    component: CompiledForm,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    when (component) {
        is CompiledSection -> RenderSection(component, viewModel, scale, usePosition)
        is CompiledTable -> RenderTable(component, viewModel, scale, usePosition)
        is CompiledText -> RenderText(component, scale, usePosition)
        is CompiledOpenQuest -> RenderOpenQuestion(component, viewModel, scale, usePosition)
        is CompiledSelectQuest -> RenderSelectQuestion(component, viewModel, scale, usePosition)
        is CompiledDropQuest -> RenderDropQuestion(component, viewModel, scale, usePosition)
        is CompiledMultipleQuest -> RenderMultipleQuestion(component, viewModel, scale, usePosition)
    }
}


/*Composable para poder insertar secciones*/
@Composable
fun RenderSection(
    section: CompiledSection,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    Box(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(section, scale) else Modifier)
            .applySize(section, scale)
            .applyStyles(section.estilosProcesados, scale)
    ) {
        val baseModifier = Modifier
            .fillMaxSize()
            .defaultContentPadding(scale)

        if (section.orientation == TipoOrientacion.VERTICAL) {
            Column(modifier = baseModifier) {
                section.elementos.forEach { child ->
                    val weightModifier =
                        if (child.height?.toInt() == -1) Modifier.weight(1f) else Modifier

                    Box(modifier = weightModifier.fillMaxWidth()) {
                        RenderComponent(child, viewModel, scale, usePosition = false)
                    }
                }
            }
        } else {
            Row(modifier = baseModifier) {
                section.elementos.forEach { child ->
                    val weightModifier =
                        if (child.width?.toInt() == -1) Modifier.weight(1f) else Modifier

                    Box(modifier = weightModifier.fillMaxHeight()) {
                        RenderComponent(child, viewModel, scale, usePosition = false)
                    }
                }
            }
        }
    }
}

/*Composable que representa a una tabla o grid */
@Composable
fun RenderTable(
    table: CompiledTable,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    Box(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(table, scale) else Modifier)
    ) {
        Column(
            modifier = Modifier
                .applySize(table, scale)
                .applyStyles(table.estilosProcesados, scale)
                .defaultContentPadding(scale)
        ) {
            table.getElementos().forEach { fila ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    fila.forEach { cell ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding((4 * scale).dp)
                                .applySize(cell, scale)
                                .applyStyles(cell.estilosProcesados, scale)
                        ) {
                            RenderComponent(cell, viewModel, scale, usePosition = false)
                        }
                    }

                    // Relleno para filas incompletas
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
fun Modifier.applyStyles(estilos: EstilosProcesados?,scale: Float): Modifier {
    if (estilos == null) return this
    var modifier = this

    estilos.backgroudColor?.let {
        modifier = modifier.background(it.toComposeColor())
    }

    estilos.border?.let { border ->
        val colorBorde = border.color.evaluarColor().toComposeColor()
        val widthBorde = border.width.toFloat().dp

        estilos.border?.let { border ->
            val colorBorde = border.color.evaluarColor().toComposeColor()
            val widthPx = border.width.toFloat() * scale
            val roundedRadius = 8f * scale

            modifier = modifier.drawBehind {
                when (border.tipo) {
                    TipoBorde.DOTTED -> {
                        drawRoundRect(
                            color = colorBorde,
                            style = Stroke(
                                width = widthPx,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(10f * scale, 10f * scale),
                                    phase = 0f
                                )
                            ),
                            cornerRadius = CornerRadius(roundedRadius, roundedRadius)
                        )
                    }
                    TipoBorde.DOUBLE -> {
                        drawRoundRect(
                            color = colorBorde,
                            style = Stroke(width = widthPx / 3f),
                            cornerRadius = CornerRadius(roundedRadius, roundedRadius)
                        )

                        val gap = widthPx * 0.8f

                        val innerSize = Size(
                            width = size.width - (gap * 2),
                            height = size.height - (gap * 2)
                        )

                        drawRoundRect(
                            color = colorBorde,
                            topLeft = Offset(x = gap, y = gap),
                            size = innerSize,
                            style = Stroke(width = widthPx / 3f),
                            cornerRadius = CornerRadius(
                                maxOf(0f, roundedRadius - gap),
                                maxOf(0f, roundedRadius - gap)
                            )
                        )
                    }
                    else -> {
                        drawRoundRect(
                            color = colorBorde,
                            style = Stroke(width = widthPx),
                            cornerRadius = CornerRadius(roundedRadius, roundedRadius)
                        )
                    }
                }
            }
        }
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
fun Modifier.applySize(component: CompiledForm, scale: Float): Modifier {
    val w = component.width?.toFloat() ?: -1f
    val h = component.height?.toFloat() ?: -1f

    return this.then(
        when {
            w > 0 && h > 0 -> Modifier.size((w * scale).dp, (h * scale).dp)
            w > 0 -> Modifier.width((w * scale).dp)
            h > 0 -> Modifier.height((h * scale).dp)
            w == -1f -> Modifier.fillMaxWidth()
            else -> Modifier
        }
    )
}

/*Funcion que permite aplicar la posicion en la que se va a escalar un componente*/
fun Modifier.applyPosition(component: CompiledForm, scale: Float): Modifier {
    val x = (component.pointX ?: 0).toFloat() * scale
    val y = (component.pointY ?: 0).toFloat() * scale

    return this.offset(x.dp, y.dp)
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
fun RenderText(text: CompiledText, scale: Float, usePosition: Boolean = true) {
    val contenido = text.texto.toDisplayString()
    val estilos = text.estilosProcesados

    Text(
        text = contenido,
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(text, scale) else Modifier)
            .applySize(text, scale)
            .applyStyles(estilos, scale)
            .padding(horizontal = (4 * scale).dp),
        color = estilos?.textColor?.toComposeColor() ?: Color.Black,
        fontSize = calculateFontSize(estilos?.textSize, scale),
        fontFamily = estilos?.fontFamilly.toComposeFont()
    )
}

/*---*****--APARTADO DE PREGUNTAS----****---*/

/*FUNCION QUE REPRESENTA A LA PREGUNTA ABIERTA*/
@Composable
fun RenderOpenQuestion(
    question: CompiledOpenQuest,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    val id = "${question.fila}_${question.columna}"
    val text = viewModel.getAnswer(id) as? String ?: ""
    val estilos = question.estilosProcesados

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .cardLike(scale,estilos)
    ) {
        Text(
            text = question.texto.toDisplayString(),
            color = estilos?.textColor?.toComposeColor() ?: Color.Black,
            fontSize = calculateFontSize(estilos?.textSize, scale),
            fontWeight = FontWeight.Medium,
            fontFamily = estilos?.fontFamilly.toComposeFont()
        )

        Spacer(modifier = Modifier.height((8 * scale).dp))

        OutlinedTextField(
            value = text,
            onValueChange = { viewModel.setAnswer(id, it) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = estilos?.textColor?.toComposeColor() ?: Color.Black,
                fontSize = calculateFontSize(14, scale),
                fontFamily = estilos?.fontFamilly.toComposeFont()
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = estilos?.textColor?.toComposeColor() ?: Color.Black,
                unfocusedTextColor = estilos?.textColor?.toComposeColor() ?: Color.Black,
                focusedBorderColor = (estilos?.textColor?.toComposeColor() ?: Color.Black).copy(
                    alpha = 0.7f
                ),
                unfocusedBorderColor = (estilos?.textColor?.toComposeColor() ?: Color.Black).copy(
                    alpha = 0.4f
                )
            )
        )
    }
}


/*FUNCION QUE REPRESENTA A LA PREGUNTA SELECT*/

@Composable
fun RenderSelectQuestion(
    question: CompiledSelectQuest,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    val id = "${question.fila}_${question.columna}"
    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1
    val estilos = question.estilosProcesados

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .cardLike(scale,estilos)
    ) {
        Text(
            text = question.texto.toDisplayString(),
            color = estilos?.textColor?.toComposeColor() ?: Color.Black,
            fontSize = calculateFontSize(estilos?.textSize, scale),
            fontWeight = FontWeight.Bold,
            fontFamily = estilos?.fontFamilly.toComposeFont()
        )

        Spacer(modifier = Modifier.height((8 * scale).dp))


        question.opciones.forEachIndexed { index, opcion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (4 * scale).dp)
                    .clickable { viewModel.setAnswer(id, index) }
            ) {
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = { viewModel.setAnswer(id, index) },
                    modifier = Modifier.scale(scale),

                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF6200EE),
                        unselectedColor = Color.Gray
                    )
                )

                Text(
                    text = opcion.toDisplayString(),
                    fontSize = calculateFontSize(estilos?.textSize ?: 12, scale),
                    color = estilos?.textColor?.toComposeColor() ?: Color.Black,
                    modifier = Modifier.padding(start = (8 * scale).dp),
                    fontFamily = estilos?.fontFamilly.toComposeFont()
                )
            }
        }
    }
}


/*Funcion que representa a la pregunta drop*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderDropQuestion(
    question: CompiledDropQuest,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    val id = "${question.fila}_${question.columna}"
    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1
    val expanded = remember { mutableStateOf(false) }
    val opciones = question.opciones.map { it.toDisplayString() }
    val selectedText = if (selectedIndex in opciones.indices) opciones[selectedIndex] else ""
    val estilos = question.estilosProcesados

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .cardLike(scale,estilos)
    ) {
        Text(
            text = question.texto.toDisplayString(),
            color = estilos?.textColor?.toComposeColor() ?: Color.White,
            fontSize = calculateFontSize(estilos?.textSize, scale),
            fontFamily = estilos?.fontFamilly.toComposeFont()
        )

        Spacer(modifier = Modifier.height((8 * scale).dp))

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(
                    fontSize = calculateFontSize(14, scale),
                    fontFamily = estilos?.fontFamilly.toComposeFont()
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                opciones.forEachIndexed { index, opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                opcion,
                                fontSize = calculateFontSize(14, scale),
                                fontFamily = estilos?.fontFamilly.toComposeFont(),
                            )
                        },
                        onClick = {
                            viewModel.setAnswer(id, index)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

/*FUNCION QUE REPRESENTA A LA MULTIPLEQUESTION*/
@Composable
fun RenderMultipleQuestion(
    question: CompiledMultipleQuest,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true
) {
    val id = "${question.fila}_${question.columna}"
    val selected = (viewModel.getAnswer(id) as? List<Int>)?.toMutableList() ?: mutableListOf()
    val estilos = question.estilosProcesados

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .cardLike(scale,estilos)
    ) {
        Text(
            text = question.texto.toDisplayString(),
            color = estilos?.textColor?.toComposeColor() ?: Color.White,
            fontSize = calculateFontSize(estilos?.textSize, scale),
            fontFamily = estilos?.fontFamilly.toComposeFont()
        )

        Spacer(modifier = Modifier.height((7 * scale).dp))

        question.opciones.forEachIndexed { index, opcion ->
            val isChecked = selected.contains(index)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = (2 * scale).dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        val newList = selected.toMutableList()

                        val indiceRealParaBackend = index

                        if (checked) {
                            if (!newList.contains(indiceRealParaBackend)) newList.add(indiceRealParaBackend)
                        } else {
                            newList.remove(indiceRealParaBackend)
                        }

                        viewModel.setAnswer(id, newList)
                    },
                    modifier = Modifier.scale(scale)
                )
                Text(
                    text = opcion.toDisplayString(),
                    color = estilos?.textColor?.toComposeColor() ?: Color.White,
                    fontSize = calculateFontSize(estilos?.textSize ?: 12, scale),
                    fontFamily = estilos?.fontFamilly.toComposeFont()
                )
            }
        }
    }
}


/*Funcion que permita poner un padding por defecto*/

fun Modifier.defaultContentPadding(scale: Float): Modifier {
    return this.padding((8 * scale).dp)
}

/*Funcion que permite poner un padding por defecto y un border*/
fun Modifier.cardLike(scale: Float, estilos: EstilosProcesados? = null): Modifier {
    val dynamicPadding = maxOf(4f, 8 * scale).dp
    var mod = this
        .padding(dynamicPadding)
        .clip(RoundedCornerShape(maxOf(4f, 10 * scale).dp))

    if (estilos?.backgroudColor == null) {
        mod = mod.background(Color.White.copy(alpha = 0.1f))
    }

    return mod.padding(dynamicPadding)
}

/*Metodo que calcula el size de la fuente en base a la escala*/
@Composable
fun calculateFontSize(textSize: Number?, scale: Float): TextUnit {
    val rawSize = textSize?.toFloat() ?: -1f

    val baseSize = if (rawSize <= 0f) 16f else rawSize

    return (baseSize * scale).sp
}


// Metodo que calcula el tipo de la letra a FontFamily

fun TipoLetra?.toComposeFont(): FontFamily {
    return when (this) {
        TipoLetra.MONO -> FontFamily.Monospace
        TipoLetra.SANS_SERIF -> FontFamily.SansSerif
        TipoLetra.CURSIVE -> FontFamily.Cursive
        else -> FontFamily.Default // Para NOT_FOUND y null
    }
}

// Mapeo de TipoBorde

fun TipoBorde?.toStrokeCap(): StrokeCap {
    return StrokeCap.Round
}
