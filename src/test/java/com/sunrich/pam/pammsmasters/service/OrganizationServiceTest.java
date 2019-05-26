package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Branch;
import com.sunrich.pam.pammsmasters.domain.Organization;
import com.sunrich.pam.pammsmasters.dto.OrganizationDTO;
import com.sunrich.pam.pammsmasters.exception.OrganizationNotFoundException;
import com.sunrich.pam.pammsmasters.repository.OrganizationRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrganizationServiceTest {

    @InjectMocks
    OrganizationService organizationService;

    @Mock
    OrganizationRepository organizationRepository;

    private List<Organization> orgList;
    private List<OrganizationDTO> orgDTOList;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        orgList = Arrays.asList(
                Organization.builder().id(1L).name("Organization 1").recordStatus(true)
                        .branches(
                                Arrays.asList(
                                        Branch.builder().id(1L).name("Branch 1").recordStatus(true).build(),
                                        Branch.builder().id(2L).name("Branch 2").recordStatus(true).build(),
                                        Branch.builder().id(3L).name("Branch 3").recordStatus(true).build()
                                )
                        )
                        .build(),
                Organization.builder().id(2L).name("Organization 2").recordStatus(true).build()
        );

        orgDTOList = Arrays.asList(
                OrganizationDTO.builder().id(1L).name("Organization 1").build(),
                OrganizationDTO.builder().id(2L).name("Organization 2").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(organizationRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(orgList);
        assertThat(organizationService.findAll()).isEqualTo(orgDTOList);
        verify(organizationRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        when(organizationRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(orgList.get(0)));
        assertThat(organizationService.findById(1L)).isEqualTo(orgDTOList.get(0));
        verify(organizationRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test(expected = OrganizationNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        organizationService.findById(5L);
        verify(organizationRepository).findByIdAndRecordStatusTrue(5L);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() {
        when(organizationRepository.findByNameAndRecordStatusTrue("Organization 2")).thenReturn(Optional.ofNullable(orgList.get(1)));
        assertThat(organizationService.findByName("Organization 2")).isEqualTo(orgDTOList.get(1));
        verify(organizationRepository).findByNameAndRecordStatusTrue("Organization 2");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        String name = "Organization 1";
        Organization.OrganizationBuilder builder = Organization.builder().id(null).name(name).recordStatus(false);

        Organization orgToSave = builder.recordStatus(true).build();
        Organization savedOrg = builder.recordStatus(true).id(1L).build();

        OrganizationDTO payload = OrganizationDTO.builder().name(name).build();
        OrganizationDTO response = OrganizationDTO.builder().name(name).id(1L).build();

        when(organizationRepository.save(orgToSave)).thenReturn(savedOrg);
        assertThat(organizationService.saveOrUpdate(payload)).isEqualTo(response);
        verify(organizationRepository).save(orgToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L;
        String updatedName = "Organization Name Updated";

        Organization.OrganizationBuilder builder = Organization.builder().id(id).name("Organization 1");

        OrganizationDTO payload = OrganizationDTO.builder().id(id).name(updatedName).build();
        Organization findByIdResult = builder.recordStatus(true).build();
        Organization orgToUpdate = builder.name(updatedName).recordStatus(true).build();

        when(organizationRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(organizationRepository.save(orgToUpdate)).thenReturn(orgToUpdate);

        assertThat(organizationService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(organizationRepository).save(orgToUpdate);
        verify(organizationRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test(expected = OrganizationNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 1L;
        OrganizationDTO organization = OrganizationDTO.builder().id(id).build();

        when(organizationRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            organizationService.saveOrUpdate(organization);
        } finally {
            verify(organizationRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;
        Organization.OrganizationBuilder builder = Organization.builder().id(id).name("Organization 1");

        Organization org = builder.recordStatus(true).build();
        when(organizationRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(org));

        Organization orgToDelete = builder.recordStatus(false).build();
        when(organizationRepository.save(orgToDelete)).then(returnsFirstArg());

        assertThat(organizationService.delete(id)).isEqualTo((Long) id);

        verify(organizationRepository).findByIdAndRecordStatusTrue(id);
        verify(organizationRepository).save(orgToDelete);
    }
}
