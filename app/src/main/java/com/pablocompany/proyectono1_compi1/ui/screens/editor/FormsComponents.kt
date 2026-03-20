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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion
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
    viewModel: FormViewModel
){
    when (component) {
        is CompiledSection -> RenderSection(component,viewModel)
        is CompiledTable -> RenderTable(component,viewModel)
        is CompiledText -> RenderText(component)
        is CompiledOpenQuest -> RenderOpenQuestion(component, viewModel)
        is CompiledSelectQuest -> RenderSelectQuestion(component, viewModel)
        is CompiledDropQuest -> RenderDropQuestion(component, viewModel)
        is CompiledMultipleQuest -> RenderMultipleQuestion(component, viewModel)
    }
}


/*Composable para poder insertar secciones*/
@Composable
fun RenderSection(
    section: CompiledSection,
    viewModel: FormViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .applyPosition(section)
    ) {

        val baseModifier = Modifier
            .applySize(section)
            .applyStyles(section.estilosProcesados)
            .defaultContentPadding()

        if (section.orientation == TipoOrientacion.VERTICAL) {

            Column(modifier = baseModifier) {
                section.elementos.forEach { child ->

                    val weightModifier =
                        if (child.height?.toFloat() == -1f) {
                            Modifier.weight(1f)
                        } else Modifier

                    Box(modifier = weightModifier) {
                        RenderComponent(child,viewModel)
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
                        RenderComponent(child,viewModel)
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
    viewModel: FormViewModel
) {

    Box(
        modifier = Modifier
            .applyPosition(table)
    ) {

        Column(
            modifier = Modifier
                .applySize(table)
                .applyStyles(table.estilosProcesados)
                .defaultContentPadding()
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
                                .defaultContentPadding()
                                .applySize(cell)
                                .applyStyles(cell.estilosProcesados)
                        ) {
                            RenderComponent(cell,viewModel)
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
            .applyStyles(text.estilosProcesados)
            .padding(horizontal = 4.dp),
        color = text.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
        fontSize = (text.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
    )
}
/*---*****--APARTADO DE PREGUNTAS----****---*/

/*FUNCION QUE REPRESENTA A LA PREGUNTA ABIERTA*/
@Composable
fun RenderOpenQuestion(
    question: CompiledOpenQuest,
    viewModel: FormViewModel
) {

    val id = "${question.fila}_${question.columna}"

    val text = viewModel.getAnswer(id) as? String ?: ""

    Column(
        modifier = Modifier
            .applySize(question)
            .applyStyles(question.estilosProcesados)
            .applyPosition(question)
            .cardLike()
    ) {

        Text(
            text = question.texto.toDisplayString(),
            color = question.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
            fontSize = (question.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = {
                viewModel.setAnswer(id, it)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


/*FUNCION QUE REPRESENTA A LA PREGUNTA SELECT*/

@Composable
fun RenderSelectQuestion(
    question: CompiledSelectQuest,
    viewModel: FormViewModel
) {

    val id = "${question.fila}_${question.columna}"

    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1

    Column(
        modifier = Modifier
            .applyPosition(question)
            .applySize(question)
            .applyStyles(question.estilosProcesados)
            .cardLike()
    ) {

        Text(
            text = question.texto.toDisplayString(),
            color = question.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
            fontSize = (question.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        question.opciones.forEachIndexed { index, opcion ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedIndex == index,
                    onClick = {
                        viewModel.setAnswer(id, index)
                    }
                )

                Text(
                    text = opcion.toDisplayString(),
                    color = Color.White
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
    viewModel: FormViewModel
) {

    val id = "${question.fila}_${question.columna}"

    val selectedIndex = viewModel.getAnswer(id) as? Int ?: -1
    val expanded = remember { mutableStateOf(false) }

    val opciones = question.opciones.map { it.toDisplayString() }

    val selectedText =
        if (selectedIndex in opciones.indices) opciones[selectedIndex]
        else ""

    Column(
        modifier = Modifier
            .applyPosition(question)
            .applySize(question)
            .applyStyles(question.estilosProcesados)
            .cardLike()
    ) {

        Text(
            text = question.texto.toDisplayString(),
            color = question.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
            fontSize = (question.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {

            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Elige") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                },
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
                        text = { Text(opcion) },
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
    viewModel: FormViewModel
) {

    val id = "${question.fila}_${question.columna}"

    val selected = (viewModel.getAnswer(id) as? List<Int>)?.toMutableList()
        ?: mutableListOf()

    Column(
        modifier = Modifier
            .applyPosition(question)
            .applySize(question)
            .applyStyles(question.estilosProcesados)
            .cardLike()
    ) {
        Text(
            text = question.texto.toDisplayString(),
            color = question.estilosProcesados?.textColor?.toComposeColor() ?: Color.White,
            fontSize = (question.estilosProcesados?.textSize?.toFloat() ?: 14f).sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        question.opciones.forEachIndexed { index, opcion ->

            val isChecked = selected.contains(index)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->

                        val newList = selected.toMutableList()

                        if (checked) {
                            newList.add(index)
                        } else {
                            newList.remove(index)
                        }

                        viewModel.setAnswer(id, newList)
                    }
                )

                Text(
                    text = opcion.toDisplayString(),
                    color = Color.White
                )
            }
        }
    }
}


/*Funcion que permita poner un padding por defecto*/

fun Modifier.defaultContentPadding(): Modifier {
    return this.padding(8.dp)
}

/*Funcion que permite poner un padding por defecto y un border*/
fun Modifier.cardLike(): Modifier {
    return this
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .padding(8.dp)
}



