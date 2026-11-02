package com.example.lending.domain;

import java.time.LocalDate;

/** 貸出 */
public class Loan {

    private Long id;
    private Long equipmentId;
    private String borrower;
    private LocalDate lentOn;
    private LocalDate dueOn;
    private LocalDate returnedOn;

    public boolean isActive() {
        return returnedOn == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public LocalDate getLentOn() {
        return lentOn;
    }

    public void setLentOn(LocalDate lentOn) {
        this.lentOn = lentOn;
    }

    public LocalDate getDueOn() {
        return dueOn;
    }

    public void setDueOn(LocalDate dueOn) {
        this.dueOn = dueOn;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public void setReturnedOn(LocalDate returnedOn) {
        this.returnedOn = returnedOn;
    }
}
