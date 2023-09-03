package by.yurkova.cleverbank.service;

import by.yurkova.cleverbank.dto.TransactionRequestDto;
import by.yurkova.cleverbank.dto.TransactionResponseDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing transactions.
 *
 * @author Yurkova Anastacia
 */
public interface TransactionService {

    TransactionResponseDto findById(Long id);

    List<TransactionResponseDto> findAll(int limit, int offset);

    List<TransactionResponseDto> findAllForPeriod(String number, LocalDate from, LocalDate to);

    TransactionResponseDto save(TransactionRequestDto transactionDto);
}
