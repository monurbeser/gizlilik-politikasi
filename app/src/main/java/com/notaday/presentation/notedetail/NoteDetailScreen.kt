package com.notaday.presentation.notedetail

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.notaday.domain.model.AttachmentType
import com.notaday.domain.model.Priority
import com.notaday.util.AttachmentValidator
import com.notaday.util.ShareFormatter
import java.time.LocalDateTime

@OptIn(ExperimentalPermissionsApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBack: () -> Unit,
    vm: NoteDetailViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value
    val note = state.note
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    val cameraPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null && AttachmentValidator.canAddAttachment(note.attachments.size) && AttachmentValidator.isValidSize(context, uri)) {
            vm.addAttachment(uri.toString(), "image-${System.currentTimeMillis()}.jpg", AttachmentType.IMAGE)
        }
    }
    val docPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null && AttachmentValidator.canAddAttachment(note.attachments.size) && AttachmentValidator.isValidSize(context, uri)) {
            vm.addAttachment(uri.toString(), "doc-${System.currentTimeMillis()}", AttachmentType.DOCUMENT)
        }
    }

    LaunchedEffect(noteId) { vm.load(noteId) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Note Details", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(note.title, vm::updateTitle, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(note.content, {
            vm.updateContent(it)
            vm.save()
        }, label = { Text("Content") }, modifier = Modifier.fillMaxWidth(), minLines = 4)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Convert to Todo")
            Switch(checked = note.isTodo, onCheckedChange = vm::updateIsTodo)
            if (note.isTodo) {
                Text("Done")
                Switch(checked = note.isCompleted == true, onCheckedChange = vm::updateCompleted)
            }
        }

        if (note.isTodo) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = note.priority?.name ?: "Select priority",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Priority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.name) },
                            onClick = {
                                vm.updatePriority(priority)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(onClick = { vm.updateDeadline(LocalDateTime.now().plusDays(1)) }) { Text("Set Deadline (+1 day)") }
            Button(onClick = { vm.updateReminder(LocalDateTime.now().plusHours(1)) }) { Text("Set Reminder (+1 hour)") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (cameraPermission.status.isGranted) imagePicker.launch("image/*") else cameraPermission.launchPermissionRequest()
            }) { Text("Add Photo") }
            Button(onClick = { docPicker.launch("application/*") }) { Text("Add Document") }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(note.attachments, key = { it.filePath + it.createdAt }) { attachment ->
                Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(attachment.fileName)
                    if (attachment.fileType == AttachmentType.IMAGE) {
                        AsyncImage(model = attachment.filePath, contentDescription = null)
                    }
                    HorizontalDivider()
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.save(onBack) }, modifier = Modifier.weight(1f)) { Text("Save") }
            TextButton(onClick = { vm.delete(onBack) }, modifier = Modifier.weight(1f)) { Text("Delete") }
            TextButton(onClick = {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, ShareFormatter.format(listOf(note)))
                }
                context.startActivity(Intent.createChooser(send, "Share note"))
            }) { Text("Share") }
        }
    }
}
