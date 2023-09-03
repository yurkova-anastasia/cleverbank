package by.yurkova.cleverbank.service;

import by.yurkova.cleverbank.dto.AccountRequestDto;
import by.yurkova.cleverbank.dto.AccountResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for managing accounts.
 *
 * @author Yurkova Anastacia
 */
public interface AccountService {

    void processInterest();

    AccountResponseDto findById(Long id);

    AccountResponseDto findByNumber(String number);

    List<AccountResponseDto> findAll(int limit, int offset);

    AccountResponseDto save(AccountRequestDto account);

    boolean deleteById(Long id);

    boolean withdraw(String number, BigDecimal amount);

    boolean refill(String number, BigDecimal amount);

    boolean transfer(String senderNumber, String receiverNumber, BigDecimal amount);
}
