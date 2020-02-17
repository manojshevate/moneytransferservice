package com.bank.services

import com.bank.base.exception.AccountNotFoundException
import com.bank.store.AccountStore
import com.bank.store.TransactionStore
import com.bank.store.entity.AccountEntity
import com.bank.store.entity.TransactionEntity
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
import java.sql.Timestamp
import java.time.Instant

@RunWith(MockitoJUnitRunner::class)
class AccountServiceTest {

    companion object {
        private const val ACCOUNT_ID = "account-id"
    }

    @Mock
    private lateinit var accountStore : AccountStore

    @Mock
    private lateinit var transactionStore: TransactionStore


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

    @Test
    fun `getTransactionsByAccountId - no transactions`() {
        //given
        given(transactionStore.getTransactionByAccountId(any()))
            .willReturn(emptyList())

        //when
        val response = subject.getTransactionsByAccountId(id = ACCOUNT_ID)

        //then
        assertThat(response).isEmpty()

        verify(transactionStore).getTransactionByAccountId(ACCOUNT_ID)
    }

    @Test
    fun `getTransactionsByAccountId - happy path`() {
        //given
        val now = Instant.now()
        given(transactionStore.getTransactionByAccountId(any()))
            .willReturn(listOf(
                TransactionEntity(id = "1", fromAccount = ACCOUNT_ID, toAccount = "dummy-account-1", amount = 10.0, createdDate = Timestamp.from(now)),
                TransactionEntity(id = "1", fromAccount = "dummy-account-1", toAccount = ACCOUNT_ID, amount = 5.0, createdDate = Timestamp.from(now))
            ))

        //when
        val response = subject.getTransactionsByAccountId(id = ACCOUNT_ID)

        //then
        assertThat(response.size).isEqualTo(2)

        verify(transactionStore).getTransactionByAccountId(ACCOUNT_ID)
    }
}