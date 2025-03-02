package com.laklu.pos.repositories;

import com.laklu.pos.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    boolean existsByCode(String code);
}
