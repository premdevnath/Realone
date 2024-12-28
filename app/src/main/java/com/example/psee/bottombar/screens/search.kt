package com.example.psee.bottombar.screens

import androidx.compose.runtime.Composable
// Necessary Imports
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.database.*
 // Replace with your actual package and activity
import com.example.psee.R // Replace with your actual package
import com.example.psee.dataclass.rvmodel
import com.google.gson.Gson

@Composable
fun search(navController: NavController) {
    // Search query state to hold user input
    val searchQuery = remember { mutableStateOf("") }

    // List to hold search results
    val searchResults = remember { mutableStateListOf<rvmodel>() }
    val isLoading = remember { mutableStateOf(true) }

    // Filtered list based on search query
    val filteredResults = remember {
        derivedStateOf {
            searchResults.filter {
                it.Name?.contains(searchQuery.value, ignoreCase = true) == true ||
                        it.description?.contains(searchQuery.value, ignoreCase = true) == true ||
                        it.location?.contains(searchQuery.value, ignoreCase = true) == true
            }
        }
    }

    LaunchedEffect(Unit) {
        // Simulating search data retrieval
        searchDataRetrieve(searchResults, isLoading)
        val reversedList = searchResults.toList().reversed()
        searchResults.clear()
        searchResults.addAll(reversedList)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
                )
            )
    ) {
        Column {
            SearchHeaderSection(searchQuery) // Search bar with query input
            Spacer(modifier = Modifier.height(20.dp))
            SearchResultsSection(filteredResults.value, isLoading, navController ) // Display filtered results
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeaderSection(searchQuery: MutableState<String>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Search Your Dream Property",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Color(0xFF37474F)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Search bar with input field
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it }, // Update search query dynamically
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F3F4), RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = "Search here...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF37474F),
                unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
fun SearchResultsSection(
    searchResults: List<rvmodel>, // Use filtered results
    isLoading: MutableState<Boolean>,
    navController: NavController
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Search Results",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (searchResults.isEmpty()) {
            Text(
                text = "No search results found",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults.size) { index ->
                    PropertyCard2(searchResults[index], navController )
                }
            }
        }
    }
}

@Composable
fun PropertyCard2(item: rvmodel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                val itemJson = Gson().toJson(item)
                Log.e("Navigation", "Navigating with: $itemJson")
                navController.navigate("details_screen?itemJson=${Uri.encode(itemJson)}")


            }, // Spacing around the card
        shape = RoundedCornerShape(16.dp), // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Card background color
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp // Elevation for depth
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp) // Padding inside the card
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp) // Fixed size for the image
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners for the image
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Crop to fit the box
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp)) // Space between image and text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp) // Padding inside the column
            ) {
                Text(
                    text = item.Name ?: "Default Name",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = item.description ?: "Default description",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = item.location ?: "Default Location",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF2ABB34), // Green star color
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                        Text(
                            text = "₹${item.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

// Function to simulate data retrieval for search
fun searchDataRetrieve(searchResults: MutableList<rvmodel>, isLoading: MutableState<Boolean>) {
    val database = FirebaseDatabase.getInstance()
    val searchRef = database.reference.child("items") // Assuming search data is in this node

    searchRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            searchResults.clear()
            for (itemSnapshot in snapshot.children) {
                itemSnapshot.getValue(rvmodel::class.java)?.let {
                    searchResults.add(it)
                }
            }
            isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            isLoading.value = false
        }
    })
}
