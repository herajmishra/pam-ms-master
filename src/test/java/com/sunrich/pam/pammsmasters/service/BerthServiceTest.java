package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Berth;
import com.sunrich.pam.pammsmasters.domain.Port;
import com.sunrich.pam.pammsmasters.dto.BerthDTO;
import com.sunrich.pam.pammsmasters.exception.BerthNotFoundException;
import com.sunrich.pam.pammsmasters.repository.BerthRepository;
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
public class BerthServiceTest {

    @InjectMocks
    BerthService berthService;

    @Mock
    BerthRepository berthRepository;

    private List<Berth> berthList;

    private List<BerthDTO> berthDTOList;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        berthList = Arrays.asList(
                Berth.builder().id(1L).code("B001").recordStatus(true)
                        .port(Port.builder().id(1L).description("Port 101").recordStatus(true).build())
                        .build(),

                Berth.builder().id(2L).code("B002").recordStatus(true)
                        .port(Port.builder().id(2L).description("Port 102").recordStatus(true).build())
                        .build()
        );

        berthDTOList = Arrays.asList(
                BerthDTO.builder().id(1L).code("B001").build(),
                BerthDTO.builder().id(2L).code("B002").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(berthRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(berthList);
        assertThat(berthService.findAll()).isEqualTo(berthDTOList);
        verify(berthRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        when(berthRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(berthList.get(0)));
        assertThat(berthService.findById(1L)).isEqualTo(berthDTOList.get(0));
        verify(berthRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test(expected = BerthNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        berthService.findById(1L);
        verify(berthRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        Berth.BerthBuilder builder = Berth.builder().id(null).code("B001");

        Berth berthToSave = builder.recordStatus(true).build();
        Berth savedBerth = builder.id(1L).recordStatus(true).build();

        BerthDTO newDto = BerthDTO.builder().code("B001").build();
        BerthDTO savedDto = BerthDTO.builder().id(1L).code("B001").build();

        when(berthRepository.save(berthToSave)).thenReturn(savedBerth);
        assertThat(berthService.saveOrUpdate(newDto)).isEqualTo(savedDto);
        verify(berthRepository).save(berthToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L;
        String updatedCode = "B0020";

        Berth.BerthBuilder builder = Berth.builder().id(id).recordStatus(true).port(Port.builder().id(101L).build());
        Berth findByIdResult = builder.code("B0010").build();
        Berth berthToUpdate = builder.code(updatedCode).build();

        BerthDTO payload = BerthDTO.builder().id(id).code(updatedCode).build();

        when(berthRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(berthRepository.save(berthToUpdate)).thenReturn(berthToUpdate);

        assertThat(berthService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(berthRepository).save(findByIdResult);
        verify(berthRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test(expected = BerthNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 1L;
        BerthDTO berth = BerthDTO.builder().id(id).build();

        when(berthRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            berthService.saveOrUpdate(berth);
        } finally {
            verify(berthRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        Berth.BerthBuilder builder = Berth.builder().id(id).code("B001")
                .port(Port.builder().id(1L).build());

        Berth berth = builder.recordStatus(true).build();
        when(berthRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(berth));

        Berth berthToDelete = builder.recordStatus(false).build();
        when(berthRepository.save(berthToDelete)).then(returnsFirstArg());

        assertThat(berthService.delete(id)).isEqualTo((Long) id);

        verify(berthRepository).findByIdAndRecordStatusTrue(id);
        verify(berthRepository).save(berthToDelete);
    }
}
