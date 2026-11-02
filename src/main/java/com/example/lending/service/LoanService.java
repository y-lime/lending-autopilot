package com.example.lending.service;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lending.domain.Equipment;
import com.example.lending.domain.EquipmentStatus;
import com.example.lending.domain.Loan;
import com.example.lending.mapper.EquipmentMapper;
import com.example.lending.mapper.LoanMapper;

@Service
@Transactional
public class LoanService {

    /** 1人が同時に借りられる備品の上限 */
    public static final int MAX_ACTIVE_LOANS_PER_BORROWER = 3;

    /** 貸出日数の上限 */
    public static final int MAX_LOAN_DAYS = 14;

    private final EquipmentMapper equipmentMapper;
    private final LoanMapper loanMapper;
    private final Clock clock;

    public LoanService(EquipmentMapper equipmentMapper, LoanMapper loanMapper, Clock clock) {
        this.equipmentMapper = equipmentMapper;
        this.loanMapper = loanMapper;
        this.clock = clock;
    }

    public Loan lend(long equipmentId, String borrower, int days) {
        if (days < 1 || days > MAX_LOAN_DAYS) {
            throw new BusinessException("INVALID_DAYS", "貸出日数は1〜" + MAX_LOAN_DAYS + "日で指定してください");
        }
        Equipment equipment = equipmentMapper.findById(equipmentId)
                .orElseThrow(() -> new NotFoundException("備品が見つかりません: id=" + equipmentId));
        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new BusinessException("NOT_AVAILABLE", "この備品は貸出できません: status=" + equipment.getStatus());
        }
        if (loanMapper.countActiveByBorrower(borrower) >= MAX_ACTIVE_LOANS_PER_BORROWER) {
            throw new BusinessException("LIMIT_EXCEEDED",
                    "同時に借りられるのは" + MAX_ACTIVE_LOANS_PER_BORROWER + "件までです");
        }

        // 注意: 「読んでから書く」ため、同時に貸出要求が来ると二重貸出になりうる（Issue #3）
        equipmentMapper.updateStatus(equipmentId, EquipmentStatus.LENT);

        LocalDate today = LocalDate.now(clock);
        Loan loan = new Loan();
        loan.setEquipmentId(equipmentId);
        loan.setBorrower(borrower);
        loan.setLentOn(today);
        loan.setDueOn(today.plusDays(days));
        loanMapper.insert(loan);
        return loan;
    }

    public Loan giveBack(long loanId) {
        Loan loan = loanMapper.findById(loanId)
                .orElseThrow(() -> new NotFoundException("貸出が見つかりません: id=" + loanId));
        if (!loan.isActive()) {
            throw new BusinessException("ALREADY_RETURNED", "この貸出は返却済みです");
        }
        LocalDate today = LocalDate.now(clock);
        loanMapper.markReturned(loanId, today);
        equipmentMapper.updateStatus(loan.getEquipmentId(), EquipmentStatus.AVAILABLE);
        loan.setReturnedOn(today);
        return loan;
    }
}
