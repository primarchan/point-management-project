package com.example.pointmanagementproject.point.reservation;

import com.example.pointmanagementproject.point.IdEntity;
import com.example.pointmanagementproject.point.wallet.PointWallet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointReservation extends IdEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    PointWallet pointWallet;

    @Column(name = "amount", nullable = false, columnDefinition = "BIGINT")
    BigInteger amount;

    @Column(name = "earned_date", nullable = false)
    LocalDate earnedDate;

    @Column(name = "available_days", nullable = false)
    int availableDays;

    @Column(name = "is_executed", columnDefinition = "TINYINT", length = 1)
    boolean executed;

    public PointReservation(
            PointWallet pointWallet,
            BigInteger amount,
            LocalDate earnedDate,
            int availableDays
    ) {
        this.pointWallet = pointWallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.availableDays = availableDays;
        this.executed = false;
    }

    public void execute() {
        this.executed = true;
    }

    public LocalDate getExpireDate() {
        return this.earnedDate.plusDays(this.availableDays);
    }

}
