package com.example.psee

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.psee.bottombar.screens.homepage
import com.example.psee.bottombar.screens.post
import com.example.psee.login.SplashScreen
import com.example.psee.login.login
import com.example.psee.login.signup


//hum ek compose fun se dusare compose ko argumnet nhi de skate hai vo alag alag page par ho to

//val detailsRoute = "details/{description}/{imageUrl}/{name}/{location}/{price}"

@Composable
fun navigation(navController: NavHostController, newviewmodel: viewmodle) {
    NavHost(navController, startDestination = "ss") {
        composable("ss") {

            SplashScreen(navController)
            /*
            //Splash Screen दिखाने के बाद, onTimeout() के जरिए लॉगिन स्क्रीन पर नेविगेट होता है।
            //Login Screen पर जाने के बाद Splash Screen को back stack से हटा दिया जाता है, जिससे यूजर वापस Splash पर नहीं जा सकता।
            SplashScreen {
                // Navigate to login screen after splash timeout
                navController.navigate("login") {
                    popUpTo("ss") { inclusive = true } // Remove splash from back stack
                }
            }

             */
        }


        composable("login") {
            login(navController, newviewmodel)
        }
        composable("register") {
            signup(navController, newviewmodel)
        }

        composable("home") {
            homepage(navController, newviewmodel)

        }

        composable("post") {
            post(navController)

        }
      
        /*

        composable(
            route = "details_screen?itemJson={itemJson}",
            arguments = listOf(
                navArgument("itemJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val itemJson = backStackEntry.arguments?.getString("itemJson")
            val item = itemJson?.let { Gson().fromJson(it, rvmodel::class.java) }
            SimplePropertyDetailsScreen(navController, item)
        }

         */

        /*
        Log.e("NavigationRoute23", detailsRoute)
        //
        // Define the details screen and receive arguments
        // Use detailsRoute to navigate
        Log.e("NavigationRoute15", detailsRoute)
        composable(
            route = "propertyDetails?name={name}&description={description}&imageUrl={imageUrl}&location={location}&price={price}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType; defaultValue = "No name" },
                navArgument("description") {
                    type = NavType.StringType; defaultValue = "No description"
                },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("location") {
                    type = NavType.StringType; defaultValue = "Unknown location"
                },
                navArgument("price") { type = NavType.StringType; defaultValue = "0" }
            )
        ) { backStackEntry ->

            // Retrieve arguments
            val name = backStackEntry.arguments?.getString("name")
            val description = backStackEntry.arguments?.getString("description")
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")
            val location = backStackEntry.arguments?.getString("location")
            val price = backStackEntry.arguments?.getString("price")

            // Log the arguments for debugging
            Log.e(
                "NavigationRouteArgs",
                "Name: $name, Description: $description, ImageURL: $imageUrl, Location: $location, Price: $price"
            )

            // Call your property details UI
            Simple
            PropertyDetailsScreen(description, imageUrl, name, location, price)

        }

         */
    }


}


/*
        composable("details/{description}/{imageUrl}/{name}/{location}/{price}") { backStackEntry ->
            val description = backStackEntry.arguments?.getString("description")
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")
            val name = backStackEntry.arguments?.getString("name")
            val location = backStackEntry.arguments?.getString("location")
            val price = backStackEntry.arguments?.getString("price")

            SimplePropertyDetailsScreen(
                description = description,
                imageUrl = imageUrl,
                Name = name,
                location = location,
                price = price
            )
        }

         */
/*
  // Correctly define the detailsScreen with a parameter
        // Correctly define the detailsScreen with a parameter
        composable("detailsScreen/{propertyItem}") { backStackEntry ->
            // Retrieve the argument passed through the navigation route
            val propertyItem = backStackEntry.arguments?.getString("propertyItem")

            // If propertyItem is not null, pass it to the SimplePropertyDetailsScreen
            if (propertyItem != null) {
                SimplePropertyDetailsScreen(propertyItem)
            } else {
                Log.d("Navigation", "No item found")
            }
        }
 */