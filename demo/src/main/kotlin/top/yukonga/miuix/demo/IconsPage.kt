// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.icon.extended.AddCircle
import top.yukonga.miuix.kmp.icon.extended.Alarm
import top.yukonga.miuix.kmp.icon.extended.Album
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Backup
import top.yukonga.miuix.kmp.icon.extended.CallRecording
import top.yukonga.miuix.kmp.icon.extended.Clear
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.CloudFill
import top.yukonga.miuix.kmp.icon.extended.Community
import top.yukonga.miuix.kmp.icon.extended.Contacts
import top.yukonga.miuix.kmp.icon.extended.Copy
import top.yukonga.miuix.kmp.icon.extended.Create
import top.yukonga.miuix.kmp.icon.extended.Delete
import top.yukonga.miuix.kmp.icon.extended.Download
import top.yukonga.miuix.kmp.icon.extended.Edit
import top.yukonga.miuix.kmp.icon.extended.Email
import top.yukonga.miuix.kmp.icon.extended.ExpandLess
import top.yukonga.miuix.kmp.icon.extended.ExpandMore
import top.yukonga.miuix.kmp.icon.extended.Favorites
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.icon.extended.Filter
import top.yukonga.miuix.kmp.icon.extended.Folder
import top.yukonga.miuix.kmp.icon.extended.Forward
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Help
import top.yukonga.miuix.kmp.icon.extended.Hide
import top.yukonga.miuix.kmp.icon.extended.Home
import top.yukonga.miuix.kmp.icon.extended.Image
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Layers
import top.yukonga.miuix.kmp.icon.extended.Link
import top.yukonga.miuix.kmp.icon.extended.ListView
import top.yukonga.miuix.kmp.icon.extended.Location
import top.yukonga.miuix.kmp.icon.extended.Lock
import top.yukonga.miuix.kmp.icon.extended.Messages
import top.yukonga.miuix.kmp.icon.extended.Mic
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.icon.extended.Music
import top.yukonga.miuix.kmp.icon.extended.Notes
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.icon.extended.Paste
import top.yukonga.miuix.kmp.icon.extended.Pause
import top.yukonga.miuix.kmp.icon.extended.Phone
import top.yukonga.miuix.kmp.icon.extended.Photos
import top.yukonga.miuix.kmp.icon.extended.Pin
import top.yukonga.miuix.kmp.icon.extended.Play
import top.yukonga.miuix.kmp.icon.extended.Refresh
import top.yukonga.miuix.kmp.icon.extended.Remove
import top.yukonga.miuix.kmp.icon.extended.Rename
import top.yukonga.miuix.kmp.icon.extended.Reply
import top.yukonga.miuix.kmp.icon.extended.Scan
import top.yukonga.miuix.kmp.icon.extended.ScreenCapture
import top.yukonga.miuix.kmp.icon.extended.Search
import top.yukonga.miuix.kmp.icon.extended.Send
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.icon.extended.Share
import top.yukonga.miuix.kmp.icon.extended.Show
import top.yukonga.miuix.kmp.icon.extended.Sort
import top.yukonga.miuix.kmp.icon.extended.Stopwatch
import top.yukonga.miuix.kmp.icon.extended.Tasks
import top.yukonga.miuix.kmp.icon.extended.Theme
import top.yukonga.miuix.kmp.icon.extended.Timer
import top.yukonga.miuix.kmp.icon.extended.Translate
import top.yukonga.miuix.kmp.icon.extended.Unlock
import top.yukonga.miuix.kmp.icon.extended.Update
import top.yukonga.miuix.kmp.icon.extended.UploadCloud
import top.yukonga.miuix.kmp.icon.extended.VolumeOff
import top.yukonga.miuix.kmp.icon.extended.VolumeUp
import top.yukonga.miuix.kmp.icon.extended.WorldClock
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun IconsPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val icons = remember {
        listOf(
            "Home" to MiuixIcons.Home,
            "Settings" to MiuixIcons.Settings,
            "Search" to MiuixIcons.Search,
            "Email" to MiuixIcons.Email,
            "Messages" to MiuixIcons.Messages,
            "Phone" to MiuixIcons.Phone,
            "Contacts" to MiuixIcons.Contacts,
            "Alarm" to MiuixIcons.Alarm,
            "Timer" to MiuixIcons.Timer,
            "Stopwatch" to MiuixIcons.Stopwatch,
            "WorldClock" to MiuixIcons.WorldClock,
            "Location" to MiuixIcons.Location,
            "Scan" to MiuixIcons.Scan,
            "ScreenCapture" to MiuixIcons.ScreenCapture,
            "Add" to MiuixIcons.Add,
            "AddCircle" to MiuixIcons.AddCircle,
            "Remove" to MiuixIcons.Remove,
            "Close" to MiuixIcons.Close,
            "Clear" to MiuixIcons.Clear,
            "Ok" to MiuixIcons.Ok,
            "Back" to MiuixIcons.Back,
            "Forward" to MiuixIcons.Forward,
            "Reply" to MiuixIcons.Reply,
            "Send" to MiuixIcons.Send,
            "Share" to MiuixIcons.Share,
            "Download" to MiuixIcons.Download,
            "UploadCloud" to MiuixIcons.UploadCloud,
            "Edit" to MiuixIcons.Edit,
            "Create" to MiuixIcons.Create,
            "Delete" to MiuixIcons.Delete,
            "Copy" to MiuixIcons.Copy,
            "Paste" to MiuixIcons.Paste,
            "Rename" to MiuixIcons.Rename,
            "Refresh" to MiuixIcons.Refresh,
            "Update" to MiuixIcons.Update,
            "Filter" to MiuixIcons.Filter,
            "Sort" to MiuixIcons.Sort,
            "Info" to MiuixIcons.Info,
            "Help" to MiuixIcons.Help,
            "Lock" to MiuixIcons.Lock,
            "Unlock" to MiuixIcons.Unlock,
            "Show" to MiuixIcons.Show,
            "Hide" to MiuixIcons.Hide,
            "Pin" to MiuixIcons.Pin,
            "Link" to MiuixIcons.Link,
            "Folder" to MiuixIcons.Folder,
            "File" to MiuixIcons.File,
            "Favorites" to MiuixIcons.Favorites,
            "Notes" to MiuixIcons.Notes,
            "Tasks" to MiuixIcons.Tasks,
            "Community" to MiuixIcons.Community,
            "More" to MiuixIcons.More,
            "GridView" to MiuixIcons.GridView,
            "ListView" to MiuixIcons.ListView,
            "ExpandLess" to MiuixIcons.ExpandLess,
            "ExpandMore" to MiuixIcons.ExpandMore,
            "VolumeUp" to MiuixIcons.VolumeUp,
            "VolumeOff" to MiuixIcons.VolumeOff,
            "Mic" to MiuixIcons.Mic,
            "Pause" to MiuixIcons.Pause,
            "Play" to MiuixIcons.Play,
            "Theme" to MiuixIcons.Theme,
            "Translate" to MiuixIcons.Translate,
            "Album" to MiuixIcons.Album,
            "Backup" to MiuixIcons.Backup,
            "CloudFill" to MiuixIcons.CloudFill,
            "Layers" to MiuixIcons.Layers,
            "CallRecording" to MiuixIcons.CallRecording,
            "Image" to MiuixIcons.Image,
            "Photos" to MiuixIcons.Photos,
            "Music" to MiuixIcons.Music,
        )
    }

    LazyVerticalGrid(
        modifier = modifier
            .overScrollVertical()
            .scrollEndHaptic(),
        columns = GridCells.Fixed(4),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SmallTitle(
                text = "${icons.size} Icons from MiuixIcons",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
        items(icons) { (name, icon) ->
            IconGridItem(name = name, icon = icon)
        }
    }
}

@Composable
private fun IconGridItem(name: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = MiuixTheme.colorScheme.onBackground,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = name,
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )
        }
    }
}
