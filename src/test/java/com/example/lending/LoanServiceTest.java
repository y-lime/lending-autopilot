package com.example.lending;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.example.lending.domain.EquipmentStatus;
import com.example.lending.domain.Loan;
import com.example.lending.mapper.EquipmentMapper;
import com.example.lending.service.BusinessException;
import com.example.lending.service.LoanService;
import com.example.lending.service.NotFoundException;

@SpringBootTest
@Import(TestClockConfig.class)
@Transactional
class LoanServiceTest {

    @Autowired
    LoanService loanService;

    @Autowired
    EquipmentMapper equipmentMapper;

    @Test
    void 貸出すると備品が貸出中になり返却期限が設定される() {
        Loan loan = loanService.lend(1L, "tanaka", 7);

        assertThat(loan.getId()).isNotNull();
        assertThat(loan.getLentOn()).isEqualTo(LocalDate.of(2026, 10, 15));
        assertThat(loan.getDueOn()).isEqualTo(LocalDate.of(2026, 10, 22));
        assertThat(equipmentMapper.findById(1L).orElseThrow().getStatus()).isEqualTo(EquipmentStatus.LENT);
    }

    @Test
    void 貸出中の備品は借りられない() {
        assertThatThrownBy(() -> loanService.lend(4L, "tanaka", 7))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("貸出できません");
    }

    @Test
    void 廃棄済みの備品は借りられない() {
        assertThatThrownBy(() -> loanService.lend(5L, "tanaka", 7))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void 存在しない備品はNotFound() {
        assertThatThrownBy(() -> loanService.lend(999L, "tanaka", 7))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 貸出日数は1日から14日まで() {
        assertThatThrownBy(() -> loanService.lend(1L, "tanaka", 0)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> loanService.lend(1L, "tanaka", 15)).isInstanceOf(BusinessException.class);
    }

    @Test
    void 同時に借りられるのは3件まで() {
        loanService.lend(1L, "suzuki", 3);
        loanService.lend(2L, "suzuki", 3);
        loanService.lend(3L, "suzuki", 3);

        assertThatThrownBy(() -> loanService.lend(6L, "suzuki", 3))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("3件まで");
    }

    @Test
    void 返却すると備品が貸出可能に戻る() {
        Loan loan = loanService.lend(1L, "tanaka", 7);

        Loan returned = loanService.giveBack(loan.getId());

        assertThat(returned.getReturnedOn()).isEqualTo(LocalDate.of(2026, 10, 15));
        assertThat(equipmentMapper.findById(1L).orElseThrow().getStatus()).isEqualTo(EquipmentStatus.AVAILABLE);
    }

    @Test
    void 返却済みの貸出は再度返却できない() {
        Loan loan = loanService.lend(1L, "tanaka", 7);
        loanService.giveBack(loan.getId());

        assertThatThrownBy(() -> loanService.giveBack(loan.getId()))
                .isInstanceOf(BusinessException.class);
    }
}
