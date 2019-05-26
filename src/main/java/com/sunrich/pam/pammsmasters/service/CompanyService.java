package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Company;
import com.sunrich.pam.pammsmasters.dto.CompanyDTO;
import com.sunrich.pam.pammsmasters.exception.CompanyNotFoundException;
import com.sunrich.pam.pammsmasters.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the company
     * @param companyDTO - domain object
     * @return - the saved/updated company object
     */
    public CompanyDTO saveOrUpdate(CompanyDTO companyDTO) {
        Company company = new Company();

        if (companyDTO.getId() != null) {
            company = findEntityById(companyDTO.getId());
        }

        modelMapper.map(companyDTO, company);

        company.setRecordStatus(true);
        Company savedCompany = companyRepository.save(company);
        return modelMapper.map(savedCompany, CompanyDTO.class);
    }

    /**
     * Used to get the list of companies
     * @return - list of companies
     */
    public List<CompanyDTO> findAll() {
        List<Company> companyList = companyRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return companyList.stream()
                .map(company -> modelMapper.map(company, CompanyDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the company by id
     * @param id - company identifier
     * @return - company object
     */
    public CompanyDTO findById(Long id) {
        Optional<Company> optCompany = companyRepository.findByIdAndRecordStatusTrue(id);
        if (!optCompany.isPresent()) {
            throw new CompanyNotFoundException("Company not found!");
        }
        return modelMapper.map(optCompany.get(), CompanyDTO.class);
    }

    /**
     * Used to get the company entity by id
     *
     * @param id - company identifier
     * @return - company object
     */
    private Company findEntityById(Long id) {
        Optional<Company> optCompany = companyRepository.findByIdAndRecordStatusTrue(id);
        if (!optCompany.isPresent()) {
            throw new CompanyNotFoundException("Company not found!");
        }
        return optCompany.get();
    }

    /**
     * Used to get the company by name
     * @param name - company name
     * @return - company object
     */
    public CompanyDTO findByName(String name) {
        Optional<Company> optCompany = companyRepository.findByNameAndRecordStatusTrue(name);
        if (!optCompany.isPresent()) {
            throw new CompanyNotFoundException("Company not found!");
        }
        return modelMapper.map(optCompany.get(), CompanyDTO.class);
    }

    /**
     * Used to logically delete the company by updating recordStatus flag to false
     * @param id - company identifier
     * @return - removed company identifier
     */
    public Long delete(Long id) {
        Company existingCompany = findEntityById(id);
        existingCompany.setRecordStatus(false);
        Company deletedCompany = companyRepository.save(existingCompany);
        return deletedCompany.getId();
    }
}
