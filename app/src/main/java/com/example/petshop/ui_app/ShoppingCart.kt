package com.example.petshop.ui_app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class CartItem(
    val id: Int,
    val name: String,
    val size: String,
    val color: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {
    val cartItems = remember {
        mutableStateListOf(
            CartItem(1, "Váy Dự Tiệc Sang Trọng", "L", "Đen", 890_000.0, "https://i.imgur.com/7xXK6Yp.png", 1),
            CartItem(2, "Giày Thể Thao Nữ", "38", "Hồng", 680_000.0, "https://i.imgur.com/Tjl6L8g.png", 1)
        )
    }

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val shipping = 30_000.0
    val total = subtotal + shipping

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Giỏ Hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: back */ }) {
                        Icon(Icons.Default.Remove, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFF00AA), Color(0xFF7B61FF))
                        )
                    )
                    .clickable { /* TODO: Thanh toán */ }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Thanh Toán (%.0fđ)".format(total),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(cartItems, key = { it.id }) { item ->
                CartItemCard(item = item, onQuantityChange = {
                    item.quantity = it
                }, onRemove = {
                    cartItems.remove(item)
                })
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                DiscountSection()
                Spacer(modifier = Modifier.height(12.dp))
                SummarySection(subtotal, shipping, total)
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFECECEC), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(item.name, fontWeight = FontWeight.SemiBold)
            Text("Size: ${item.size}   Màu: ${item.color}", fontSize = 13.sp, color = Color.Gray)
            Text(
                "%,.0fđ".format(item.price),
                color = Color(0xFFFF007A),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.End)) {
                IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Tăng")
                }
                Text("${item.quantity}")
                IconButton(
                    onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Giảm")
                }
            }
        }
        IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.Top)) {
            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Gray)
        }
    }
}

@Composable
fun DiscountSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFECECEC), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Nhập mã giảm giá", color = Color.Gray)
        Text("Áp dụng", color = Color(0xFFFF007A), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SummarySection(subtotal: Double, shipping: Double, total: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFECECEC), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        SummaryRow("Tạm tính", subtotal)
        SummaryRow("Phí vận chuyển", shipping)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SummaryRow("Tổng cộng", total, highlight = true)
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text(text = "Thanh toán")
        }
    }
}

@Composable
fun SummaryRow(label: String, value: Double, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 15.sp)
        Text(
            "%,.0fđ".format(value),
            color = if (highlight) Color(0xFFFF007A) else Color.Black,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}