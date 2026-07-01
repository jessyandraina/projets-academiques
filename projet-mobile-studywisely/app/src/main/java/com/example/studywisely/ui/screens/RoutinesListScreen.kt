package com.example.studywisely.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studywisely.ui.navigation.Screen
import com.example.studywisely.ui.components.RoutineCard
import com.example.studywisely.ui.theme.*
import com.example.studywisely.viewmodel.routines_list.ListRoutinesViewModel

@Composable
fun RoutinesListScreen(
    navController: NavController,
    viewModel: ListRoutinesViewModel
) {
    val routines = viewModel.routines.value

    var showDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = BackgroundMain,

        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    "StudyWisely",
                    color = PurpleMain,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                )

                Text(
                    "Votre assistant de réussite",
                    color = PurpleMain,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    "Mes routines",
                    color = PurpleMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.AddEditRoutineScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMain),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        "Ajouter une nouvelle routine",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(routines, key = { it.id!! }) { routine ->
                    RoutineCard(
                        routine = routine,
                        onDelete = { id ->
                            routineToDelete = id
                            showDialog = true
                        },
                        onClick = {
                            navController.navigate("${Screen.AddEditRoutineScreen.route}?routineId=${routine.id}")
                        }
                    )
                }
            }

            if (showDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Voulez-vous vraiment supprimer cette routine ?",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = {
                                        routineToDelete?.let {
                                            viewModel.deleteRoutine(it)
                                        }
                                        showDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PurpleMain
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        "Oui",
                                        color = White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = {
                                        showDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LightLavender
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        "Non",
                                        color = Color.Black,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}