package com.example.psee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.*


import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.psee.dataclass.rvmodel


//kuch kaam ka nhi

//dynemic colour chnage

/*
@Composable
fun home() {
    // Firebase se data fetch karne ke liye list and loading state maintain kiya
    val listData = remember { mutableStateListOf<rvmodel>() }
    val isLoading = remember { mutableStateOf(true) }

    // Dominant background color ke liye state banayi
    val dominantColor = remember { mutableStateOf(Color.White) }

    LaunchedEffect(Unit) {
        // Firebase se data retrieve kiya
        dataRetrieve(listData, isLoading)

        // Image ka dominant color extract karke background change karte hain
        listData.firstOrNull()?.imageUrl?.let { imageUrl ->
            calculateDominantColor(imageUrl) { color ->
                dominantColor.value = color
            }
        }
    }

    // Dynamic background color apply kiya
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor.value) // Yaha background color dynamically change hoga
    ) {
        Column {
            HeaderSection2()
            Spacer(modifier = Modifier.height(20.dp))
            TrendingPropertiesSection2(listData, isLoading)
        }
    }
}

// Function to calculate dominant color from the image
fun calculateDominantColor(imageUrl: String, onColorCalculated: (Color) -> Unit) {
    // Coil library ka use karke image load ki aur uska dominant color extract kiya
    val context = LocalContext.current
    ImageLoader(context).enqueue(
        ImageRequest.Builder(context)
            .data(imageUrl)
            .target { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                Palette.from(bitmap).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let { colorValue ->
                        onColorCalculated(Color(colorValue)) // Background color ko update kiya
                    }
                }
            }
            .build()
    )
}

@Composable
fun PropertyCard(item: rvmodel) {
    // Card design ko improve kiya
    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 250.dp) // Card ka fixed size
            .clip(RoundedCornerShape(16.dp)) // Corners ko rounded banaya
            .background(Color.White) // White background rakha
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // Shadow add ki better effect ke liye
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Padding adjust ki better layout ke liye
        ) {
            // Image box ke liye changes kiye
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
            Text(text = item.Name ?: "Default Name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = item.location ?: "Default Location", fontSize = 12.sp, color = Color.Gray)
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
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PropertyAppUI()
        }
    }
}

 */
//bottom nav ka simple tarika
@Composable
fun PropertyAppUI() {
    @Composable
    fun InstagramBottomBar() {
        BottomNavigation(
            backgroundColor = Color.White, // Background white hoga
            contentColor = Color.Black     // Icons black color me rahenge
        ) {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = true,
                onClick = { /* Home icon pe click karne ka action yaha hoga */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") },
                selected = false,
                onClick = { /* Search icon ka action yaha hoga */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                label = { Text("Add") },
                selected = false,
                onClick = { /* Add button pe click karne ka action */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
                label = { Text("Notifications") },
                selected = false,
                onClick = { /* Notifications ke liye action */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = false,
                onClick = { /* Profile button ke liye action */ }
            )
        }
    }

    /*
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFDE8E8), Color(0xFFF2E3F3))
                )
            )
    ) {
        Column {
            HeaderSection()
            Spacer(modifier = Modifier.height(20.dp))
            TrendingPropertiesSection()
        }
        FloatingBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun HeaderSection() {
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
            PropertyTypeCard("House", R.drawable.g, Color(0xFFEDE7F6))
            PropertyTypeCard("Condo", R.drawable.g, Color(0xFFFCE4EC))
            PropertyTypeCard("Villa", R.drawable.g, Color(0xFFE8F5E9))
            PropertyTypeCard("Apartment", R.drawable.g, Color(0xFFE3F2FD))
        }
    }

     */
}

@Composable
fun PropertyTypeCard(name: String, icon: Int, bgColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(80.dp)
            .background(bgColor, shape = CircleShape)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = name,
            tint = Color.Black,
            modifier = Modifier.size(36.dp)
        )
        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TrendingPropertiesSection(

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
            Text(
                text = "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable { /* Navigate to All Properties */ }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {
            items(2) { index ->
                PropertyCard(
                    name = if (index == 0) "Wiley's Cottage" else "Town Center",
                    location = if (index == 0) "Brookline" else "Dorchester",
                    rating = 4.5f
                )
            }
        }
    }
}

@Composable
fun PropertyCard(name: String, location: String, rating: Float) {
    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 250.dp)
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .shadow(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = location, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFF17DA24),
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "$rating", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FloatingBottomBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFF37474F), shape = RoundedCornerShape(50))
            .shadow(8.dp, shape = RoundedCornerShape(50))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            BottomNavItem("Home", Icons.Default.Home)
            BottomNavItem("Search", Icons.Default.Search)
            BottomNavItem("Chat", Icons.Default.List)
            BottomNavItem("Favorites", Icons.Default.Favorite)
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White)
        Text(text = label, fontSize = 12.sp, color = Color.White)
    }
}


@Preview(showBackground = true)
@Composable
fun PropertyAppUIPreview() {
    PropertyAppUI()
}

