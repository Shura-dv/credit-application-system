package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        // Given
        val fakeCustomer = buildCustomer()
        val fakeCredit = buildCredit(fakeCustomer)

        every { customerService.findById(fakeCustomer.id!!) } returns fakeCustomer
        every { creditRepository.save(any()) } returns fakeCredit

        // When
        val actual = creditService.save(fakeCredit)

        // Then
        assertThat(actual).isNotNull
        assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { customerService.findById(fakeCustomer.id!!) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all credits by customer`() {
        // given
        val customerId = 1L
        val fakeCustomer = buildCustomer(id = customerId)
        val fakeCredits = listOf(
            buildCredit(fakeCustomer, UUID.randomUUID(), BigDecimal.valueOf(1000.0)),
            buildCredit(fakeCustomer, UUID.randomUUID(), BigDecimal.valueOf(1500.0))
        )

        every { creditRepository.findAllByCustomerId(customerId) } returns fakeCredits

        // when
        val actual = creditService.findAllByCustomer(customerId)

        // then
        assertThat(actual).isNotNull
        assertThat(actual).hasSize(2)
        assertThat(actual).containsAll(fakeCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(customerId) }
    }


    @Test
    fun `should find credit by credit code and customer id`() {
        // Given
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val fakeCredit = buildCredit(buildCustomer(id = customerId), creditCode)

        every { creditRepository.findByCreditCode(creditCode) } returns fakeCredit

        // When
        val actual = creditService.findByCreditCode(customerId, creditCode)

        // Then
        assertThat(actual).isNotNull
        assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    @Test
    fun `should not find credit by invalid credit code`() {
        // given
        val customerId = 1L
        val creditCode = UUID.randomUUID()

        every { creditRepository.findByCreditCode(creditCode) } returns null

        // When
        // Then
        assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(customerId, creditCode) }
            .withMessage("Creditcode $creditCode not found")

        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    private fun buildCustomer(
        id: Long = 1L,
        firstName: String = "Shura",
        lastName: String = "Dio"
    ): Customer {
        return Customer(id = id, firstName = firstName, lastName = lastName)
    }

    private fun buildCredit(
        customer: Customer,
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0)
    ): Credit {
        return Credit(
            creditCode = creditCode,
            creditValue = creditValue,
            dayFirstInstallment = LocalDate.now(),
            numberOfInstallments = 12,
            customer = customer
        )
    }
}
