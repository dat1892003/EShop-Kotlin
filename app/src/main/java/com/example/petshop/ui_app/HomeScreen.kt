package com.example.petshop.ui_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon as M3Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage


data class Product(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val rating: Double,
    val price: Int,        // price in VND (no dots)
    val oldPrice: Int?,
    val badge: String? = null, // e.g. "-20%", "Mới"
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var cartCount by remember { mutableStateOf(3) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Tất cả") }

    val categories = listOf("Tất cả", "Áo", "Váy", "Quần", "Phụ kiện", "Giày")

    // sample data
    val products = remember {
        listOf(
            Product(1, "Áo Sơ Mi Nữ Cao Cấp", null, 4.5, 450000, 650000, "-31%"),
            Product(2, "Váy Dự Tiệc Sang Trọng", null, 5.0, 890000, 1200000, "-26%", isFavorite = false),
            Product(3, "Áo Phao Nam", null, 4.0, 750000, null, "Mới"),
            Product(4, "Giày Thể Thao", null, 4.2, 650000, 820000, "-20%"),
            Product(5, "Váy Xòe", null, 4.8, 520000, 780000, null),
            Product(6, "Túi Xách Thời Trang", null, 4.6, 330000, 450000, "-27%")
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF7F7FB))
    ) {




        if(selectedTab!=0){
            TopBar(cartCount = cartCount)
            Spacer(modifier = Modifier.height(8.dp))
            SearchAndFilters()
            Spacer(modifier = Modifier.height(8.dp))
            CategoryRow(categories = categories, selectedCategory = selectedCategory, onSelect = { selectedCategory = it })}
        else{
            CartScreen()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${products.size} sản phẩm", color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("Sắp xếp", color = Color.Gray, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Product grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(bottom = 80.dp), // leave space for bottom nav
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product, onToggleFavorite = {
                    // handle favorite toggle (for demo we do nothing)
                })
            }
        }
    }

    // Bottom navigation (overlay)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        NavigationBar {
            NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = { M3Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                label = { Text("Trang chủ") }
            )
            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = { M3Icon(Icons.Default.Search, contentDescription = "Tìm kiếm") },
                label = { Text("Tìm kiếm") }
            )
            NavigationBarItem(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = { M3Icon(Icons.Default.FavoriteBorder, contentDescription = "Yêu thích") },
                label = { Text("Yêu thích") }
            )
            NavigationBarItem(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                icon = {
                    // show badge on cart
                    if (cartCount > 0) {
                        BadgedBox( { Text(cartCount.toString()) }) {
                            M3Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng")
                        }
                    } else {
                        M3Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng")
                    }
                },
                label = { Text("Giỏ hàng") }
            )
        }
    }
}


@Composable
fun TopBar(cartCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* open drawer */ }) {
            M3Icon(Icons.Default.Menu, contentDescription = "menu")
        }
        Spacer(modifier = Modifier.width(4.dp))
        // Logo + Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFF7EB3), Color(0xFF8E54E9)),
                            start = Offset(0f, 0f),
                            end = Offset(100f, 100f)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = "logo", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Fashion Store", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /* bell */ }) {
            // little dot indicator
            Box {
                Icon(Icons.Default.NotificationsNone, contentDescription = "notifications")
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFFF1E88), CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun SearchAndFilters() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 0.dp,
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = "search", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tìm kiếm sản phẩm...", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            tonalElevation = 0.dp,
            modifier = Modifier
                .size(44.dp)
                .clickable { /* filter click */ }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Tune, contentDescription = "filter")
            }
        }
    }
}

@Composable
fun CategoryRow(categories: List<String>, selectedCategory: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.lazy.LazyRow(content = {
            items(categories.size) { idx ->
                val cat = categories[idx]
                val isSelected = cat == selectedCategory
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFFFF2E7D) else Color.White,
                        tonalElevation = if (isSelected) 4.dp else 0.dp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable { onSelect(cat) }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        })
    }
}

@Composable
fun ProductCard(product: Product, onToggleFavorite: (Int) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
            ) {
                // image - use coil AsyncImage or resource placeholder
                if (product.imageUrl != null) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    // placeholder local image
                    Box(modifier = Modifier.align(alignment = Alignment.Center)) {
                        Icon(Icons.Default.Image, contentDescription = "")
                    }
                }

                // badge top-left
                if (!product.badge.isNullOrEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (product.badge.contains("-")) Color(0xFFFF2E7D) else Color(0xFF00C853),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = product.badge,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }

                // favorite icon top-right
                IconButton(
                    onClick = { onToggleFavorite(product.id) },
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "wishlist", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(String.format("%.1f", product.rating), fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatVnd(product.price),
                    color = Color(0xFFFF2E7D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (product.oldPrice != null) {
                    Text(
                        text = formatVnd(product.oldPrice),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}

fun formatVnd(value: Int): String {
    // simple formatter: 450000 -> "450.000đ"
    val s = value.toString()
    val sb = StringBuilder()
    var cnt = 0
    for (i in s.length - 1 downTo 0) {
        sb.append(s[i])
        cnt++
        if (cnt == 3 && i != 0) {
            sb.append('.')
            cnt = 0
        }
    }
    return sb.reverse().toString() + "đ"
}
