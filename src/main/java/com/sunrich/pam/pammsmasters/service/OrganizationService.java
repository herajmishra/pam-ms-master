package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Organization;
import com.sunrich.pam.pammsmasters.dto.OrganizationDTO;
import com.sunrich.pam.pammsmasters.exception.OrganizationNotFoundException;
import com.sunrich.pam.pammsmasters.repository.OrganizationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the organization
     * @param organizationDTO - domain object
     * @return - the saved/updated organization
     */
    public OrganizationDTO saveOrUpdate(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();

        if (organizationDTO.getId() != null) {
            organization = findEntityById(organizationDTO.getId());
        }

        modelMapper.map(organizationDTO, organization);

        organization.setRecordStatus(true);
        Organization savedOrg = organizationRepository.save(organization);
        return modelMapper.map(savedOrg, OrganizationDTO.class);
    }

    /**
     * Used to get the list of organizations
     * @return - the list of organizations
     */
    public List<OrganizationDTO> findAll() {
        List<Organization> orgList = organizationRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return orgList.stream()
                .map(org -> modelMapper.map(org, OrganizationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the organization by id
     * @param id - organization identifier
     * @return - organization object
     */
    public OrganizationDTO findById(Long id) {
        Organization org = findEntityById(id);
        return modelMapper.map(org, OrganizationDTO.class);
    }

    private Organization findEntityById(Long id) {
        Optional<Organization> optOrg = organizationRepository.findByIdAndRecordStatusTrue(id);
        if (!optOrg.isPresent()) {
            throw new OrganizationNotFoundException("Organization not found!");
        }
        return optOrg.get();
    }

    /**
     * Used to get the organization by name
     * @param name - organization name
     * @return - organization object
     */
    public OrganizationDTO findByName(String name) {
        Optional<Organization> optOrganization = organizationRepository.findByNameAndRecordStatusTrue(name);
        if (!optOrganization.isPresent()) {
            throw new OrganizationNotFoundException("Organization Not Found!");
        }
        return modelMapper.map(optOrganization.get(), OrganizationDTO.class);
    }

    /**
     * Used to logically delete the organization by updating recordStatus flag to false
     * @param id - organization identifier
     * @return - removed organization identifier
     */
    public Long delete(Long id) {
        Organization org = findEntityById(id);
        org.setRecordStatus(false);
        organizationRepository.save(org);
        return org.getId();
    }
}
