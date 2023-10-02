package me.dio.credit.application.system.controller

import jakarta.validation.Valid
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.response.CreditView
import me.dio.credit.application.system.dto.response.CreditViewList
import me.dio.credit.application.system.dto.response.CustomerView
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
  private val creditService: CreditService
) {

  @PostMapping
  fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
    val savedCredit: Credit = this.creditService.save(creditDto.toEntity())

    // Obtenha o customerId do Credit e defina-o no CreditView
    val creditView = CreditView(savedCredit)
    creditView.customerId = savedCredit.customer?.id ?: 1L // Defina um valor padrão se o customer for nulo

    return ResponseEntity.status(HttpStatus.CREATED).body(creditView)
  }


  @GetMapping
  fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
      ResponseEntity<List<CreditViewList>> {
    val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomer(customerId)
      .stream()
      .map { credit: Credit -> CreditViewList(credit) }
      .collect(Collectors.toList())
    return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
  }

  @GetMapping("/{creditCode}")
  fun findByCreditCode(
    @RequestParam(value = "customerId") customerId: Long,
    @PathVariable creditCode: UUID
  ): ResponseEntity<CreditView> {
    val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)
    return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
  }
}