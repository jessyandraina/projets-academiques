package com.example.studywisely.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studywisely.model.local.RoutineModel
import com.example.studywisely.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.net.toUri

@Composable
fun RoutineCard(
    routine: RoutineModel,
    onDelete: (Int) -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.FRENCH)

    val routineFormatted = routine.routineDateTimeMillis?.let { sdf.format(Date(it)) } ?: "Non définie"
    val examFormatted = routine.examDateTimeMillis?.let { sdf.format(Date(it)) } ?: "Non définie"

    val hasValidLocation = routine.latitude != 0.0 || routine.longitude != 0.0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = routine.title,
                    color = PurpleMain,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                Text(
                    text = "Priorité: ${routine.priority}",
                    fontSize = 18.sp
                )

                Text(
                    text = "À faire le: $routineFormatted",
                    fontSize = 18.sp
                )

                Text(
                    text = "Examen/Livrable: $examFormatted",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val gmmIntentUri =
                            "google.navigation:q=${routine.latitude},${routine.longitude}".toUri()

                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }

                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            val fallbackUri =
                                "https://www.google.com/maps/dir/?api=1&destination=${routine.latitude},${routine.longitude}".toUri()
                            val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
                            context.startActivity(fallbackIntent)
                        }
                    },
                    enabled = hasValidLocation
                ) {
                    Text("Itinéraire vers le lieu")
                }
            }

            IconButton(
                onClick = {
                    routine.id?.let { onDelete(it) }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = PurpleMain,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}