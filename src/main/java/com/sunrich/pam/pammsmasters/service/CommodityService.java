package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Commodity;
import com.sunrich.pam.pammsmasters.dto.CommodityDTO;
import com.sunrich.pam.pammsmasters.exception.CommodityNotFoundException;
import com.sunrich.pam.pammsmasters.repository.CommodityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommodityService {

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the commodity
     *
     * @param commodityDTO - domain object
     * @return - the saved/updated commodity
     */
    public CommodityDTO saveOrUpdate(CommodityDTO commodityDTO) {
        Commodity commodity = new Commodity();

        if (commodityDTO.getId() != null) {
            commodity = findEntityById(commodityDTO.getId());
        }

        modelMapper.map(commodityDTO, commodity);

        commodity.setRecordStatus(true);
        Commodity savedCommodity = commodityRepository.save(commodity);
        return modelMapper.map(savedCommodity, CommodityDTO.class);
    }

    private Commodity findEntityById(Long id) {
        Optional<Commodity> optCommodity = commodityRepository.findByIdAndRecordStatusTrue(id);
        if (!optCommodity.isPresent()) {
            throw new CommodityNotFoundException("Commodity not found!");
        }
        return optCommodity.get();
    }

    /**
     * Used to get the list of commodity
     *
     * @return - the list of commodity
     */
    public List<CommodityDTO> findAll() {
        List<Commodity> commodityList = commodityRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return commodityList.stream()
                .map(commodity -> modelMapper.map(commodity, CommodityDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the commodity by id
     *
     * @param id - commodity identifier
     * @return - commodity object
     */
    public CommodityDTO findById(Long id) {
        Commodity commodity = findEntityById(id);
        return modelMapper.map(commodity, CommodityDTO.class);
    }

    /**
     * Used to get the commodity by name
     *
     * @param name - commodity name
     * @return - commodity object
     */
    public CommodityDTO findByName(String name) {
        Optional<Commodity> optCommodity = commodityRepository.findByNameAndRecordStatusTrue(name);
        if (!optCommodity.isPresent()) {
            throw new CommodityNotFoundException("Commodity Not Found!");
        }
        return modelMapper.map(optCommodity.get(), CommodityDTO.class);
    }

    /**
     * Used to logically delete the commodity by updating recordStatus flag to false
     *
     * @param id - commodity identifier
     * @return - removed commodity identifier
     */
    public Long delete(Long id) {
        Commodity commodity = findEntityById(id);
        commodity.setRecordStatus(false);
        Commodity updatedCommodity = commodityRepository.save(commodity);
        return updatedCommodity.getId();
    }
}
