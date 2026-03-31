package com.forum.booking.service;

import com.forum.booking.dto.CompanyCreateDto;
import com.forum.booking.dto.CompanyDto;
import com.forum.booking.dto.CompanyUpdateDto;
import com.forum.booking.entity.Company;
import com.forum.booking.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
            .map(this::convertToDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Компания не найдена"));
        return convertToDto(company);
    }

    @Transactional
    public CompanyDto createCompany(CompanyCreateDto dto) {
        Company company = Company.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .logoUrl(dto.getLogoUrl())
            .isActive(true)
            .build();

        Company saved = companyRepository.save(company);
        return convertToDto(saved);
    }

    @Transactional
    public CompanyDto updateCompany(Long id, CompanyUpdateDto dto) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Компания не найдена"));

        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setLogoUrl(dto.getLogoUrl());
        if (dto.getIsActive() != null) {
            company.setIsActive(dto.getIsActive());
        }

        Company updated = companyRepository.save(company);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Компания не найдена");
        }
        companyRepository.deleteById(id);
    }

    private CompanyDto convertToDto(Company company) {
        return CompanyDto.builder()
            .id(company.getId())
            .name(company.getName())
            .description(company.getDescription())
            .logoUrl(company.getLogoUrl())
            .isActive(company.getIsActive())
            .createdAt(company.getCreatedAt())
            .build();
    }
}
