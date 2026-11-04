package com.example.streamlite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF101010)
private val SurfaceDark = Color(0xFF202020)
private val Accent = Color(0xFFFF0033)
private val Muted = Color(0xFFAAAAAA)

data class Video(val title: String, val channel: String, val views: String, val age: String, val id: String, val tint: Color)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videos = listOf(
            Video("Beautiful moments from a day out", "Daily Vlogs", "12K views", "2 days ago", "ysz5S6PUM-U", Color(0xFF5B426D)),
            Video("The ultimate travel experience", "Explore Now", "8.4K views", "1 week ago", "ScMzIvxBSi4", Color(0xFF315C68)),
            Video("Relaxing music for your evening", "Sound Space", "45K views", "3 weeks ago", "jfKfPfyJRdk", Color(0xFF73513C)),
            Video("Simple ideas that make life better", "Creative Life", "3.2K views", "1 month ago", "aqz-KE-bpKQ", Color(0xFF435C43))
        )
        setContent { StreamLiteApp(videos) { id ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$id")))
        } }
    }
}

@Composable
fun StreamLiteApp(videos: List<Video>, openVideo: (String) -> Unit) {
    var tab by remember { mutableStateOf("Home") }
    MaterialTheme(colorScheme = darkColorScheme(background = Bg, surface = SurfaceDark, primary = Accent)) {
        Scaffold(
            containerColor = Bg,
            topBar = {
                Row(Modifier.fillMaxWidth().background(Bg).padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("▶", color = Accent, fontSize = 25.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("StreamLite", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.Cast, "Cast", tint = Color.White)
                    Spacer(Modifier.width(18.dp))
                    Icon(Icons.Default.Search, "Search", tint = Color.White)
                    Spacer(Modifier.width(18.dp))
                    Box(Modifier.size(32.dp).background(Color(0xFF6B4EFF), CircleShape), contentAlignment = Alignment.Center) {
                        Text("S", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF181818), contentColor = Color.White) {
                    listOf("Home", "Shorts", "Subscriptions", "Profile").forEach { item ->
                        NavigationBarItem(
                            selected = tab == item,
                            onClick = { tab = item },
                            icon = {
                                when(item) {
                                    "Home" -> Icon(Icons.Default.Home, null)
                                    "Shorts" -> Icon(Icons.Default.PlayCircle, null)
                                    "Subscriptions" -> Icon(Icons.Default.Subscriptions, null)
                                    else -> Icon(Icons.Default.Person, null)
                                }
                            },
                            label = { Text(item, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, indicatorColor = Color(0xFF454545), unselectedIconColor = Muted, selectedTextColor = Color.White, unselectedTextColor = Muted)
                        )
                    }
                }
            }
        ) { padding ->
            when(tab) {
                "Profile" -> ProfileScreen(Modifier.padding(padding))
                "Shorts" -> CenterScreen("Shorts", "Short-form video experience coming soon", Modifier.padding(padding))
                "Subscriptions" -> CenterScreen("Subscriptions", "Your followed channels will appear here", Modifier.padding(padding))
                else -> HomeScreen(videos, openVideo, Modifier.padding(padding))
            }
        }
    }
}

@Composable
fun HomeScreen(videos: List<Video>, openVideo: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize()) {
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "Music", "Vlogs", "Gaming", "Live").forEachIndexed { i, label ->
                    Box(Modifier.background(if(i == 0) Color.White else SurfaceDark, RoundedCornerShape(18.dp)).clickable { }.padding(horizontal = 15.dp, vertical = 8.dp)) {
                        Text(label, color = if(i == 0) Color.Black else Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
        items(videos) { video ->
            Column(Modifier.fillMaxWidth().clickable { openVideo(video.id) }.padding(bottom = 18.dp)) {
                Box(Modifier.fillMaxWidth().height(205.dp).background(video.tint), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.PlayCircle, null, tint = Color.White.copy(alpha = .9f), modifier = Modifier.size(58.dp))
                    Text("STREAMLITE", color = Color.White.copy(alpha = .45f), fontWeight = FontWeight.Black, fontSize = 17.sp)
                    Box(Modifier.align(Alignment.BottomEnd).padding(8.dp).background(Color.Black.copy(alpha = .8f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 3.dp)) {
                        Text("▶ WATCH", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.Top) {
                    Box(Modifier.size(38.dp).background(video.tint, CircleShape), contentAlignment = Alignment.Center) {
                        Text(video.channel.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(video.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("${video.channel} • ${video.views} • ${video.age}", color = Muted, fontSize = 12.sp)
                    }
                    Icon(Icons.Default.MoreVert, null, tint = Muted)
                }
            }
        }
        item { Text("Ad-free inside StreamLite • External YouTube playback may show ads", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(16.dp)) }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp)) {
        Text("Your profile", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(68.dp).background(Color(0xFF6B4EFF), CircleShape), contentAlignment = Alignment.Center) {
                Text("S", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("StreamLite User", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Local demo profile", color = Muted, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(28.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2022)), shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text("✦  PREMIUM STYLE", color = Color(0xFFFF6B83), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Text("A clean viewing experience", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("No ads are placed in this demo app. This does not activate or include YouTube Premium.", color = Color(0xFFD0C4C6), fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        listOf("Your channel", "Downloads (demo)", "Settings", "Help & feedback").forEach {
            Row(Modifier.fillMaxWidth().clickable { }.padding(vertical = 17.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Circle, null, tint = Muted, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(14.dp))
                Text(it, color = Color.White, fontSize = 15.sp)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = Muted)
            }
            Divider(color = Color(0xFF303030))
        }
    }
}

@Composable
fun CenterScreen(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.PlayCircleOutline, null, tint = Muted, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(12.dp))
        Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 13.sp)
    }
}
