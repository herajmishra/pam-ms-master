package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Berth;
import com.sunrich.pam.pammsmasters.domain.Port;
import com.sunrich.pam.pammsmasters.dto.BerthDTO;
import com.sunrich.pam.pammsmasters.dto.PortDTO;
import com.sunrich.pam.pammsmasters.exception.PortNotFoundException;
import com.sunrich.pam.pammsmasters.repository.BerthRepository;
import com.sunrich.pam.pammsmasters.repository.PortRepository;
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
public class PortServiceTest {

    @InjectMocks
    PortService portService;

    @Mock
    PortRepository portRepository;

    @Mock
    BerthRepository berthRepository;

    private List<Port> portList;

    private List<PortDTO> portDTOList;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        portList = Arrays.asList(
                Port.builder().id(1L).description("Port 101").recordStatus(true).countryId(1L).berths(
                        Arrays.asList(
                                Berth.builder().id(3L).code("B003").recordStatus(true).build(),
                                Berth.builder().id(2L).code("B002").recordStatus(true).build(),
                                Berth.builder().id(1L).code("B001").recordStatus(true).build()
                        )
                ).build(),
                Port.builder().id(2L).description("Port 102").recordStatus(true).countryId(1L).berths(Collections.emptyList()).build(),
                Port.builder().id(3L).description("Port 103").recordStatus(true).countryId(1L).berths(Collections.emptyList()).build()
        );

        portDTOList = Arrays.asList(
                PortDTO.builder().id(1L).description("Port 101").countryId(1L).berths(
                        Arrays.asList(
                                BerthDTO.builder().id(3L).code("B003").build(),
                                BerthDTO.builder().id(2L).code("B002").build(),
                                BerthDTO.builder().id(1L).code("B001").build()
                        )
                ).build(),
                PortDTO.builder().id(2L).description("Port 102").countryId(1L).berths(Collections.emptyList()).build(),
                PortDTO.builder().id(3L).description("Port 103").countryId(1L).berths(Collections.emptyList()).build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(portRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(portList);
        assertThat(portService.findAll()).isEqualTo(portDTOList);
        verify(portRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        long portId = 1L;
        when(portRepository.findByIdAndRecordStatusTrue(portId)).thenReturn(Optional.ofNullable(portList.get(0)));
        when(berthRepository.findAllByPortIdAndRecordStatusTrueOrderByIdDesc(portId)).thenReturn(portList.get(0).getBerths());
        assertThat(portService.findById(portId)).isEqualTo(portDTOList.get(0));
        verify(portRepository).findByIdAndRecordStatusTrue(portId);
    }

    @Test(expected = PortNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        portService.findById(1L);
        verify(portRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        Long countryId = 105L;
        String description = "Port 101";
        Port.PortBuilder builder = Port.builder().id(null).description(description).countryId(1L).recordStatus(false);

        PortDTO payload = PortDTO.builder().description(description).countryId(countryId).build();
        PortDTO savedDto = PortDTO.builder().description(description).countryId(countryId).id(1L).build();

        Port portToSave = builder.recordStatus(true).countryId(countryId).build();
        Port savedPort = builder.recordStatus(true).countryId(countryId).id(1L).build();

        when(portRepository.save(portToSave)).thenReturn(savedPort);
        assertThat(portService.saveOrUpdate(payload)).isEqualTo(savedDto);
        verify(portRepository).save(portToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L, countryId = 105L;

        Port.PortBuilder builder = Port.builder().id(id).description("Port 101").recordStatus(false)
                .berths(null);

        String updatedDescription = "Port Updated Description";
        PortDTO payload = PortDTO.builder().id(id).description(updatedDescription).countryId(countryId).build();

        Port portToUpdate = builder.description(updatedDescription).recordStatus(true).countryId(countryId).build();
        Port findByIdResult = builder.recordStatus(true).countryId(countryId).build();

        when(berthRepository.findAllByPortIdAndRecordStatusTrueOrderByIdDesc(id)).thenReturn(portList.get(0).getBerths());
        when(portRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(portRepository.save(portToUpdate)).thenReturn(portToUpdate);

        assertThat(portService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(portRepository).findByIdAndRecordStatusTrue(id);
        verify(portRepository).save(portToUpdate);
    }

    @Test(expected = PortNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 1L;
        PortDTO port = PortDTO.builder().id(id).countryId(1L).build();

        when(portRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            portService.saveOrUpdate(port);
        } finally {
            verify(portRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        Port.PortBuilder builder = Port.builder().id(id).description("Port 101").berths(Collections.emptyList());

        Port port = builder.recordStatus(true).build();
        when(portRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(port));

        Port portToDelete = builder.recordStatus(false).build();
        when(portRepository.save(portToDelete)).then(returnsFirstArg());

        assertThat((Long) id).isEqualTo(portService.delete(id));

        verify(portRepository).findByIdAndRecordStatusTrue(id);
        verify(portRepository).save(portToDelete);
    }

}
