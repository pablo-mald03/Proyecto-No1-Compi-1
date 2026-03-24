package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.pablocompany.proyectono1_compi1.R
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions
import com.pablocompany.proyectono1_compi1.data.clases.EvaluacionQuestion
import com.pablocompany.proyectono1_compi1.data.clases.ReporteFormulario

/*Clase composable utilizada para poder recrear los elementos en la UI*/

@Composable
fun RenderComponent(
    component: CompiledForm,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true,
    evaluationMap: Map<String, EvaluacionQuestion>? = null
) {

    val id = if (component is CompiledQuestions) "${component.fila}_${component.columna}" else ""
    val miEvaluacion = evaluationMap?.get(id)


    when (component) {
        is CompiledSection -> RenderSection(component, viewModel, scale, usePosition,evaluationMap)
        is CompiledTable -> RenderTable(component, viewModel, scale, usePosition,evaluationMap)

        is CompiledText -> RenderText(component, scale, usePosition)
        is CompiledOpenQuest -> RenderOpenQuestion(component, viewModel, scale, usePosition,miEvaluacion)
        is CompiledSelectQuest -> RenderSelectQuestion(component, viewModel, scale, usePosition,miEvaluacion)
        is CompiledDropQuest -> RenderDropQuestion(component, viewModel, scale, usePosition,miEvaluacion)
        is CompiledMultipleQuest -> RenderMultipleQuestion(component, viewModel, scale, usePosition,miEvaluacion)
    }
}


/*Composable para poder insertar secciones*/
@Composable
fun RenderSection(
    section: CompiledSection,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true,
    evaluationMap: Map<String, EvaluacionQuestion>? = null
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
                        RenderComponent(child, viewModel, scale, usePosition = false, evaluationMap = evaluationMap)
                    }
                }
            }
        } else {
            Row(modifier = baseModifier) {
                section.elementos.forEach { child ->
                    val weightModifier =
                        if (child.width?.toInt() == -1) Modifier.weight(1f) else Modifier

                    Box(modifier = weightModifier.fillMaxHeight()) {
                        RenderComponent(child, viewModel, scale, usePosition = false, evaluationMap = evaluationMap)
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
    usePosition: Boolean = true,
    evaluationMap: Map<String, EvaluacionQuestion>? = null
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
                            RenderComponent(cell, viewModel, scale, usePosition = false, evaluationMap = evaluationMap)
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
fun Modifier.applyStyles(estilos: EstilosProcesados?, scale: Float): Modifier {
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

    val r = (this.getOrNull(0) ?: 0).coerceIn(0, 255) / 255f
    val g = (this.getOrNull(1) ?: 0).coerceIn(0, 255) / 255f
    val b = (this.getOrNull(2) ?: 0).coerceIn(0, 255) / 255f
    val a = (if (this.size > 3) this[3] else 255) / 255f

    return Color(red = r, green = g, blue = b, alpha = a)
}

/*Funcion que permite aplicar un tamanio a un componente*/
fun Modifier.applySize(component: CompiledForm, scale: Float): Modifier {
    val w = component.width?.toFloat() ?: -1f
    val h = component.height?.toFloat() ?: -1f

    return this.then(
        when {
            w == -1f && h == -1f -> Modifier.fillMaxSize()

            w > 0 && h > 0 -> Modifier.size((w * scale).dp, (h * scale).dp)

            w == -1f && h > 0 -> Modifier
                .fillMaxWidth()
                .height((h * scale).dp)

            w > 0 && h == -1f -> Modifier
                .width((w * scale).dp)
                .fillMaxHeight()

            w == -1f -> Modifier.fillMaxWidth()
            h == -1f -> Modifier.fillMaxHeight()
            w > 0 -> Modifier.width((w * scale).dp)
            h > 0 -> Modifier.height((h * scale).dp)

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
            .padding(horizontal = (3 * scale).dp),
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
    usePosition: Boolean = true,
    evaluation: EvaluacionQuestion? = null
) {
    val id = "${question.fila}_${question.columna}"
    val text = viewModel.getAnswer(id) as? String ?: ""
    val estilos = question.estilosProcesados

    val fontSize = calculateFontSize(estilos?.textSize ?: 14, scale)

    val statusColor = when {
        evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.Black
        evaluation.esCorrecta -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .then(
                if (evaluation != null) Modifier.border(
                    1.dp,
                    statusColor.copy(alpha = 0.3f),
                    RoundedCornerShape(8.dp)
                )
                else Modifier
            )
            .cardLike(scale, estilos)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = question.texto.toDisplayString(),
                color = estilos?.textColor?.toComposeColor() ?: Color.Black,
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                fontFamily = estilos?.fontFamilly.toComposeFont(),
                modifier = Modifier.weight(1f)
            )

            if (evaluation != null) {
                Icon(
                    imageVector = if (evaluation.esCorrecta) Icons.Default.TaskAlt else Icons.Default.SmsFailed,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size((18 * scale).dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((8 * scale).dp))

        OutlinedTextField(
            value = text,
            onValueChange = { viewModel.setAnswer(id, it) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = evaluation != null,
            enabled = evaluation == null,
            textStyle = TextStyle(
                color = if (evaluation != null) statusColor else (estilos?.textColor?.toComposeColor()
                    ?: Color.Black),
                fontSize = fontSize,
                fontFamily = estilos?.fontFamilly.toComposeFont()
            ),
            placeholder = {
                if (evaluation == null) {
                    Text("Escribe tu respuesta aquí...", fontSize = fontSize)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = statusColor,
                unfocusedBorderColor = statusColor.copy(alpha = 0.5f),
                disabledBorderColor = statusColor.copy(alpha = 0.6f),
                disabledTextColor = if (text.isEmpty()) Color.Gray else statusColor,
                disabledContainerColor = if (evaluation != null) statusColor.copy(alpha = 0.05f) else Color.Transparent
            )
        )

        if (evaluation != null) {
            Text(
                text = if (evaluation.esCorrecta) "Informacion guardada correctamente" else "Este campo es requerido",
                color = statusColor.copy(alpha = 0.8f),
                fontSize = fontSize,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


/*FUNCION QUE REPRESENTA A LA PREGUNTA SELECT*/

@Composable
fun RenderSelectQuestion(
    question: CompiledSelectQuest,
    viewModel: FormViewModel,
    scale: Float,
    usePosition: Boolean = true,
    evaluation: EvaluacionQuestion? = null
) {
    val id = "${question.fila}_${question.columna}"
    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1
    val estilos = question.estilosProcesados

    val statusColor = when {
        evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.Black
        evaluation.esCorrecta -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    val correctIndex = evaluation?.respuestaCorrecta?.toString()?.toDoubleOrNull()?.toInt() ?: -1

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .then(
                if (evaluation != null) Modifier.border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                else Modifier
            )
            .cardLike(scale, estilos)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = question.texto.toDisplayString(),
                color = estilos?.textColor?.toComposeColor() ?: Color.Black,
                fontSize = calculateFontSize(estilos?.textSize, scale),
                fontWeight = FontWeight.Bold,
                fontFamily = estilos?.fontFamilly.toComposeFont(),
                modifier = Modifier.weight(1f)
            )

            if (evaluation != null) {
                Icon(
                    imageVector = if (evaluation.esCorrecta) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size((18 * scale).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height((7 * scale).dp))

        question.opciones.forEachIndexed { index, opcion ->

            val isOptionSelected = selectedIndex == index
            val isOptionCorrect = correctIndex == index

            val optionColor = when {
                evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.Black
                isOptionCorrect -> Color(0xFF4CAF50)
                isOptionSelected && !isOptionCorrect -> Color(0xFFF44336)
                else -> (estilos?.textColor?.toComposeColor() ?: Color.Black).copy(alpha = 0.5f)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { viewModel.setAnswer(id, index) }
                    .then(
                        if (evaluation != null && isOptionCorrect)
                            Modifier.background(Color(0xFF4CAF50).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        else Modifier
                    )
            ) {
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = { if (evaluation == null) viewModel.setAnswer(id, index) },
                    modifier = Modifier.scale(scale),
                    enabled = evaluation == null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = if (evaluation != null) optionColor else Color(0xFF6200EE),
                        unselectedColor = if (evaluation != null && isOptionCorrect) Color(0xFF4CAF50) else Color.Gray,
                        disabledSelectedColor = optionColor,
                        disabledUnselectedColor = if (isOptionCorrect) Color(0xFF4CAF50).copy(alpha = 0.5f) else Color.LightGray
                    )
                )

                Text(
                    text = opcion.toDisplayString(),
                    fontSize = calculateFontSize(estilos?.textSize ?: 12, scale),
                    color = estilos?.textColor?.toComposeColor() ?: Color.Black,
                    modifier = Modifier.padding(start = (8 * scale).dp),
                    fontFamily = estilos?.fontFamilly.toComposeFont(),
                    fontWeight = if (isOptionCorrect || isOptionSelected) FontWeight.Medium else FontWeight.Normal
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
    usePosition: Boolean = true,
    evaluation: EvaluacionQuestion? = null
) {
    val id = "${question.fila}_${question.columna}"
    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1
    val expanded = remember { mutableStateOf(false) }
    val opciones = question.opciones.map { it.toDisplayString() }
    val selectedText = if (selectedIndex in opciones.indices) opciones[selectedIndex] else ""
    val estilos = question.estilosProcesados

    val statusColor = when {
        evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.White
        evaluation.esCorrecta -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .then(
                if (evaluation != null) Modifier.border(
                    1.dp,
                    statusColor.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                )
                else Modifier
            )
            .cardLike(scale, estilos)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question.texto.toDisplayString(),
                color = estilos?.textColor?.toComposeColor() ?: Color.White,
                fontSize = calculateFontSize(estilos?.textSize, scale),
                fontFamily = estilos?.fontFamilly.toComposeFont()
            )

            if (evaluation != null) {
                Icon(
                    imageVector = if (evaluation.esCorrecta) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier
                        .size((20 * scale).dp)
                        .padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((7 * scale).dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            ExposedDropdownMenuBox(
                expanded = if (evaluation == null) expanded.value else false,
                onExpandedChange = {
                    if (evaluation == null) expanded.value = !expanded.value
                }
            ) {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(
                        fontSize = calculateFontSize(estilos?.textSize ?: 12, scale),
                        fontFamily = estilos?.fontFamilly.toComposeFont(),
                        color = if (evaluation != null) statusColor else Color.Unspecified
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                if (evaluation == null) {
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        opciones.forEachIndexed { index, opcion ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        opcion,
                                        fontSize = calculateFontSize(
                                            estilos?.textSize ?: 12,
                                            scale
                                        ),
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

        if (evaluation != null && !evaluation.esCorrecta && !evaluation.esInformativa) {
            val correctIdx = evaluation.respuestaCorrecta as? Int ?: -1
            if (correctIdx in opciones.indices) {
                Text(
                    text = "Respuesta correcta: ${opciones[correctIdx]}",
                    color = Color(0xFFBBDEFB),
                    fontSize = calculateFontSize(estilos?.textSize ?: 12, scale),
                    modifier = Modifier.padding(top = 4.dp),
                    fontWeight = FontWeight.Bold
                )
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
    usePosition: Boolean = true,
    evaluation: EvaluacionQuestion? = null
) {
    val id = "${question.fila}_${question.columna}"
    val selected = (viewModel.getAnswer(id) as? List<*>)
        ?.mapNotNull { it.toString().toDoubleOrNull()?.toInt() } ?: emptyList()

    val estilos = question.estilosProcesados

    val statusColor = when {
        evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.White
        evaluation.esCorrecta -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    val respuestasCorrectas = (evaluation?.respuestaCorrecta as? List<*>)
        ?.mapNotNull { it.toString().toDoubleOrNull()?.toInt() } ?: emptyList()

    Column(
        modifier = Modifier
            .then(if (usePosition) Modifier.applyPosition(question, scale) else Modifier)
            .applySize(question, scale)
            .applyStyles(estilos, scale)
            .then(
                if (evaluation != null) Modifier.border(
                    1.dp,
                    statusColor.copy(alpha = 0.4f),
                    RoundedCornerShape(8.dp)
                )
                else Modifier
            )
            .cardLike(scale, estilos)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = question.texto.toDisplayString(),
                color = statusColor,
                fontSize = calculateFontSize(estilos?.textSize ?: 14, scale),
                fontFamily = estilos?.fontFamilly.toComposeFont(),
                modifier = Modifier.weight(1f)
            )
            if (evaluation != null) {
                Icon(
                    imageVector = if (evaluation.esCorrecta) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size((20 * scale).dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((7 * scale).dp))

        question.opciones.forEachIndexed { index, opcion ->
            val isChecked = selected.contains(index)
            val isOptionCorrect = respuestasCorrectas.contains(index)

            val optionContentColor = when {
                evaluation == null -> estilos?.textColor?.toComposeColor() ?: Color.White
                isOptionCorrect && isChecked -> Color(0xFF4CAF50)
                isOptionCorrect && !isChecked -> Color(0xFF2196F3)
                !isOptionCorrect && isChecked -> Color(0xFFF44336)
                else -> (estilos?.textColor?.toComposeColor() ?: Color.White).copy(alpha = 0.6f)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = (2 * scale).dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        if (evaluation == null) {
                            val newList = selected.toMutableList()
                            if (checked) {
                                if (!newList.contains(index)) newList.add(index)
                            } else {
                                newList.remove(index)
                            }
                            viewModel.setAnswer(id, newList)
                        }
                    },
                    modifier = Modifier.scale(scale),
                    enabled = evaluation == null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = if (evaluation != null) optionContentColor else MaterialTheme.colorScheme.primary,
                        uncheckedColor = if (evaluation != null && isOptionCorrect) Color(0xFF2196F3) else MaterialTheme.colorScheme.onSurface,
                        disabledCheckedColor = optionContentColor,
                        disabledUncheckedColor = if (isOptionCorrect) Color(0xFF2196F3).copy(alpha = 0.5f) else Color.Gray
                    )
                )
                Text(
                    text = opcion.toDisplayString(),
                    color = optionContentColor,
                    fontSize = calculateFontSize(estilos?.textSize ?: 14, scale),
                    fontFamily = estilos?.fontFamilly.toComposeFont(),
                    style = if (evaluation != null && isOptionCorrect && !isChecked)
                        TextStyle(textDecoration = TextDecoration.Underline)
                    else TextStyle.Default
                )
            }
        }

        if (evaluation != null && evaluation.esInformativa) {
            Text(
                text = "Recopilacion de información",
                color = Color.Gray,
                fontSize = calculateFontSize(estilos?.textSize ?: 14, scale),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp)
            )
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


/*Variables de tipos de letra propios de la app*/
val JetBrainsMono = FontFamily(Font(R.font.jetbrains_mono, FontWeight.Normal))
val RobotoSans = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal))
val MontserratDefault = FontFamily(Font(R.font.montserrat_regular, FontWeight.Normal))


// Metodo que calcula el tipo de la letra a FontFamily
fun TipoLetra?.toComposeFont(): FontFamily {
    return when (this) {
        TipoLetra.MONO -> JetBrainsMono
        TipoLetra.SANS_SERIF -> RobotoSans
        TipoLetra.CURSIVE -> FontFamily.Cursive
        else -> MontserratDefault
    }
}
