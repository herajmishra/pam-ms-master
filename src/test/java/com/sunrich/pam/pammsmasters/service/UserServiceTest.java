package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.User;
import com.sunrich.pam.pammsmasters.dto.UserDTO;
import com.sunrich.pam.pammsmasters.exception.UserNotFoundException;
import com.sunrich.pam.pammsmasters.repository.UserRepository;
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
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    // if modifying this list, respective tests needs to updated as well
    private List<User> userList;

    private List<UserDTO> userDTOList;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        userList = Arrays.asList(
                User.builder().id(1L).userName("john_doe").recordStatus(true).build(),
                User.builder().id(2L).userName("jane_doe").recordStatus(true).build(),
                User.builder().id(3L).userName("jerry_potter").recordStatus(true).build()
        );

        userDTOList = Arrays.asList(
                UserDTO.builder().id(1L).userName("john_doe").build(),
                UserDTO.builder().id(2L).userName("jane_doe").build(),
                UserDTO.builder().id(3L).userName("jerry_potter").build()
        );

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        when(userRepository.findAllByRecordStatusTrueOrderByIdDesc()).thenReturn(userList);

        assertThat(userService.findAll())
                .extracting(UserDTO::getId, UserDTO::getUserName)
                .contains(
                        tuple(1L, "john_doe"),
                        tuple(2L, "jane_doe"),
                        tuple(3L, "jerry_potter")
                );

        verify(userRepository).findAllByRecordStatusTrueOrderByIdDesc();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        when(userRepository.findByIdAndRecordStatusTrue(1L)).thenReturn(Optional.ofNullable(userList.get(0)));
        assertThat(userService.findById(1L)).isEqualTo(userDTOList.get(0));
        verify(userRepository).findByIdAndRecordStatusTrue(1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        userService.findById(5L);
        verify(userRepository).findByIdAndRecordStatusTrue(5L);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() {
        when(userRepository.findByUserNameAndRecordStatusTrue("jane_doe")).thenReturn(Optional.ofNullable(userList.get(1)));
        assertThat(userService.findByName("jane_doe")).isEqualTo(userDTOList.get(1));
        verify(userRepository).findByUserNameAndRecordStatusTrue("jane_doe");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        User.UserBuilder builder = User.builder().id(null).userName("john_doe").recordStatus(false);

        UserDTO payload = UserDTO.builder().userName("john_doe").build();
        UserDTO response = UserDTO.builder().id(1L).userName(payload.getUserName()).build();

        User userToSave = builder.recordStatus(true).build();
        User savedUser = builder.id(1L).recordStatus(true).build();

        when(userRepository.save(userToSave)).thenReturn(savedUser);
        assertThat(userService.saveOrUpdate(payload)).isEqualTo(response);
        verify(userRepository).save(userToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        long id = 1L;
        String updatedName = "john_doe_updated_name";

        UserDTO payload = UserDTO.builder().id(id).userName(updatedName).build();

        User userToUpdate = User.builder().id(id).userName(updatedName).recordStatus(true).build();
        User findByIdResult = User.builder().id(id).userName("john_doe").recordStatus(true).build();

        when(userRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(findByIdResult));
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        assertThat(userService.saveOrUpdate(payload)).isEqualTo(payload);

        verify(userRepository).save(userToUpdate);
        verify(userRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test(expected = UserNotFoundException.class)
    public void update_whenNonExistingObject_shouldThrowNotFoundException() {
        long id = 5L;
        UserDTO userToUpdate = UserDTO.builder().id(id).build();

        when(userRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.empty());
        try {
            userService.saveOrUpdate(userToUpdate);
        } finally {
            verify(userRepository).findByIdAndRecordStatusTrue(id);
        }
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        long id = 1L;

        User.UserBuilder builder = User.builder().id(1L).userName("john_doe");

        User user = builder.recordStatus(true).build();
        User userToDelete = builder.recordStatus(false).build();

        when(userRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(userToDelete)).then(returnsFirstArg());

        assertThat(userService.delete(id)).isEqualTo((Long) id);

        verify(userRepository).save(userToDelete);
        verify(userRepository).findByIdAndRecordStatusTrue(id);
    }
}
