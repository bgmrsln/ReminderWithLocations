package com.codemave.mobilecomputing.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.ui.edit.EditViewModel
import com.codemave.mobilecomputing.ui.home.categoryTask.CategoryTask
import com.codemave.mobilecomputing.ui.profile.ProfileViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Composable
fun Home(

    navController: NavController,
    username: String,
    viewModel: HomeViewModel = viewModel(),
    viewModel2: EditViewModel= viewModel()
) {
    val viewState by viewModel.state.collectAsState()

    val selectedCategory = viewState.selectedCategory

    if (viewState.categories.isNotEmpty() && selectedCategory != null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.clouds),
                contentDescription =null,
                modifier = Modifier,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
            HomeContent(
                selectedCategory = selectedCategory,
                categories = viewState.categories,
                onCategorySelected = viewModel::onCategorySelected,
                navController = navController,
                username= username,
                viewModel
            )

        }
    }

}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeContent(
    selectedCategory: Category,
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    navController: NavController,
    username: String,
    viewModel: HomeViewModel
) {
    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("virtual_location_data")
        ?.value


    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        backgroundColor= MaterialTheme.colors.primary.copy(alpha = 0.37f),
        floatingActionButton = {
            FloatingActionButton (

                onClick = {
                    navController.navigate(route = "task"  )  },
                contentColor = Color.White,
                backgroundColor= MaterialTheme.colors.secondary,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }

        },




    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.primary.copy(alpha = 0.37f)

            HomeAppBar(
                backgroundColor = appBarColor,
                navController= navController,
                username= username
            )



            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
            )

            OutlinedButton(
                onClick = {navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("virtual_location_data", latlng)
                    navController.navigate("secondmap")},
                modifier= Modifier.height(55.dp)
            ){
                if(latlng==null){
                    Text(text= "Virtual location")
                }else{
                    Text(text= "Virtual Location: ${latlng.latitude}, ${latlng.longitude}")
                }

            }



            CategoryTask(
                modifier = Modifier.fillMaxSize(),
                categoryId = selectedCategory.id,
                navController = navController

            )
        }
    }
}



@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navController: NavController,
    username: String

) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color =MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton( onClick = {} ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search))
            }
            IconButton( onClick = { navController.navigate("profile/${username}")} ) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account))
            }
            IconButton( onClick = { navController.navigate("login")} ) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = stringResource(R.string.Logout))
            }
        }
    )
}

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        indicator = emptyTabIndicator,
        modifier = Modifier.fillMaxWidth(),
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                ChoiceChipContent(
                    text = category.name,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> Color.White
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> Color.Black
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}