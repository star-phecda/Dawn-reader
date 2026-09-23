package com.emptycastle.novery.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emptycastle.novery.ui.theme.DawnBlue
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta
import com.emptycastle.novery.ui.theme.DawnNavy
import com.emptycastle.novery.ui.theme.DawnViolet

/** Dawn's signature floating expressive navigation capsule. */
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

@Composable
fun NoveryBottomNavBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalizedSelectedRoute = selectedRoute.removePrefix("tab_")
    Surface(
        modifier = modifier.widthIn(max = 430.dp),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.96f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 12.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 7.dp, vertical = 7.dp)
                .animateContentSize(animationSpec = spring(stiffness = 500f)),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                DawnNavItem(
                    item = item,
                    selected = item.route == normalizedSelectedRoute,
                    onClick = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
private fun DawnNavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val tint by animateColorAsState(
        targetValue = if (selected) DawnNavy else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = 650f),
        label = "dawn_nav_tint"
    )

    Surface(
        modifier = Modifier.clickable(
            interactionSource = source,
            indication = null,
            onClick = onClick
        ),
        shape = RoundedCornerShape(23.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .then(
                    if (selected) {
                        Modifier.background(
                            Brush.horizontalGradient(
                                listOf(DawnCyan, DawnBlue, DawnMagenta)
                            ),
                            RoundedCornerShape(23.dp)
                        )
                    } else Modifier
                )
                .padding(horizontal = if (selected) 15.dp else 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(if (selected) 7.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                modifier = Modifier.size(22.dp),
                tint = tint
            )
            AnimatedContent(targetState = selected, label = "dawn_nav_label") { showLabel ->
                if (showLabel) {
                    Text(
                        text = item.label,
                        color = tint,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
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
            .padding(horizontal = 12.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        NoveryBottomNavBar(
            selectedRoute = selectedRoute,
            onItemSelected = onItemSelected
        )
    }
}
