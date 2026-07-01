package com.example.studywisely.ui.screens

import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@Composable
fun MapPickerScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }

    val defaultLocation = LatLng(45.5017, -73.5673) // Montréal
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    fun searchLocation() {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            Toast.makeText(context, "Entre un lieu à rechercher", Toast.LENGTH_SHORT).show()
            return
        }

        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(trimmed, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        selectedPosition = latLng

                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                                durationMs = 1000
                            )
                        }
                    } else {
                        Toast.makeText(context, "Lieu introuvable", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(trimmed, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    selectedPosition = latLng

                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                            durationMs = 1000
                        )
                    }
                } else {
                    Toast.makeText(context, "Lieu introuvable", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Erreur réseau ou géocodage", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Impossible de rechercher ce lieu", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            Text(
                text = "Sélectionner un lieu",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Rechercher un lieu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { searchLocation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Rechercher")
            }

            Spacer(modifier = Modifier.height(12.dp))

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedPosition = latLng
                }
            ) {
                selectedPosition?.let { position ->
                    Marker(
                        state = MarkerState(position = position),
                        title = "Lieu sélectionné"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Latitude : ${selectedPosition?.latitude ?: "Non choisie"}")
                Text("Longitude : ${selectedPosition?.longitude ?: "Non choisie"}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val position = selectedPosition ?: return@Button

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_latitude", position.latitude)

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_longitude", position.longitude)

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedPosition != null
                ) {
                    Text("Valider et revenir")
                }
            }
        }
    }

}