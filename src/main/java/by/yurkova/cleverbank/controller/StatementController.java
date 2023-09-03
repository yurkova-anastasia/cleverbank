package by.yurkova.cleverbank.controller;

import by.yurkova.cleverbank.config.AppConfig;
import by.yurkova.cleverbank.dto.AccountResponseDto;
import by.yurkova.cleverbank.dto.TransactionResponseDto;
import by.yurkova.cleverbank.exception.BadRequestException;
import by.yurkova.cleverbank.service.AccountService;
import by.yurkova.cleverbank.service.TransactionService;
import by.yurkova.cleverbank.service.UserService;
import by.yurkova.cleverbank.util.yaml.YMLParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.yurkova.cleverbank.util.statement.PdfStatementGenerator.generatePdfAccountStatement;
import static by.yurkova.cleverbank.util.statement.PdfStatementGenerator.generatePdfMoneyStatement;

/**
 * Controller for Statements with doGet method
 *
 * @author Yurkova Anastacia
 */
@WebServlet("/statements/*")
public class StatementController extends HttpServlet {

    private AccountService accountService;
    private TransactionService transactionService;
    private UserService userService;
    private YMLParser yamlParser;

    @Override
    public void init(ServletConfig config) {
        this.accountService = AppConfig.getAccountService();
        this.transactionService = AppConfig.getTransactionService();
        this.userService = AppConfig.getUserService();
        this.yamlParser = AppConfig.getYamlParser();
    }

    /**
     * Generates financial statements based on the provided URI:
     * - If the URI specifies "account," it generates an account statement for the given account number
     * within the specified date range.
     * - If the URI specifies "money," it generates a money statement for the given account number
     * within the specified date range.
     *
     * @param req  The HttpServletRequest object representing the HTTP request.
     * @param resp The HttpServletResponse object representing the HTTP response.
     * @throws BadRequestException If the HTTP request format is invalid or the account number doesn't match the expected pattern.
     */
    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) {
        validateRequest(req);
        String[] uriElements = req.getRequestURI().split("/");
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        switch (uriElements[2]) {
            case "account":
                createAccountStatement(
                        uriElements[3],
                        LocalDate.parse(req.getParameter("from"), formatter),
                        LocalDate.parse(req.getParameter("to"), formatter));
            case "money":
                createMoneyStatement(
                        uriElements[3],
                        LocalDate.parse(req.getParameter("from"), formatter),
                        LocalDate.parse(req.getParameter("to"), formatter));
        }
    }

    /**
     * Generates an account statement in PDF format for the specified account number within the given date range.
     *
     * @param accountNumber The account number for which the statement is generated.
     * @param from          The start date of the statement period.
     * @param to            The end date of the statement period.
     */
    private void createAccountStatement(String accountNumber, LocalDate from, LocalDate to) {
        var account = accountService.findByNumber(accountNumber);
        if (!account.bankId().equals(1L)) {
            throw new UnsupportedOperationException("Only CleverBank account can receive statements");
        }
        var transactions = transactionService.findAllForPeriod(accountNumber, from, to);
        var userDto = userService.findByAccountId(account.id());
        generatePdfAccountStatement(userDto, transactions, account, from, to);
    }

    /**
     * Generates a money statement in PDF format for the specified account number within the given date range.
     * The money statement includes income and outcome calculations.
     *
     * @param accountNumber The account number for which the statement is generated.
     * @param from          The start date of the statement period.
     * @param to            The end date of the statement period.
     */
    private void createMoneyStatement(String accountNumber, LocalDate from, LocalDate to) {
        var account = accountService.findByNumber(accountNumber);
        if (!account.bankId().equals(1L)) {
            throw new UnsupportedOperationException("Only CleverBank account can receive statements");
        }
        var transactions = transactionService.findAllForPeriod(accountNumber, from, to)
                .stream()
                .collect(Collectors.groupingBy(TransactionResponseDto::type));
        var user = userService.findByAccountId(account.id());
        var income = calculateIncome(transactions, account);
        var outcome = calculateOutcome(transactions, account);
        generatePdfMoneyStatement(user, account, income, outcome, from, to);
    }

    /**
     * Calculates the income portion of the money statement based on transaction data.
     *
     * @param transactions The list of transactions for the account.
     * @param account      The account for which the statement is generated.
     * @return The calculated income amount.
     */
    private BigDecimal calculateIncome(
            Map<String, List<TransactionResponseDto>> transactions,
            AccountResponseDto account
    ) {
        Map<String, BigDecimal> exchangeRates = yamlParser.getYaml().getExchangeRates();
        BigDecimal incomeFromRefills = BigDecimal.ZERO;
        BigDecimal incomeFromTransfers = BigDecimal.ZERO;
        if (transactions.get("REFILL") != null) {
            incomeFromRefills = transactions.get("REFILL")
                    .stream()
                    .map(TransactionResponseDto::amount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
        if (transactions.get("TRANSFER") != null) {
            incomeFromTransfers = transactions.get("TRANSFER")
                    .stream()
                    .filter(tr -> tr.receiverAccountId().equals(account.id()))
                    .map(tr -> tr.amount().multiply(exchangeRates.get(tr.currency() + account.currency())))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
        return incomeFromRefills.add(incomeFromTransfers);
    }

    /**
     * Calculates the outcome portion of the money statement based on transaction data.
     *
     * @param transactions The list of transactions for the account.
     * @param account      The account for which the statement is generated.
     * @return The calculated outcome amount.
     */
    private BigDecimal calculateOutcome(
            Map<String, List<TransactionResponseDto>> transactions,
            AccountResponseDto account
    ) {
        BigDecimal outcomeFromWithdraws = BigDecimal.ZERO;
        BigDecimal outcomeFromTransfers = BigDecimal.ZERO;
        if (transactions.get("WITHDRAW") != null) {
            outcomeFromWithdraws = transactions.get("WITHDRAW")
                    .stream()
                    .map(TransactionResponseDto::amount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
        if (transactions.get("TRANSFER") != null) {
            outcomeFromTransfers = transactions.get("TRANSFER")
                    .stream()
                    .filter(tr -> tr.senderAccountId().equals(account.id()))
                    .map(TransactionResponseDto::amount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
        return outcomeFromWithdraws.add(outcomeFromTransfers);
    }

    /**
     * Validates the HTTP request to ensure it follows the expected format.
     *
     * @param request The HttpServletRequest object representing the HTTP request.
     * @throws BadRequestException If the HTTP request format is invalid or the account number doesn't match the expected pattern.
     */
    private void validateRequest(HttpServletRequest request) {
        String[] parts = request.getRequestURI().split("/");
        if (parts.length != 4) {
            throw new BadRequestException("Invalid HTTP request format");
        }

        if (parts[3].length() != 16) {
            throw new BadRequestException("Account number must be exactly 16 characters long");
        }

        if (!parts[3].matches("[0-9]{16}$")) {
            throw new BadRequestException("Account number must contain 16 numbers");
        }
    }
}
