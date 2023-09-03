package by.yurkova.cleverbank.config;

import by.yurkova.cleverbank.repository.AccountRepository;
import by.yurkova.cleverbank.repository.BankRepository;
import by.yurkova.cleverbank.repository.TransactionRepository;
import by.yurkova.cleverbank.repository.UserRepository;
import by.yurkova.cleverbank.service.AccountService;
import by.yurkova.cleverbank.service.BankService;
import by.yurkova.cleverbank.service.TransactionService;
import by.yurkova.cleverbank.service.UserService;
import by.yurkova.cleverbank.service.impl.AccountServiceImpl;
import by.yurkova.cleverbank.service.impl.BankServiceImpl;
import by.yurkova.cleverbank.service.impl.TransactionServiceImpl;
import by.yurkova.cleverbank.service.impl.UserServiceImpl;
import by.yurkova.cleverbank.util.yaml.YMLParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * Configuration class responsible for initializing various components of the application.
 * This class sets up the data source, repositories, services, and other necessary objects used throughout the application.
 * It also provides access to commonly used instances such as the YAML parser and Jackson ObjectMapper.
 * All components are initialized as static fields for easy access throughout the application.
 *
 * @author Yurkova Anastacia
 */
@UtilityClass
public class AppConfig {

    private static final YMLParser yamlParser;
    private static final DataSource dataSource;
    private static final AccountRepository accountRepository;
    private static final BankRepository bankRepository;
    private static final TransactionRepository transactionRepository;
    private static final UserRepository userRepository;
    private static final AccountService accountService;
    private static final BankService bankService;
    private static final TransactionService transactionService;
    private static final UserService userService;
    private static final ObjectMapper objectMapper;

    static {
        yamlParser = new YMLParser();

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(yamlParser.getYaml().getPostgres().getUrl());
        pgSimpleDataSource.setUser(yamlParser.getYaml().getPostgres().getUser());
        pgSimpleDataSource.setPassword(yamlParser.getYaml().getPostgres().getPassword());
        dataSource = pgSimpleDataSource;

        accountRepository = new AccountRepository(dataSource);
        bankRepository = new BankRepository(dataSource);
        transactionRepository = new TransactionRepository(dataSource);
        userRepository = new UserRepository(dataSource);

        accountService = new AccountServiceImpl(accountRepository, transactionRepository);
        bankService = new BankServiceImpl(bankRepository);
        transactionService = new TransactionServiceImpl(transactionRepository);
        userService = new UserServiceImpl(userRepository);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static YMLParser getYamlParser() {
        return yamlParser;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    public static BankService getBankService() {
        return bankService;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
