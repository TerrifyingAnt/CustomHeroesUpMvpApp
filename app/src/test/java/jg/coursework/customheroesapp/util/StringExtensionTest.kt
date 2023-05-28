package jg.coursework.customheroesapp.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtensionTest {

    @Test
    fun `test string contains no number returns false when check for it`() {
        val result = "NoNumber".containsNumber()
        assertThat(result).isFalse()
    }

    @Test
    fun `test string contains number returns true when check for it`() {
        val result = "YesNumber5".containsNumber()
        assertThat(result).isTrue()
    }

    @Test
    fun `test string contains no upper case returns false when check for it`() {
        val result = "nobigchonkyletters".containsUpperCase()
        assertThat(result).isFalse()
    }

    @Test
    fun `test string contains upper case returns true when check for it`() {
        val result = "BigChonkyLetters".containsUpperCase()
        assertThat(result).isTrue()
    }

    @Test
    fun `test string contains no special char returns false when check for it`() {
        val result = "nospecialchar".containsSpecialChar()
        assertThat(result).isFalse()
    }

    @Test
    fun `test string contains special char returns true when check for it`() {
        val result = "!specialchar!".containsSpecialChar()
        assertThat(result).isTrue()
    }
}