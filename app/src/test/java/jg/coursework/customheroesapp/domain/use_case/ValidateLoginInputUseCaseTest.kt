package jg.coursework.customheroesapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import jg.coursework.customheroesapp.domain.model.LoginInputValidationType
import org.junit.Test

class ValidateLoginInputUseCaseTest {
    private val validateLoginInputUseCase = ValidateLoginInputUseCase()

    @Test
    fun `test empty email field returns ValidationType empty field`() {
        val result = validateLoginInputUseCase(email = "", password = "password")
        assertThat(result).isEqualTo(LoginInputValidationType.EmptyField)
    }

    @Test
    fun `test empty password field returns ValidationType empty field`(){
        val result = validateLoginInputUseCase(email = "xd@xd.xd", password = "")
        assertThat(result).isEqualTo(LoginInputValidationType.EmptyField)
    }

    @Test
    fun `test no email returns ValidationType no email`(){
        val result = validateLoginInputUseCase(email = "xdxd.xd", password = "password")
        assertThat(result).isEqualTo(LoginInputValidationType.NoEmail)
    }

    @Test
    fun `test everything is correct return validation type valid`(){
        val result = validateLoginInputUseCase(email = "xd@xd.xd", password = "password")
        assertThat(result).isEqualTo(LoginInputValidationType.Valid)
    }
}