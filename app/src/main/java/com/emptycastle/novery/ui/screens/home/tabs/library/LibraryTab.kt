package com.emptycastle.novery.ui.screens.home.tabs.library

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.emptycastle.novery.R
import com.emptycastle.novery.data.repository.LibraryItem
import com.emptycastle.novery.domain.model.AppSettings
import com.emptycastle.novery.domain.model.DisplayMode
import com.emptycastle.novery.ui.screens.home.shared.LibraryStateHolder
import com.emptycastle.novery.domain.model.LibraryFilter
import com.emptycastle.novery.ui.components.DawnPill
import com.emptycastle.novery.ui.components.DawnSectionHeader
import com.emptycastle.novery.ui.components.NovelActionSheet
import com.emptycastle.novery.ui.components.NovelCard
import com.emptycastle.novery.ui.components.NovelListItem
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnNavy
import com.emptycastle.novery.ui.theme.DawnPanel
import com.emptycastle.novery.ui.theme.DawnViolet
import com.emptycastle.novery.ui.theme.DawnMagenta
import com.emptycastle.novery.ui.theme.NoveryTheme
import com.emptycastle.novery.util.calculateGridColumns

@Composable
fun LibraryTab(
    appSettings: AppSettings,
    onNavigateToDetails: (novelUrl: String, providerName: String) -> Unit,
    onNavigateToReader: (chapterUrl: String, novelUrl: String, providerName: String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val actionSheetState by viewModel.actionSheetState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val refreshState = rememberPullToRefreshState()
    val dimensions = NoveryTheme.dimensions

    LaunchedEffect(Unit) { LibraryStateHolder.initialize() }

    if (actionSheetState.isVisible && actionSheetState.data != null) {
        val data = actionSheetState.data!!
        NovelActionSheet(
            data = data,
            sheetState = sheetState,
            onDismiss = viewModel::hideActionSheet,
            onViewDetails = {
                viewModel.hideActionSheet()
                onNavigateToDetails(data.novel.url, data.novel.apiName)
            },
     onContinueReading = {
    viewModel.hideActionSheet()
    LibraryStateHolder.getLibraryItem(data.novel.url)?.lastReadPosition?.let {
        onNavigateToReader(
            it.chapterUrl,
            data.novel.url,
            data.novel.apiName
        )
    } ?: onNavigateToDetails(
        data.novel.url,
        data.novel.apiName
    )
},
           
            onAddToLibrary = null,
            onRemoveFromLibrary = { viewModel.removeFromLibrary(data.novel.url) },
            onRemoveFromHistory = null,
            onStatusChange = viewModel::updateReadingStatus
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refreshLibrary(context) },
            modifier = Modifier.fillMaxSize(),
            state = refreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = uiState.isRefreshing,
                    state = refreshState,
                    containerColor = DawnPanel,
                    color = DawnCyan
                )
            }
        ) {
            if (uiState.isLoading) {
                DawnLibraryLoading(modifier = Modifier.fillMaxSize())
            } else {
                val uniqueItems = remember(uiState.filteredItems) {
                    uiState.filteredItems.distinctBy { it.novel.url }
                }
                DawnLibraryContent(
                    uiState = uiState,
                    items = uniqueItems,
                    appSettings = appSettings,
                    dimensions = dimensions,
                    onQueryChange = viewModel::setSearchQuery,
                    onNotificationClick = onNavigateToNotifications,
                    onFilterChange = viewModel::onFilterChipPressed,
                    onNovelClick = { item ->
                        if (item.hasNewChapters) viewModel.acknowledgeNewChapters(item.novel.url)
                        val position = item.lastReadPosition
                        if (position != null) {
                            onNavigateToReader(position.chapterUrl, item.novel.url, item.novel.apiName)
                        } else {
                            onNavigateToDetails(item.novel.url, item.novel.apiName)
                        }
                    },
                    onNovelLongClick = viewModel::showActionSheet,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun DawnLibraryContent(
    uiState: LibraryUiState,
    items: List<LibraryItem>,
    appSettings: AppSettings,
    dimensions: com.emptycastle.novery.ui.theme.NoveryDimensions,
    onQueryChange: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onFilterChange: (LibraryFilter) -> Unit,
    onNovelClick: (LibraryItem) -> Unit,
    onNovelLongClick: (LibraryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val gridColumns = calculateGridColumns(appSettings.libraryGridColumns)
    val recent = items.firstOrNull { it.lastReadPosition != null } ?: items.firstOrNull()
    val shelfFilters = uiState.visibleFilters.take(6)

    when (appSettings.libraryDisplayMode) {
        DisplayMode.GRID -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                modifier = modifier,
                contentPadding = PaddingValues(
                    start = dimensions.gridPadding,
                    end = dimensions.gridPadding,
                    top = top + 12.dp,
                    bottom = 116.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "dawn_header") {
                    DawnHeader(
                        query = uiState.searchQuery,
                        onQueryChange = onQueryChange,
                        newCount = items.count { it.hasNewChapters },
                        total = uiState.items.size,
                        onNotificationClick = onNotificationClick
                    )
                }

                if (recent != null) {
                    item(span = { GridItemSpan(maxLineSpan) }, key = "dawn_continue") {
                        DawnContinueCard(recent, onClick = { onNovelClick(recent) })
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }, key = "dawn_shelves") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shelfFilters, key = { it.name }) { filter ->
                            DawnPill(
                                label = filter.displayName(),
                                selected = uiState.filter == filter,
                                onClick = { onFilterChange(filter) }
                            )
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }, key = "dawn_shelf_header") {
                    DawnSectionHeader(
                        title = "Your shelf",
                        subtitle = "${items.size} stories in this view"
                    )
                }

                itemsIndexed(items, key = { index, item -> "dawn_${item.novel.url}_$index" }) { _, item ->
                    NovelCard(
                        novel = item.novel,
                        onClick = { onNovelClick(item) },
                        onLongClick = { onNovelLongClick(item) },
                        newChapterCount = if (appSettings.showBadges) item.newChapterCount else 0,
                        readingStatus = if (appSettings.showBadges) item.readingStatus else null,
                        lastReadChapter = item.lastReadPosition?.chapterName,
                        density = appSettings.uiDensity
                    )
                }

                if (items.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }, key = "dawn_empty") {
                        DawnEmptyState(query = uiState.searchQuery, filter = uiState.filter)
                    }
                }
            }
        }
        DisplayMode.LIST -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(
                    start = dimensions.gridPadding,
                    end = dimensions.gridPadding,
                    top = top + 12.dp,
                    bottom = 116.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "dawn_header") {
                    DawnHeader(
                        query = uiState.searchQuery,
                        onQueryChange = onQueryChange,
                        newCount = items.count { it.hasNewChapters },
                        total = uiState.items.size,
                        onNotificationClick = onNotificationClick
                    )
                }
                if (recent != null) {
                    item(key = "dawn_continue") {
                        DawnContinueCard(recent, onClick = { onNovelClick(recent) })
                    }
                }
                item(key = "dawn_shelves") {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        shelfFilters.forEach { filter ->
                            DawnPill(
                                label = filter.displayName(),
                                selected = uiState.filter == filter,
                                onClick = { onFilterChange(filter) }
                            )
                        }
                    }
                }
                item(key = "dawn_shelf_header") {
                    DawnSectionHeader(
                        title = "Your shelf",
                        subtitle = "${items.size} stories in this view"
                    )
                }
                itemsIndexed(items, key = { index, item -> "dawn_${item.novel.url}_$index" }) { _, item ->
                    NovelListItem(
                        novel = item.novel,
                        onClick = { onNovelClick(item) },
                        onLongClick = { onNovelLongClick(item) },
                        newChapterCount = if (appSettings.showBadges) item.newChapterCount else 0,
                        readingStatus = if (appSettings.showBadges) item.readingStatus else null,
                        lastReadChapter = item.lastReadPosition?.chapterName,
                        density = appSettings.uiDensity
                    )
                }
                if (items.isEmpty()) {
                    item(key = "dawn_empty") { DawnEmptyState(query = uiState.searchQuery, filter = uiState.filter) }
                }
            }
        }
    }
}

@Composable
private fun DawnHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    newCount: Int,
    total: Int,
    onNotificationClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.dawn_launcher_round),
                contentDescription = "Dawn",
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dawn",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (total == 0) "Your next story starts here" else "${total} stories · keep reading",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Rounded.NotificationsNone, "Notifications")
                }
                if (newCount > 0) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).size(9.dp),
                        shape = RoundedCornerShape(50),
                        color = DawnMagenta,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.background)
                    ) {}
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(23.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.Search, null, tint = DawnCyan)
                androidx.compose.foundation.text.BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorationBox = { inner ->
                        Box {
                            if (query.isBlank()) {
                                Text("Search your library...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            inner()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DawnContinueCard(item: LibraryItem, onClick: () -> Unit) {
    val novel = item.novel
    val chapter = item.lastReadPosition?.chapterName
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(188.dp)
                .clip(RoundedCornerShape(30.dp))
        ) {
            AsyncImage(
                model = novel.posterUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                DawnNavy.copy(alpha = 0.97f),
                                DawnNavy.copy(alpha = 0.82f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(20.dp)
                    .padding(end = 130.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "CONTINUE READING",
                    style = MaterialTheme.typography.labelSmall,
                    color = DawnCyan,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = novel.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                chapter?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.72f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (chapter != null) "Tap to continue" else "Open story",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.78f)
                )
            }
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp)
                    .size(50.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(listOf(DawnCyan, DawnMagenta))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.MenuBook, null, tint = DawnNavy, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun DawnEmptyState(query: String, filter: LibraryFilter) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = when (filter) {
                    LibraryFilter.READING -> Icons.Rounded.MenuBook
                    LibraryFilter.COMPLETED -> Icons.Rounded.CheckCircle
                    LibraryFilter.DOWNLOADED -> Icons.Rounded.CloudDownload
                    LibraryFilter.PLAN_TO_READ -> Icons.Rounded.BookmarkAdd
                    LibraryFilter.ON_HOLD -> Icons.Rounded.PauseCircle
                    else -> Icons.Rounded.AutoAwesome
                },
                contentDescription = null,
                tint = DawnViolet,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = if (query.isBlank()) "Nothing here yet" else "No stories found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (query.isBlank()) "Explore novels and bring your next world into Dawn." else "Try another title, author, or keyword.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DawnLibraryLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(Modifier.height(18.dp))
        repeat(5) { index ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (index == 0) 56.dp else 86.dp),
                shape = RoundedCornerShape(if (index == 0) 22.dp else 26.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {}
        }
    }
}
