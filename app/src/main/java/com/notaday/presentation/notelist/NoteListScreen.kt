package com.notaday.presentation.notelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NoteListScreen(
    onOpenDetail: (Long) -> Unit,
    vm: NoteListViewModel = hiltViewModel()
) {
    val notes = vm.notes.collectAsStateWithLifecycle().value
    val query = remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query.value,
            onValueChange = {
                query.value = it
                vm.setQuery(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search notes") }
        )
        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            items(notes, key = { it.id }) { note ->
                Card(onClick = { onOpenDetail(note.id) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(note.date.toString())
                        Text(note.title.ifBlank { "Untitled note" })
                        Text(note.content.take(120))
                    }
                }
            }
        }
    }
}
