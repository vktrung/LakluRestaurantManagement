package com.laklu.pos.repositories;

import com.laklu.pos.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

}
