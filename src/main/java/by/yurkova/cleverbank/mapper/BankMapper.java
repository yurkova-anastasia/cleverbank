package by.yurkova.cleverbank.mapper;

import by.yurkova.cleverbank.dto.BankRequestDto;
import by.yurkova.cleverbank.dto.BankResponseDto;
import by.yurkova.cleverbank.model.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface responsible for mapping between Bank entities and their corresponding DTOs.
 * It provides methods for converting Bank objects to BankResponseDto objects and vice versa.
 * Additionally, it supports mapping lists of Bank entities to lists of BankResponseDto objects.
 *
 * @author Yurkova Anastacia
 * @see Bank
 * @see BankResponseDto
 */
@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BankMapper {

    BankResponseDto toDto(Bank bank);

    Bank fromDto(BankRequestDto dto);

    List<BankResponseDto> toListOfDto(List<Bank> banks);
}
