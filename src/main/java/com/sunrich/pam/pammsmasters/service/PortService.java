package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Berth;
import com.sunrich.pam.pammsmasters.domain.Port;
import com.sunrich.pam.pammsmasters.dto.PortDTO;
import com.sunrich.pam.pammsmasters.exception.PortNotFoundException;
import com.sunrich.pam.pammsmasters.repository.BerthRepository;
import com.sunrich.pam.pammsmasters.repository.PortRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortService {

    @Autowired
    PortRepository portRepository;

    @Autowired
    BerthRepository berthRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the port
     *
     * @param portDTO - domain object
     * @return - the saved/updated port object
     */
    public PortDTO saveOrUpdate(PortDTO portDTO) {
        Port port = new Port();

        if (portDTO.getId() != null) {
            port = findEntityById(portDTO.getId());
        }

        modelMapper.map(portDTO, port);

        port.setRecordStatus(true);
        Port savedPort = portRepository.save(port);
        return modelMapper.map(savedPort, PortDTO.class);
    }

    /**
     * Used to get the list of ports
     *
     * @return - list of ports
     */
    public List<PortDTO> findAll() {
        List<Port> ports = portRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return ports.stream()
                .map(port -> modelMapper.map(port, PortDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the port by id
     *
     * @param id - port identifier
     * @return - Port object
     */
    public PortDTO findById(Long id) {
        Port port = findEntityById(id);
        return modelMapper.map(port, PortDTO.class); // convert to DTO and return
    }

    private Port findEntityById(Long id) {
        Optional<Port> optPort = portRepository.findByIdAndRecordStatusTrue(id);
        if (!optPort.isPresent()) {
            throw new PortNotFoundException("Port not found!");
        }

        Port port = optPort.get(); // set berths of the port
        List<Berth> berths = berthRepository.findAllByPortIdAndRecordStatusTrueOrderByIdDesc(port.getId());
        port.setBerths(berths);
        return port;
    }

    /**
     * Used to logically delete the port by updating recordStatus flag to false
     *
     * @param id - port identifier
     * @return - removed port's identifier
     */
    public Long delete(Long id) {
        Port port = findEntityById(id);
        port.setRecordStatus(false);
        portRepository.save(port);
        return port.getId();
    }
}
