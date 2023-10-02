package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired lateinit var customerRepository: CustomerRepository

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired lateinit var creditRepository: CreditRepository

    @Autowired lateinit var objectMapper: ObjectMapper


    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setUp() {
        val customer = Customer(
            id = 1L,
        )
        customerRepository.save(customer)

        val creditCode1 = UUID.fromString("49f740be-46a7-449b-84e7-ff5b7986d7ef")
        val creditCode2 = UUID.fromString("aabbccdd-1234-5678-8765-4321aabbccdd")

        val credit1 = Credit(
            creditCode = creditCode1,
            creditValue = BigDecimal.valueOf(1000.0),
            dayFirstInstallment = LocalDate.now(),
            numberOfInstallments = 12,
            customer = customer
        )
        creditRepository.save(credit1)

        val credit2 = Credit(
            creditCode = creditCode2,
            creditValue = BigDecimal.valueOf(2000.0),
            dayFirstInstallment = LocalDate.now(),
            numberOfInstallments = 24,
            customer = customer
        )
        creditRepository.save(credit2)
    }


    @AfterEach
    fun tearDown() { creditRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        // Given
        val creditDto: CreditDto = buildCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            // Then
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return list of credits by customerId`() {
        // Given
        val customerId = 1L

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/credits")
                .param("customerId", customerId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditValue").value(2000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberOfInstallments").isNumber)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return a specific credit by creditCode`() {
        // Given
        val customerId = 1L
        val creditCode = UUID.fromString("49f740be-46a7-449b-84e7-ff5b7986d7ef")

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/credits/{creditCode}", creditCode)
                .param("customerId", customerId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customerId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(0.0))
    }


    private fun buildCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(7),
        numberOfInstallments: Int = 12,
        customerId: Long = 1L
    ): CreditDto {
        return CreditDto(
            creditValue = creditValue,
            dayFirstOfInstallment = dayFirstOfInstallment,
            numberOfInstallments = numberOfInstallments,
            customerId = customerId
        )
    }

}
