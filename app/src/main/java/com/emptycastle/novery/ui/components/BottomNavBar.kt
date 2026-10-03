package com.emptycastle.novery.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("library", "Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    BottomNavItem("browse", "Browse", Icons.Filled.Explore, Icons.Outlined.Explore),
    BottomNavItem("foryou", "For You", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    BottomNavItem("history", "History", Icons.Filled.History, Icons.Outlined.History),
    BottomNavItem("more", "More", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
)

/**
 * Expressive floating navigation shell.
 *
 * The navigation semantics come from Material 3 NavigationBar/NavigationBarItem,
 * while the outer Surface gives Dawn the compact floating-capsule treatment.
 */
@Composable
fun NoveryBottomNavBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalizedSelectedRoute = selectedRoute.removePrefix("tab_")

    Surface(
        modifier = modifier.widthIn(max = 560.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.98f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.72f)
        ),
        tonalElevation = 4.dp,
        shadowElevation = 12.dp
    ) {
        NavigationBar(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            bottomNavItems.forEach { item ->
                val selected = item.route == normalizedSelectedRoute
                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(item.route) },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        androidx.compose.material3.Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun NoveryBottomNavBarWithInsets(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        NoveryBottomNavBar(
            selectedRoute = selectedRoute,
            onItemSelected = onItemSelected
        )
    }
}
