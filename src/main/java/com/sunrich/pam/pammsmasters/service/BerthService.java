package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Berth;
import com.sunrich.pam.pammsmasters.dto.BerthDTO;
import com.sunrich.pam.pammsmasters.exception.BerthNotFoundException;
import com.sunrich.pam.pammsmasters.repository.BerthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BerthService {

    @Autowired
    BerthRepository berthRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the berth
     *
     * @param berthDTO - domain object
     * @return - the saved/updated berth object
     */
    public BerthDTO saveOrUpdate(BerthDTO berthDTO) {
        Berth berth = new Berth();

        if (berthDTO.getId() != null) {
            berth = findEntityById(berthDTO.getId());
        }

        modelMapper.map(berthDTO, berth);

        berth.setRecordStatus(true);
        Berth savedBerth = berthRepository.save(berth);
        return modelMapper.map(savedBerth, BerthDTO.class);
    }

    /**
     * Used to get the berth by id
     *
     * @param id - berth identifier
     * @return - Berth object
     */
    public BerthDTO findById(Long id) {
        Berth berth = findEntityById(id);
        return modelMapper.map(berth, BerthDTO.class);
    }

    /**
     * Used to get the berth by id
     *
     * @param id - berth identifier
     * @return - Berth object
     */
    private Berth findEntityById(Long id) {
        Optional<Berth> optBerth = berthRepository.findByIdAndRecordStatusTrue(id);
        if (!optBerth.isPresent()) {
            throw new BerthNotFoundException("Berth not found!");
        }
        return optBerth.get();
    }

    public List<BerthDTO> findAll() {
        List<Berth> berths = berthRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return berths.stream()
                .map(berth -> modelMapper.map(berth, BerthDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to logically delete the berth by updating recordStatus flag to false
     *
     * @param id - berth identifier
     * @return - removed berth's identifier
     */
    public Long delete(Long id) {
        Berth berth = findEntityById(id);
        berth.setRecordStatus(false);
        berthRepository.save(berth);
        return berth.getId();
    }
}
