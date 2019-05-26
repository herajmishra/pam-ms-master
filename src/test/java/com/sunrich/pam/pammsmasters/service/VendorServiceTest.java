package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Vendor;
import com.sunrich.pam.pammsmasters.dto.VendorDTO;
import com.sunrich.pam.pammsmasters.exception.VendorNotFoundException;
import com.sunrich.pam.pammsmasters.repository.VendorRepository;
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
public class VendorServiceTest {

    @Mock
    VendorRepository vendorRepository;

    @InjectMocks
    VendorService vendorService;

    private List<Vendor> vendorList;
    private List<VendorDTO> vendorDTOList;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        vendorList = Arrays.asList(
                Vendor.builder().id(1L).name("Vendor 1").code("V001").recordStatus(true).build(),
                Vendor.builder().id(2L).name("Vendor 2").code("V002").recordStatus(true).build(),
                Vendor.builder().id(3L).name("Vendor 3").code("V003").recordStatus(true).build()
        );

        vendorDTOList = Arrays.asList(
                VendorDTO.builder().id(1L).name("Vendor 1").code("V001").build(),
                VendorDTO.builder().id(2L).name("Vendor 2").code("V002").build(),
                VendorDTO.builder().id(3L).name("Vendor 3").code("V003").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(vendorRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(vendorList);
        assertThat(vendorService.findAll()).isEqualTo(vendorDTOList);
        verify(vendorRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        when(vendorRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(vendorList.get(0)));
        assertThat(vendorService.findById(1L)).isEqualTo(vendorDTOList.get(0));
        verify(vendorRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() {
        when(vendorRepository.findByNameAndRecordStatusTrue("Vendor 2")).thenReturn(Optional.ofNullable(vendorList.get(1)));
        assertThat(vendorService.findByName("Vendor 2")).isEqualTo(vendorDTOList.get(1));
        verify(vendorRepository).findByNameAndRecordStatusTrue("Vendor 2");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {

        VendorDTO payload = VendorDTO.builder().name("Vendor 1").code("V001").build();
        VendorDTO response = VendorDTO.builder().id(1L).name("Vendor 1").code("V001").build();

        Vendor.VendorBuilder builder = Vendor.builder().id(null).name("Vendor 1").code("V001").recordStatus(false);

        Vendor vendorToSave = builder.recordStatus(true).build();
        Vendor savedVendor = builder.id(1L).recordStatus(true).build();

        when(vendorRepository.save(vendorToSave)).thenReturn(savedVendor);
        assertThat(vendorService.saveOrUpdate(payload)).isEqualTo(response);
        verify(vendorRepository).save(vendorToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L;
        String updatedName = "Vendor 1 Updated Name", updatedCode = "V0100";

        VendorDTO payload = VendorDTO.builder().id(id).name(updatedName).code(updatedCode).build();

        Vendor.VendorBuilder builder = Vendor.builder().id(id).name("Vendor 1").code("V001").recordStatus(true);
        Vendor findByIdResult = builder.build();
        Vendor userToUpdate = builder.name(updatedName).code(updatedCode).build();

        when(vendorRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(vendorRepository.save(userToUpdate)).thenReturn(userToUpdate);

        assertThat(vendorService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(vendorRepository).findByIdAndRecordStatusTrue(id);
        verify(vendorRepository).save(userToUpdate);
    }

    @Test(expected = VendorNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 5L;
        VendorDTO vendorToUpdate = VendorDTO.builder().id(id).build();

        when(vendorRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            vendorService.saveOrUpdate(vendorToUpdate);
        } finally {
            verify(vendorRepository).findByIdAndRecordStatusTrue(id);
        }
    }


    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        Vendor.VendorBuilder builder = Vendor.builder().id(id).name("Vendor 1").code("V001");

        Vendor vendor = builder.recordStatus(true).build();
        when(vendorRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(vendor));

        Vendor vendorToDelete = builder.recordStatus(false).build();
        when(vendorRepository.save(vendorToDelete)).then(returnsFirstArg());

        assertThat((Long) id).isEqualTo(vendorService.delete(id));

        verify(vendorRepository).findByIdAndRecordStatusTrue(id);
        verify(vendorRepository).save(vendorToDelete);
    }

    @Test(expected = VendorNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        vendorService.findById(5L);
        verify(vendorRepository).findByIdAndRecordStatusTrue(5L);
    }
}
