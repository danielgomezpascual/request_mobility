package com.personal.metricas.transacciones.ui.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.formas.MA_Circulo
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.transacciones.ui.entidades.EstadoProceso
import com.personal.metricas.transacciones.ui.entidades.TransaccionesUI


@Preview(backgroundColor = 0xFFEBEBEB, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO or android.content.res.Configuration.UI_MODE_TYPE_DESK, showBackground = true, showSystemUi = false)
@Composable
fun TestItemTransaccion() {
    val trx: TransaccionesUI = TransaccionesUI(
        mobRequestId = 788978,
        programVersion = "1.34.0.162",
        creationDate = "2025-04-08",
        estado = EstadoProceso.OK,
        reqStatus = 0,
        numero = "12313",
        tipoMov = "ALB_MAN",
        lectoraFisica = "DFN0001",
        lectoraLogica = "DE0101",
        usuarioLectora = "dgomez",
        reqMessage = "Eerror",
        parentRequestId = 78897,
        redoneByReqId = -11,
        doneForReqId = -1,
        proceso = "MOBLITY",
        languageCode = "ES",
        xmlHash = "",
        reqResult = "",
        reqResultDetail = "",
        reqResultDate = "",
    )
    ItemTransacciones(trx){}
}

@Composable
fun ItemTransacciones(trx: TransaccionesUI, onClick: (TransaccionesUI)->Unit) {
    Column {


      //  var estado  by remember{ mutableStateOf<Boolean>(trx.seleccionada)}
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .clickable {
                   // estado = !estado
                    onClick(trx)
                }
                .padding(4.dp).
                    background(color = if (trx.seleccionada) {
                        Color.Yellow} else { Color.Transparent})
            ,

            verticalAlignment = Alignment.Companion.CenterVertically
        ) {





                        //ImagenDrawable(R.drawable)
            MA_Circulo(color = trx.estado.color, size = 48.dp )
            Spacer(modifier = Modifier.Companion.width(16.dp))

            // Nombre y detalles
            Column {
                Row {
                    MA_LabelNormal("#${trx.visible}# ")
                    MA_LabelNormal("#${trx.seleccionada}# ")
                    MA_LabelNormal(trx.numero)
                }

                Row {
                    MA_LabelNormal(trx.reqStatus.toString())
                    MA_LabelNormal(trx.tipoMov)
                }
            }
        }
        HorizontalDivider()
    }
}

