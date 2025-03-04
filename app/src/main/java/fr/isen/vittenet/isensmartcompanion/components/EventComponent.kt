package fr.isen.vittenet.isensmartcompanion.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import fr.isen.vittenet.isensmartcompanion.R

@Composable
fun getCategoryColor(category: String): Color {
    return when (category) {
        "Vie associative" -> colorResource(id = R.color.vie_associative_color)
        "BDE" -> colorResource(id = R.color.bde_color)
        "BDS" -> colorResource(id = R.color.bds_color)
        "Professionnel" -> colorResource(id = R.color.professionnel_color)
        "Concours" -> colorResource(id = R.color.concours_color)
        "Institutionnel" -> colorResource(id = R.color.institutionnel_color)
        "Technologique" -> colorResource(id = R.color.technologique_color)
        "International" -> colorResource(id = R.color.international_color)
        else -> Color.Cyan
    }
}

@Composable
fun ColoredCircle(color: Color, size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        drawCircle(color = color)
    }
}