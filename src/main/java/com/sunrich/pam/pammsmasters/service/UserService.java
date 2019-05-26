package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.User;
import com.sunrich.pam.pammsmasters.domain.UserOrg;
import com.sunrich.pam.pammsmasters.dto.UserDTO;
import com.sunrich.pam.pammsmasters.exception.UserNotFoundException;
import com.sunrich.pam.pammsmasters.repository.UserOrgRepository;
import com.sunrich.pam.pammsmasters.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserOrgRepository userOrgRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the user
     *
     * @param userDTO - domain object
     * @return - the saved/updated user
     */
    @Transactional
    public UserDTO saveOrUpdate(UserDTO userDTO) {
        User user = new User();

        if (userDTO.getId() != null) {
            user = findEntityById(userDTO.getId());
        }

        modelMapper.map(userDTO, user);

        user.setRecordStatus(true);
        User savedUser = userRepository.save(user);

        UserOrg userOrg = UserOrg.builder().userId(savedUser.getId()).orgId(userDTO.getOrgId()).build();
        userOrgRepository.save(userOrg);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    /**
     * Used to get the list of users
     *
     * @return - the list of users
     */
    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the user by id
     *
     * @param id - user identifier
     * @return - user object
     */
    public UserDTO findById(Long id) {
        User user = findEntityById(id);
        return modelMapper.map(user, UserDTO.class);
    }

    private User findEntityById(Long id) {
        Optional<User> optUser = userRepository.findByIdAndRecordStatusTrue(id);
        if (!optUser.isPresent()) {
            throw new UserNotFoundException("User Not Found!");
        }
        return optUser.get();
    }

    /**
     * Used to get the user by userName
     *
     * @param userName - user userName
     * @return - user object
     */
    public UserDTO findByName(String userName) {
        Optional<User> optUser = userRepository.findByUserNameAndRecordStatusTrue(userName);
        if (!optUser.isPresent()) {
            throw new UserNotFoundException("User Not Found!");
        }
        return modelMapper.map(optUser.get(), UserDTO.class);
    }

    /**
     * Used to logically delete the user by updating recordStatus flag to false
     *
     * @param id - user identifier
     * @return - removed user identifier
     */
    public Long delete(Long id) {
        User user = findEntityById(id);
        user.setRecordStatus(false);
        userRepository.save(user);
        return user.getId();
    }
}
