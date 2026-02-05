package com.notaday.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notaday.domain.model.Note
import com.notaday.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())

    val notes: StateFlow<List<Note>> = selectedDate
        .flatMapLatest(noteUseCases.getNotesByDate::invoke)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val date: StateFlow<LocalDate> = selectedDate

    fun onDateSelected(date: LocalDate) {
        selectedDate.value = date
    }
}
