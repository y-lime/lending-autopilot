package com.example.lending.web;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.lending.domain.Loan;
import com.example.lending.service.LoanService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    public record LendRequest(@NotNull Long equipmentId, @NotBlank String borrower, @NotNull Integer days) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan lend(@Validated @RequestBody LendRequest request) {
        return loanService.lend(request.equipmentId(), request.borrower(), request.days());
    }

    @PostMapping("/{id}/return")
    public Loan giveBack(@PathVariable("id") long id) {
        return loanService.giveBack(id);
    }
}
