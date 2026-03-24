package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/*Metodo composable que permite manejar las plantillas de codigo de la app*/
@Composable
fun TemplatePickerDialog(
    onDismiss: () -> Unit,
    onTemplateSelected: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF020226),
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color.White)
            }
        },
        text = {

            Column {

                Text(
                    text = "Insertar plantilla",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // ===== FUNCIONES SPECIAL =====
                    TemplateDropdownButton(
                        title = "COMPONENTES",
                        options = listOf(

                            "TEXTO" to {
                                onTemplateSelected(
                                    """
TEXT [
    width: 1, ${'$'}opcional 
    height: 1, ${'$'}opcional
    content: "contenido"
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "OPEN QUESTION" to {
                                onTemplateSelected(
                                    """
OPEN_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional
    label: "texto"
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "DROP QUESTION" to {
                                onTemplateSelected(
                                    """
DROP_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional
    label: "texto",

    options: {"primera", "segunda"},

    correct: 0
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "SELECT QUESTION" to {
                                onTemplateSelected(
                                    """
SELECT_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional

    options: {"primera", "segunda"},

    correct: 0
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "MULTIPLE QUESTION" to {
                                onTemplateSelected(
                                    """
MULTIPLE_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional

    options: {"primera", "segunda"},

    correct: {0, 1}
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }

                        )
                    )

                    // ===== LAYOUTS =====
                    TemplateDropdownButton(
                        title = "LAYOUTS",
                        options = listOf(

                            "SECTION" to {
                                onTemplateSelected(
                                    """
SECTION [
    width: 1,    ${'$'}Opcional
    height: 1,  ${'$'}Opcional
    
    pointX: 0,
    pointY: 0,
    
    orientation: VERTICAL,  ${'$'}Opcional  
    
    elements: {
        ${'$'}Elementos
    }
    
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "TABLE" to {
                                onTemplateSelected(
                                    """
TABLE [
    width: 1, ${'$'}Opcional
    height: 1, ${'$'}Opcional
    
    pointX: 1,
    pointY: 1,
    
    orientation: VERTICAL,
    
    elements: {
        [
            {
                ${'$'}Elemento
            }
        ]
    }
    
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== EMOJIS =====
                    TemplateDropdownButton(
                        title = "EMOJIS",
                        options = listOf(

                            "SMILE" to {
                                onTemplateSelected(
                                    """
 @[:smile:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },

                            "SAD" to {
                                onTemplateSelected(
                                    """
 @[:sad:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "SERIOUS" to {
                                onTemplateSelected(
                                    """
 @[:serious:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },

                            "HEART" to {
                                onTemplateSelected(
                                    """
 @[:heart:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "STAR" to {
                                onTemplateSelected(
                                    """
 @[:star:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "CAT" to {
                                onTemplateSelected(
                                    """
 @[:cat:]
                                """.trimIndent()
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CONFIGURACIONES =====
                    TemplateDropdownButton(
                        title = "CONFIGURACIONES",
                        options = listOf(

                            "STYLES" to {
                                onTemplateSelected(
                                    """
    styles [
        "color": #000000,
        "background color": #000000,
        "font family": MONO,
        "text size": 1
    ]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "COLOR" to {
                                onTemplateSelected(
                                    """
        "color": #000000
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "BACKGROUND COLOR" to {
                                onTemplateSelected(
                                    """
        "background color": #000000
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "FONT FAMILY" to {
                                onTemplateSelected(
                                    """
        "font family": MONO
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "TEXT SIZE" to {
                                onTemplateSelected(
                                    """
        "text size": 1
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "BORDER" to {
                                onTemplateSelected(
                                    """
        "border": (1, DOTTED, #000000)
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CONDICIONALES =====
                    TemplateDropdownButton(
                        title = "CONDICIONALES",
                        options = listOf(

                            "IF" to {
                                onTemplateSelected(
                                    """
IF (condicion) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "IF - ELSE" to {
                                onTemplateSelected(
                                    """
IF (condicion) {
    ${'$'}Contenido
} ELSE {
    
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "ELSE" to {
                                onTemplateSelected(
                                    """
ELSE {
    ${'$'}Contenido 
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "ELSE IF" to {
                                onTemplateSelected(
                                    """
ELSE IF (condicion){
    ${'$'}Contenido 
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CICLOS =====
                    TemplateDropdownButton(
                        title = "CICLOS",
                        options = listOf(

                            "FOR" to {
                                onTemplateSelected(
                                    """
FOR (i = 0; i < 10; i = i + 1) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "WHILE" to {
                                onTemplateSelected(
                                    """
WHILE (condicion) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "DO-WHILE" to {
                                onTemplateSelected(
                                    """
DO {
    ${'$'}Contenido
} WHILE (condicion)
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "FOR-RANGO" to {
                                onTemplateSelected(
                                    """
FOR (i in 1..5) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== ELEMENTOS =====
                    TemplateDropdownButton(
                        title = "ELEMENTOS",
                        options = listOf(

                            "FILAS" to {
                                onTemplateSelected(
                                    """
    [
        {
            ${'$'}Elemento
        }
    ]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "COLUMNA" to {
                                onTemplateSelected(
                                    """
    
    {
            ${'$'}Elemento
    }
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "ORIENTATION" to {
                                onTemplateSelected(
                                    """
    
    orientation: VERTICAL,
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "FUNCION POKEMON" to {
                                onTemplateSelected(
                                    """
    
    who_is_that_pokemon(NUMBER, 1, 5),
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "LABEL" to {
                                onTemplateSelected(
                                    """
                                    
    label: "texto",
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                }
            }
        }
    )
}

//Consola para poder realizar el picker de colores
@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var selectedFormat by remember { mutableStateOf("HEX") }

    var selectedPresetName by remember { mutableStateOf<String?>(null) }

    val controller = rememberColorPickerController()

    AlertDialog(
        containerColor = Color(0xFF022841),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(25.dp),
        confirmButton = {
            TextButton(onClick = {

                val result = selectedPresetName ?: when (selectedFormat) {
                    "HEX" -> selectedColor.toHex()
                    "RGB" -> selectedColor.toRgb()
                    "HSL" -> selectedColor.toHsl()
                    else -> selectedColor.toHex()
                }

                onColorSelected(result)
                onDismiss()
            }) {
                Text("Insertar", color = Color(0xFF33CC12))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFFFFFFFF))
            }
        },
        text = {
            Column {

                Text(
                    text = "PRESETS",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(12.dp))

                val presets = listOf(
                    "RED" to Color(0xFFFF0000),
                    "GREEN" to Color(0xFF00FF00),
                    "BLUE" to Color(0xFF0000FF),
                    "PURPLE" to Color(0xFF9C27B0),
                    "SKY" to Color(0xFF03A9F4),
                    "YELLOW" to Color(0xFFFFEB3B),
                    "BLACK" to Color(0xFF000000),
                    "WHITE" to Color(0xFFFFFFFF)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    //Presets de la aplicacion propia
                    items(presets) { (name, colorValue) ->

                        Box(
                            modifier = Modifier
                                .background(colorValue, RoundedCornerShape(50))
                                .border(
                                    width = if (selectedColor == colorValue) 2.dp else 1.dp,
                                    color = if (selectedColor == colorValue)
                                        Color(0xFFBB86FC)
                                    else
                                        Color.DarkGray,
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable {
                                    selectedColor = colorValue
                                    selectedPresetName = name
                                    controller.selectByColor(colorValue, fromUser = true)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = name,
                                color = if (colorValue.luminance() > 0.5f)
                                    Color.Black
                                else
                                    Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }

                }

                Spacer(Modifier.height(20.dp))

                //Picker de colores
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
                    border = BorderStroke(3.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        HsvColorPicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            controller = controller,
                            onColorChanged = { envelope: ColorEnvelope ->
                                selectedColor = envelope.color
                                selectedPresetName = null
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ===== Botones de formato =====
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("HEX", "RGB", "HSL").forEach { format ->
                        val isSelected = selectedFormat == format
                        Surface(
                            onClick = { selectedFormat = format },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF6A1B9A) else Color.White.copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = format,
                                color = if (isSelected) Color.White else Color.Gray,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                val previewText = when (selectedFormat) {
                    "HEX" -> selectedColor.toHex()
                    "RGB" -> selectedColor.toRgb()
                    "HSL" -> selectedColor.toHsl()
                    else -> selectedColor.toHex()
                }

                // ===== Preview visual del color =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(selectedColor, RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.height(16.dp))

                // ===== Preview estilo del codigo =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212), RoundedCornerShape(12.dp))
                        .border(3.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {

                        Text(
                            text = "Vista previa: ",
                            color = Color(0xFF6A9955),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = previewText,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}


//Retorna formato HSL de colores
fun Color.toHsl(): String {
    val r = red
    val g = green
    val b = blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    var h = 0f
    val l = (max + min) / 2f
    val s = if (delta == 0f) 0f else delta / (1f - kotlin.math.abs(2f * l - 1f))

    if (delta != 0f) {
        h = when (max) {
            r -> ((g - b) / delta) % 6f
            g -> ((b - r) / delta) + 2f
            else -> ((r - g) / delta) + 4f
        }
        h *= 60f
        if (h < 0) h += 360f
    }

    return "<${h.toInt()}, ${(s * 100).toInt()}, ${(l * 100).toInt()}>"
}

//Retorna colores hexadecimales
fun Color.toHex(): String {
    return String.format(
        "#%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

//Retorna colores rgb
fun Color.toRgb(): String {
    return "(${(red * 255).toInt()}, ${(green * 255).toInt()}, ${(blue * 255).toInt()})"
}
