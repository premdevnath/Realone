package com.example.psee.bottombar.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psee.dataclass.rvmodel
import com.google.firebase.database.*
import com.example.psee.R
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.navigation.NavHostController

import com.google.gson.Gson


// Replace with your actual package name for R class
// Replace with your theme package if needed


//yaha image ko show ke liye gild ki jagah koil ka use karte hai
//RecyclerView Adapter -> LazyColumn: RecyclerView will be replaced by LazyColumn, a composable that handles lists efficiently.
//Glide -> Coil: We'll replace Glide with Coil for image loading in Jetpack Compose.
//Fragment -> Composable: The fragment's onCreateView and onViewCreated methods will be replaced with a composable function, which will handle UI rendering.
//BottomSheet: For bottom sheets, you can use ModalBottomSheet from Jetpack Compose.
//yaha home me lazy coloum jo bhi lagana hai

//naya code
//ye home,searh,profle iska sab ka navigation home page par hai to in page se data transfr karna hai to pahel vaha composable karna hoga
@Composable
fun home(navController: NavHostController) {
    // List to hold data
    val listData = remember { mutableStateListOf<rvmodel>() }
    // Show loading indicator or "No data available" if list is empty
    val isLoading = remember { mutableStateOf(true) }

    // Dominant background color ke liye state banayi
    val dominantColor = remember { mutableStateOf(Color.White) }
    LaunchedEffect(Unit) {

        // Retrieve data from Firebase
        dataRetrieve(listData, isLoading)
        val reversedList = listData.toList().reversed() // Reverse a copy of the list
        listData.clear() // Clear the original list
        listData.addAll(reversedList) // Add reversed items back
      //  listData.reversed()// List ko ulta kar diya taki new add item pahle dikhe

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    //gradint colour ke loiye
                    //hame colour lagane hai to Color(hexa code) aise bhi laga skate hai
                    //colors = listOf(Color(0xFFFDE8E8), Color(0xFFF2E3F3))
                   colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
                )
            )
    ) {
        Column {
            HeaderSection2()
            // Fetch data from Firebase when this composable is first launched
            Spacer(modifier = Modifier.height(20.dp))
            TrendingPropertiesSection2(listData,isLoading,navController)
        }

    }
}

@Composable
fun HeaderSection2() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "What are you Looking for?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Color(0xFF37474F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Icons Row
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            PropertyTypeCard2("Villa", R.drawable.g,Color(0xFFF3E5F5))
            PropertyTypeCard2("Apartment", R.drawable.ar, Color(0xFFFCE4EC))
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Second Row
        Row(
            horizontalArrangement = Arrangement.Start, // Icons left-align करेंगे
            modifier = Modifier.fillMaxWidth()
        ) {
            PropertyTypeCard2("Flat", R.drawable.flat, Color(0xFFE6FDE6))

            Spacer(modifier = Modifier.width(35.dp)) // Adjust spacing between "Flat" and "House"

            PropertyTypeCard2("House", R.drawable.home2, Color(0xFFE3F2FD))
        }
    }
}



@Composable
fun PropertyTypeCard2(name: String, icon: Int, bgColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically, // Icon aur Text same alignment me
        modifier = Modifier.padding(8.dp)
    ) {
        // Icon colored circle ke andar
        Box(
            contentAlignment = Alignment.Center,

            modifier = Modifier
                .size(80.dp)
                .background(bgColor, shape = CircleShape)
                .padding(12.dp)


        ) {
            //yaha hame pass colour full image hai to hum icon lagayenge to vo blak hoga hame image lagani higo
            /*
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = Modifier.size(36.dp),
                tint = Color.Unspecified// Icon apne real colors me
            )

             */
            // PNG image ke liye Image Composable ka use
            Image(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = Modifier.size(36.dp) // Icon ke size adjust karna
            )
        }
        Spacer(modifier = Modifier.width(16.dp)) // Icon aur Text ke beech spacing
        // Text circle ke right side
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}


@Composable
fun TrendingPropertiesSection2(
    listData: SnapshotStateList<rvmodel>,
    isLoading: MutableState<Boolean>,
    navController: NavHostController
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Trending Properties",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F)
            )

            ///
            Text(
                text = "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable {navController.navigate("search_screen")}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if (listData.isEmpty()) {
            Text(
                text = "No trending properties available",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        } else {
            val reversedList = listData.reversed()
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {



                //data ko revser karne ke liye ye kiya
                items(reversedList.size) { item -> // Reversed list
                    PropertyCard3(reversedList[item],navController) // Pass item directly
                }




/*
                //yaha item me batana hai ki kitne card honge to jinte listdata ki isze hongi
                items(listData.size) { index ->
                    PropertyCard(
                       item= listData[index]

                    )
                }

                     */

            }
        }
    }
}

// yaha se card ko clikable karenge

@Composable
fun PropertyCard3(item: rvmodel, navController: NavHostController) {


   Card(
        modifier = Modifier
            .size(width = 200.dp, height = 250.dp) // Fixed card size
            .padding(8.dp)
            //
            .clickable{


               // navController.navigate("item/${item}")


                val itemJson = Gson().toJson(item)
                Log.e("Navigation", "Navigating with: $itemJson")
                navController.navigate("details_screen?itemJson=${Uri.encode(itemJson)}")




                /*
                // Har parameter ko encode karte samay dhyan rakho
                val encodedName = Uri.encode(item.Name ?: "No name")
                val encodedDescription = Uri.encode(item.description ?: "No description")
                val encodedImageUrl = item.imageUrl // Image URL agar encoded hai, toh yahin rehne do
                val encodedLocation = Uri.encode(item.location ?: "Unknown location")
                val encodedPrice = Uri.encode(item.price ?: "0")

                // Final route banao
                val route = "propertyDetails?name=$encodedName&description=$encodedDescription&imageUrl=$encodedImageUrl&location=$encodedLocation&price=$encodedPrice"
                Log.e("NavigationRoute10", route)

                // Navigate karo details screen par
                navController.navigate(route)


                 */
                      }, // Spacing around the card
        shape = RoundedCornerShape(16.dp), // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Card background color
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp // Elevation for depth
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp) // Padding inside the card
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Fixed image height
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners for the image
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Crop to fit the box
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.Name ?: "Default Name",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = item.description ?: "Default description",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = item.location ?: "Default Location",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
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


// Function to retrieve data from Firebase and update the list
fun dataRetrieve(listData: MutableList<rvmodel>, isLoading: MutableState<Boolean>) {

    val database = FirebaseDatabase.getInstance()
    val dataRef = database.reference.child("items")

    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            listData.clear() // Clear existing data
            for (foodSnapshot in snapshot.children) {

                foodSnapshot.getValue(rvmodel::class.java)?.let {
                    listData.add(it) // Add the fetched item to the list

                //    listData.reversed()
                }
            }
            isLoading.value = false // Data fetched, stop loading indicator
        }

        override fun onCancelled(error: DatabaseError) {
            isLoading.value = false // Stop loading indicator if error occurs
        }
    })
}

fun showItemDetails(item: rvmodel) {
    // You can navigate to another screen or show more details of the item
}
/*
@Composable
fun PropertyCard(item: rvmodel) {
    // Card design ko improve kiya
    Box(
        modifier = Modifier

            .size(width = 200.dp, height = 250.dp) // Card ka fixed size
            .clip(RoundedCornerShape(5.dp)) // Corners ko rounded banaya
            .background(Color.White) // White background rakha
            .shadow(1.dp, shape = RoundedCornerShape(4.dp)) // Shadow add ki better effect ke liye
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Padding adjust ki better layout ke liye
        ) {
            // Image box ke liye changes kiye
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(120.dp) // Fixed height of the image
                    .clip(RoundedCornerShape(16.dp)) // Image ke liye rounded corners
            ) {
                AsyncImage(
                    model = item.imageUrl, // Firebase se image URL fetch karke load kiya
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Image ko crop kiya to fit the box
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp)) // Spacing between image and text
            // Property name and location text ko better alignment and styling di
            Text(text = item.Name ?: "Default Name", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(start = 10.dp))
            Text(text = item.location ?: "Default Location", fontSize = 12.sp, color = Color.Gray,modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.height(8.dp))
            // Price display ke liye Row design kiya
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF2ABB34), // Star icon ka color green rakha
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "₹${item.price}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

 */
/*
@Composable
fun home2() {
    // List to hold data
    val listData = remember { mutableStateListOf<rvmodel>() }
    // Show loading indicator or "No data available" if list is empty
    val isLoading = remember { mutableStateOf(true) }


    // Fetch data from Firebase when this composable is first launched
    LaunchedEffect(Unit) {

        // Retrieve data from Firebase
        dataRetrieve(listData, isLoading)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Image Slider (Static images for example)
        val imageList = listOf(
            R.drawable.mask, R.drawable.mask, R.drawable.mask
        )
        ImageSlider(imageList)

        // LazyColumn to display the fetched data
        if (isLoading.value) {
            // Show loading indicator while fetching data

            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Show data once loaded

            if (listData.isNotEmpty()) {

                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(listData.size) { index ->
                        MenuItemCard(item = listData[index])
                    }
                }
            } else {
                Text(
                    text = "No data available",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ImageSlider(imageList: List<Int>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(imageList.size) { index ->
            Image(
                painter = painterResource(id = imageList[index]),
                contentDescription = "Image $index",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MenuItemCard(item: rvmodel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showItemDetails(item) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Assuming imageUrl is a URL, so using a network image loading library like Coil
            Image(
                painter = rememberAsyncImagePainter(model = item.imageUrl),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                // Display the item name and price, with a default if not available
                Text(text = item.Name ?: "Default Name", style = MaterialTheme.typography.bodyMedium)
                Text(text = "₹${item.price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

*/









/*
@Composable
fun home() {
    val listData = remember { mutableStateListOf<rvmodel>() }

    // Fetch data from Firebase
    LaunchedEffect(Unit) {
        dataRetrieve(listData)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Image Slider
        val imageList = listOf(
            R.drawable.mask
           , R.drawable.mask, R.drawable.mask
        )
        ImageSlider(imageList)

        // LazyColumn to display list
        if (listData.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(listData.size) { index ->
                    MenuItemCard(item = listData[index])
                }
            }
        } else {
            Text(
                text = "No data available",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ImageSlider(imageList: List<Int>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(imageList.size) { index ->
            Image(
                painter = painterResource(id = imageList[index]),
                contentDescription = "Image $index",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MenuItemCard(item:rvmodel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showItemDetails(item) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = item.imageUrl),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {

                // Display the item name and price
                //name nhi ho to default name aajayega
                Text(text = item.Name ?: "Default Name", style = MaterialTheme.typography.bodyMedium)

                Text(text = "₹${item.price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

fun dataRetrieve(listData: MutableList<rvmodel>) {
    val database = FirebaseDatabase.getInstance()
    val dataRef = database.reference.child("menu")

    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            listData.clear()
            for (foodSnapshot in snapshot.children) {
                foodSnapshot.getValue(rvmodel::class.java)?.let {
                    listData.add(it)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle errors
        }
    })
}
fun showItemDetails(item: rvmodel) {
    // Navigate to the details screen, use Navigation for Compose
    // You can pass the item data to another screen
}

fun showBottomSheet() {
    // Show the bottom sheet using ModalBottomSheet
    // Implement the bottom sheet using ModalBottomSheetLayout
}


 */

