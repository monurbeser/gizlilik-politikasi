package com.notaday

import com.notaday.domain.model.Note
import com.notaday.util.ShareFormatter
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ShareFormatterTest {
    @Test
    fun `formats todo and note with symbols`() {
        val output = ShareFormatter.format(
            listOf(
                Note(date = LocalDate.parse("2026-01-01"), title = "Plan", content = "Finish sketch", isTodo = true, isCompleted = false),
                Note(date = LocalDate.parse("2026-01-01"), title = "Idea", content = "Use clay textures", isTodo = false)
            )
        )
        assertTrue(output.contains("☐ Plan"))
        assertTrue(output.contains("• Idea"))
    }
}
