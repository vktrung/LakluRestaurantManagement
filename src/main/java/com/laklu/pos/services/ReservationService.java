package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.UpdateReservationDTO;
import com.laklu.pos.entities.Reservation;
import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Table;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.ReservationMapper;
import com.laklu.pos.repositories.ReservationRepository;
import com.laklu.pos.repositories.ReservationTableRepository;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TablesMustBeAvailable;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationService  implements ReservationByDateResolver {

    ReservationRepository reservationRepository;
    TableRepository tableRepository;
    ReservationTableRepository reservationTableRepository;
    ReservationMapper reservationMapper;

    @Transactional
    public Reservation createReservation(ReservationRequest request) {
        Reservation reservation = reservationMapper.toEntity(request);

        List<Table> tables = tableRepository.findAllById(request.getTableIds());

        RuleValidator.validate(new TablesMustBeAvailable(tables, null, this));

        reservation = reservationRepository.save(reservation);

        this.createReservationTables(reservation, tables, reservation.getReservationTime());

        return reservation;
    }


    public Reservation updateReservationInfo(Reservation reservation, UpdateReservationDTO dto) {
        reservationMapper.updateReservation(dto, reservation);

        return reservationRepository.save(reservation);
    }

    public Reservation addTablesToReservation(Reservation reservation, List<Integer> tableIds) {
        List<Table> tables = tableRepository.findAllExceptInReservation(tableIds, reservation);

        RuleValidator.validate(new TablesMustBeAvailable(tables, null, this));

        this.createReservationTables(reservation, tables, LocalDateTime.now());

        return reservation;
    }

    public void deleteTablesReservation(Reservation reservation, List<Integer> tableIds) {
        List<ReservationTable> reservationTables = reservationTableRepository.findByReservationAndTables(reservation, tableIds);
        this.reservationTableRepository.deleteAll(reservationTables);
    }



    public Optional<Reservation> findReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    public Reservation findOrFail(Integer id) {
        return this.findReservationById(id).orElseThrow(NotFoundException::new);
    }

    private void createReservationTables(Reservation reservation, List<Table> tables, LocalDateTime createdAt) {
        List<ReservationTable> reservationTables = tables.stream()
                .map(table -> ReservationTable.builder()
                        .reservation(reservation)
                        .table(table)
                        .createdAt(createdAt)
                        .build())
                .collect(Collectors.toList());

        this.reservationTableRepository.saveAll(reservationTables);
    }

    @Override
    public List<ReservationTable> resolveReservationsDate(LocalDate date, List<Table> tables) {

        return this.reservationTableRepository.findReservationsDate(date, tables);
    }
}