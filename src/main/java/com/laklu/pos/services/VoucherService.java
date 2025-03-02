package com.laklu.pos.services;


import com.laklu.pos.entities.*;
import com.laklu.pos.enums.VoucherStatus;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public Voucher findOrFail(Integer id) {
        return this.getVoucherById(id).orElseThrow(NotFoundException::new);
    }

    public Optional<Voucher> getVoucherById(int voucherId) {
        return voucherRepository.findById(voucherId);
    }

    public List<Voucher> getAll() {
        return voucherRepository.findAll();
    }

    @Transactional
    public Voucher createVoucher(Voucher voucher) {
        if(voucherRepository.existsByCode(voucher.getCode())){
            throw new IllegalArgumentException("Voucher đã tồn tại!");
        }
        voucher.setCreatedAt(LocalDateTime.now());
        voucher.setUpdatedAt(LocalDateTime.now());
        voucher.setStatus(VoucherStatus.ACTIVE);

        return voucherRepository.save(voucher);
    }

    @Transactional
    public Voucher updateVoucher(int voucherId, Voucher updateVoucher) {
        Voucher editVoucher = findOrFail(voucherId);

        editVoucher.setCode(updateVoucher.getCode());
        editVoucher.setDiscountType(updateVoucher.getDiscountType());
        editVoucher.setDiscountValue(updateVoucher.getDiscountValue());
        editVoucher.setValidFrom(updateVoucher.getValidFrom());
        editVoucher.setValidUntil(updateVoucher.getValidUntil());
        editVoucher.setStatus(updateVoucher.getStatus());
        editVoucher.setUpdatedAt(LocalDateTime.now());

        return voucherRepository.save(editVoucher);
    }

    @Transactional
    public void deleteVoucher(int voucherId) {
        Voucher voucher = findOrFail(voucherId);
        voucherRepository.delete(voucher);
    }
}
