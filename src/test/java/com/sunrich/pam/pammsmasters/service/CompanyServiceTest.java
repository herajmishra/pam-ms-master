package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Company;
import com.sunrich.pam.pammsmasters.domain.Organization;
import com.sunrich.pam.pammsmasters.dto.CompanyDTO;
import com.sunrich.pam.pammsmasters.exception.CompanyNotFoundException;
import com.sunrich.pam.pammsmasters.repository.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    private List<Company> companyList;
    private List<CompanyDTO> companyDTOList;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        companyList = Arrays.asList(
                Company.builder().id(1L).name("Company 1").recordStatus(true).organizations(
                        Arrays.asList(
                                Organization.builder().id(1L).name("Organization 1").recordStatus(true).build(),
                                Organization.builder().id(2L).name("Organization 2").recordStatus(true).build()
                        )
                ).build(),

                Company.builder().id(2L).name("Company 2").recordStatus(true).organizations(Collections.emptyList()).build(),
                Company.builder().id(3L).name("Company 3").recordStatus(true).organizations(Collections.emptyList()).build()
        );

        companyDTOList = Arrays.asList(
                CompanyDTO.builder().id(1L).name("Company 1").build(),
                CompanyDTO.builder().id(2L).name("Company 2").build(),
                CompanyDTO.builder().id(3L).name("Company 3").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() throws Exception {
        when(companyRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(companyList);
        assertThat(companyService.findAll()).isEqualTo(companyDTOList);
        verify(companyRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() throws Exception {
        when(companyRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(companyList.get(0)));
        assertThat(companyService.findById(1L)).isEqualTo(companyDTOList.get(0));
        verify(companyRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test(expected = CompanyNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() throws Exception {
        companyService.findById(5L);
        verify(companyRepository).findByIdAndRecordStatusTrue(5L);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() throws Exception {
        when(companyRepository.findByNameAndRecordStatusTrue("Company 2")).thenReturn(Optional.ofNullable(companyList.get(1)));
        assertThat(companyService.findByName("Company 2")).isEqualTo(companyDTOList.get(1));
        verify(companyRepository).findByNameAndRecordStatusTrue("Company 2");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        Company.CompanyBuilder builder = Company.builder().id(null).name("Company 1").recordStatus(false);

        Company companyToSave = builder.recordStatus(true).build(); // input for repo layer save method
        Company savedCompany = builder.id(1L).recordStatus(true).build(); // result of repo layer save method

        CompanyDTO newDto = CompanyDTO.builder().name("Company 1").build();
        CompanyDTO savedDto = CompanyDTO.builder().id(1L).name("Company 1").build();

        when(companyRepository.save(companyToSave)).thenReturn(savedCompany);
        assertThat(companyService.saveOrUpdate(newDto)).isEqualTo(savedDto);
        verify(companyRepository).save(companyToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L;
        String updatedName = "Company Name Updated";

        Company.CompanyBuilder builder = Company.builder().id(id).name("Company 1");

        CompanyDTO payload = CompanyDTO.builder().id(1L).name(updatedName).build();
        Company findByIdResult = builder.recordStatus(true).build();
        Company companyToUpdate = builder.name(updatedName).recordStatus(true).build();

        when(companyRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(companyRepository.save(companyToUpdate)).thenReturn(companyToUpdate);

        assertThat(companyService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(companyRepository).findByIdAndRecordStatusTrue(id);
        verify(companyRepository).save(companyToUpdate);
    }

    @Test(expected = CompanyNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() throws Exception {
        long id = 1L;
        CompanyDTO companyDTO = CompanyDTO.builder().id(id).build();

        when(companyRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            companyService.saveOrUpdate(companyDTO);
        } finally {
            verify(companyRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        Company.CompanyBuilder builder = Company.builder().id(id).name("Company 1");

        Company company = builder.recordStatus(true).build();
        Company companyToDelete = builder.recordStatus(false).build();

        when(companyRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(company));
        when(companyRepository.save(companyToDelete)).then(returnsFirstArg());

        assertThat(companyService.delete(id)).isEqualTo((Long) id);

        verify(companyRepository).findByIdAndRecordStatusTrue(id);
        verify(companyRepository).save(companyToDelete);
    }
}
