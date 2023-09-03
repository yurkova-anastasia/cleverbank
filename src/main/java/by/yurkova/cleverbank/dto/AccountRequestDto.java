package by.yurkova.cleverbank.dto;

/**
 * A data transfer object (DTO) representing a request to create or update an account.
 * This DTO contains information about the account, including its number, currency, user ID, and bank ID.
 * It is used to transfer data between the client and server when creating or updating accounts.
 *
 * @author Yurkova Anastacia
 */
public record AccountRequestDto(
        String number,
        String currency,
        Long userId,
        Long bankId
) {
}
