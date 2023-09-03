package by.yurkova.cleverbank.service;

import by.yurkova.cleverbank.dto.BankRequestDto;
import by.yurkova.cleverbank.dto.BankResponseDto;

import java.util.List;

/**
 * Service interface for managing banks.
 *
 * @author Yurkova Anastacia
 */
public interface BankService {

    BankResponseDto findById(Long id);

    BankResponseDto findByAccountId(Long accountId);

    List<BankResponseDto> findAll(int limit, int offset);

    BankResponseDto save(BankRequestDto bank);

    boolean updateById(Long id, BankRequestDto bankDto);

    boolean deleteById(Long id);
}
