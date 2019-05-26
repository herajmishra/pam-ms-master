package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Commodity;
import com.sunrich.pam.pammsmasters.dto.CommodityDTO;
import com.sunrich.pam.pammsmasters.exception.CommodityNotFoundException;
import com.sunrich.pam.pammsmasters.repository.CommodityRepository;
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
public class CommodityServiceTest {

    @Mock
    CommodityRepository commodityRepository;

    @InjectMocks
    CommodityService commodityService;

    private List<Commodity> commodityList;

    private List<CommodityDTO> commodityDTOList;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        commodityList = Arrays.asList(
                Commodity.builder().id(1L).name("Commodity 1").code("C001").recordStatus(true).build(),
                Commodity.builder().id(2L).name("Commodity 2").code("C002").recordStatus(true).build(),
                Commodity.builder().id(3L).name("Commodity 3").code("C003").recordStatus(true).build()
        );

        commodityDTOList = Arrays.asList(
                CommodityDTO.builder().id(1L).name("Commodity 1").code("C001").build(),
                CommodityDTO.builder().id(2L).name("Commodity 2").code("C002").build(),
                CommodityDTO.builder().id(3L).name("Commodity 3").code("C003").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(commodityRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(commodityList);
        assertThat(commodityService.findAll()).isEqualTo(commodityDTOList);
        verify(commodityRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        when(commodityRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(commodityList.get(0)));
        assertThat(commodityService.findById(1L)).isEqualTo(commodityDTOList.get(0));
        verify(commodityRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test(expected = CommodityNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        commodityService.findById(5L);
        verify(commodityRepository).findByIdAndRecordStatusTrue(5L);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() {
        when(commodityRepository.findByNameAndRecordStatusTrue("Commodity 2")).thenReturn(Optional.ofNullable(commodityList.get(1)));
        assertThat(commodityService.findByName("Commodity 2")).isEqualTo(commodityDTOList.get(1));
        verify(commodityRepository).findByNameAndRecordStatusTrue("Commodity 2");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        Commodity.CommodityBuilder builder = Commodity.builder().id(null).name("Commodity 1").code("C001").recordStatus(false);

        Commodity commodityToSave = builder.recordStatus(true).build(); // input for repo layer save method
        Commodity savedCommodity = builder.id(1L).recordStatus(true).build(); // result of repo layer save method

        CommodityDTO payload = CommodityDTO.builder().name("Commodity 1").code("C001").build();
        CommodityDTO response = CommodityDTO.builder().name("Commodity 1").code("C001").id(1L).build();

        when(commodityRepository.save(commodityToSave)).thenReturn(savedCommodity); // mocking save method of repo layer
        assertThat(commodityService.saveOrUpdate(payload)).isEqualTo(response);
        verify(commodityRepository).save(commodityToSave);
    }

    @Test
    public void update_whenValidObject_shouldUpdateAndReturnUpdatedObject() {
        long id = 1L;
        String updatedName = "Commodity Updated Name";

        Commodity.CommodityBuilder builder = Commodity.builder().id(id).name("Commodity Updated").code("C0010");

        Commodity findByIdResult = builder.recordStatus(true).build();
        Commodity commodityToUpdate = builder.recordStatus(true).name(updatedName).build();

        CommodityDTO payload = CommodityDTO.builder().id(1L).name(updatedName).code("C0010").build();

        when(commodityRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(commodityRepository.save(commodityToUpdate)).thenReturn(commodityToUpdate);

        assertThat(commodityService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(commodityRepository).save(findByIdResult);
        verify(commodityRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test(expected = CommodityNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 5L;
        CommodityDTO commodityToUpdate = CommodityDTO.builder().id(id).build();

        when(commodityRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            commodityService.saveOrUpdate(commodityToUpdate);
        } finally {
            verify(commodityRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        Commodity.CommodityBuilder builder = Commodity.builder().id(id).name("Commodity 1").code("C001");

        Commodity commodity = builder.recordStatus(true).build();
        when(commodityRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(commodity));

        Commodity commodityToDelete = builder.recordStatus(false).build();
        when(commodityRepository.save(commodityToDelete)).then(returnsFirstArg());

        assertThat((Long) id).isEqualTo(commodityService.delete(id));

        verify(commodityRepository).findByIdAndRecordStatusTrue(id);
        verify(commodityRepository).save(commodityToDelete);
    }
}
