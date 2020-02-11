package com.bank.services

import com.bank.exceptions.AccountNotFoundException
import com.bank.store.AccountStore
import com.bank.store.entity.AccountEntity
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountServiceTest {

    companion object {
        private const val ACCOUNT_ID = "account-id"
    }
    @Mock
    private lateinit var accountStore : AccountStore

    @InjectMocks
    private lateinit var subject: AccountService

    @Test
    fun `getAccount - should return account not found exception`() {
        //given
        given(accountStore.getAccount(any()))
            .willThrow(AccountNotFoundException::class.java)

        //when
        val response = assertThatThrownBy {
            subject.getAccount(id = ACCOUNT_ID)
        }

        //then
        response
            .isInstanceOf(AccountNotFoundException::class.java)

        verify(accountStore).getAccount(ACCOUNT_ID)
    }

    @Test
    fun `getAccount - happy path`() {
        //given
        val expectedAccount = AccountEntity(
            id = ACCOUNT_ID,
            name = "name",
            balance = 10.0
        )
        given(accountStore.getAccount(any()))
            .willReturn(expectedAccount)

        //when
        val response = subject.getAccount(id = ACCOUNT_ID)

        //then
        assertThat(response).isEqualTo(expectedAccount)

        verify(accountStore).getAccount(ACCOUNT_ID)
    }
}