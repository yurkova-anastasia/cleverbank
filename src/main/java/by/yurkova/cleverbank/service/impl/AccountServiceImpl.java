package by.yurkova.cleverbank.service.impl;

import by.yurkova.cleverbank.config.AppConfig;
import by.yurkova.cleverbank.dto.AccountRequestDto;
import by.yurkova.cleverbank.dto.AccountResponseDto;
import by.yurkova.cleverbank.exception.EntityNotFoundException;
import by.yurkova.cleverbank.mapper.AccountMapper;
import by.yurkova.cleverbank.model.Account;
import by.yurkova.cleverbank.model.Transaction;
import by.yurkova.cleverbank.repository.AccountRepository;
import by.yurkova.cleverbank.repository.TransactionRepository;
import by.yurkova.cleverbank.service.AccountService;
import by.yurkova.cleverbank.util.check.CheckGenerator;
import by.yurkova.cleverbank.util.yaml.YMLParser;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service implementation for managing accounts.
 *
 * @author Yurkova Anastacia
 */
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private final YMLParser yamlParser;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.yamlParser = AppConfig.getYamlParser();
    }

    /**
     * Process interest accrual on accounts.
     */
    public void processInterest() {
        BigDecimal interestRate = yamlParser.getYaml().getInterestRate()
                .divide(BigDecimal.valueOf(100));
        if (shouldApplyInterest()) {
            accountRepository.accrueInterest(interestRate);
        }
    }

    private boolean shouldApplyInterest() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getDayOfMonth() == currentDate.lengthOfMonth();
    }

    /**
     * Find an account by its unique identifier (ID).
     *
     * @param id The unique identifier of the account.
     * @return The AccountResponseDto representing the found account.
     * @throws EntityNotFoundException If no account with the given ID is found.
     */
    @Override
    public AccountResponseDto findById(Long id) {
        return accountMapper.toDto(
                accountRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Account with id = " + id + " was not found")));
    }

    /**
     * Find an account by its account number.
     *
     * @param number The account number.
     * @return The AccountResponseDto representing the found account.
     * @throws EntityNotFoundException If no account with the given account number is found.
     */
    @Override
    public AccountResponseDto findByNumber(String number) {
        return accountMapper.toDto(
                accountRepository.findByNumber(number).orElseThrow(() ->
                        new EntityNotFoundException("Account with number = " + number + " was not found")));
    }

    /**
     * Retrieve a list of accounts with pagination support.
     *
     * @param limit  The maximum number of accounts to retrieve.
     * @param offset The starting index for pagination.
     * @return A list of AccountResponseDto representing accounts.
     */
    @Override
    public List<AccountResponseDto> findAll(int limit, int offset) {
        return accountMapper.toListOfDto(accountRepository.findAll(limit, offset));
    }

    /**
     * Save a new account based on the provided AccountRequestDto.
     *
     * @param accountRequestDto The AccountRequestDto containing account information.
     * @return The AccountResponseDto representing the saved account.
     */
    @Override
    public AccountResponseDto save(AccountRequestDto accountRequestDto) {
        Account account = accountMapper.fromDto(accountRequestDto);
        return accountMapper.toDto(accountRepository.save(account));
    }

    /**
     * Withdraw a specified amount from an account.
     *
     * @param number The account number from which to withdraw funds.
     * @param amount The amount to be withdrawn.
     * @return True if the withdrawal was successful, otherwise false.
     * @throws EntityNotFoundException       If the account with the given number is not found.
     * @throws UnsupportedOperationException If the bank associated with the account is not CleverBank or
     *                                       if there are insufficient funds.
     */
    @Override
    public boolean withdraw(String number, BigDecimal amount) {
        Account account = accountRepository.blockingFindByNumber(number).orElseThrow(() ->
                new EntityNotFoundException("Account with number = " + number + " was not found"));
        if (!account.getBankId().equals(1L)) {
            throw new UnsupportedOperationException("Bank must be CleverBank");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new UnsupportedOperationException("Not enough money on this account");
        }
        Transaction withdrawTransaction = accountRepository.withdraw(account, amount);
        transactionRepository.save(withdrawTransaction);
        CheckGenerator.generateCheck(withdrawTransaction, account, null);
        return true;
    }

    /**
     * Refill an account with a specified amount.
     *
     * @param number The account number to be refilled.
     * @param amount The amount to be refilled.
     * @return True if the refill was successful, otherwise false.
     * @throws EntityNotFoundException       If the account with the given number is not found.
     * @throws UnsupportedOperationException If the bank associated with the account is not CleverBank.
     */
    @Override
    public boolean refill(String number, BigDecimal amount) {
        Account account = accountRepository.blockingFindByNumber(number).orElseThrow(() ->
                new EntityNotFoundException("Account with id = " + number + " was not found"));
        if (!account.getBankId().equals(1L)) {
            throw new UnsupportedOperationException("Bank must be CleverBank");
        }
        Transaction refillTransaction = accountRepository.refill(account, amount);
        transactionRepository.save(refillTransaction);
        CheckGenerator.generateCheck(refillTransaction, null, account);
        return true;
    }

    /**
     * Transfer a specified amount from one account to another.
     *
     * @param senderNumber   The account number from which to transfer funds.
     * @param receiverNumber The account number to which funds are to be transferred.
     * @param amount         The amount to be transferred.
     * @return True if the transfer was successful, otherwise false.
     * @throws EntityNotFoundException       If either the sender or receiver account is not found.
     * @throws UnsupportedOperationException If the bank associated with either account is not CleverBank or
     *                                       if there are insufficient funds.
     */
    @Override
    public boolean transfer(String senderNumber, String receiverNumber, BigDecimal amount) {
        Account sender = accountRepository.blockingFindByNumber(senderNumber).orElseThrow(() ->
                new EntityNotFoundException("Account with number = " + senderNumber + " was not found"));
        Account receiver = accountRepository.blockingFindByNumber(receiverNumber).orElseThrow(() ->
                new EntityNotFoundException("Account with number = " + receiverNumber + " was not found"));
        if (!(sender.getBankId().equals(1L) || receiver.getBankId().equals(1L))) {
            throw new UnsupportedOperationException("Sender or receiver bank must be CleverBank");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new UnsupportedOperationException("Not enough money on sender account");
        }
        Transaction transferTransaction = accountRepository.transfer(sender, receiver, amount);
        transactionRepository.save(transferTransaction);
        CheckGenerator.generateCheck(transferTransaction, sender, receiver);
        return true;
    }

    /**
     * Delete an account by its unique identifier (ID).
     *
     * @param id The unique identifier of the account to be deleted.
     * @return True if the account was successfully deleted, otherwise false.
     * @throws EntityNotFoundException If no account with the given ID is found.
     */
    @Override
    public boolean deleteById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Account with id = " + id + " was not found"));
        if (!account.getBankId().equals(1L)) {
            throw new UnsupportedOperationException("Only CleverBank account can be deleted");
        }
        return accountRepository.delete(id);
    }
}
