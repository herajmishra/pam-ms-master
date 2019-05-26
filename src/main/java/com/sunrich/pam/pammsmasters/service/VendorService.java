package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Vendor;
import com.sunrich.pam.pammsmasters.dto.VendorDTO;
import com.sunrich.pam.pammsmasters.exception.VendorNotFoundException;
import com.sunrich.pam.pammsmasters.repository.VendorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Used to save or update the vendor
     *
     * @param vendorDTO - domain object
     * @return - the saved/updated vendor
     */
    public VendorDTO saveOrUpdate(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();

        if (vendorDTO.getId() != null) {
            vendor = findEntityById(vendorDTO.getId());
        }

        modelMapper.map(vendorDTO, vendor);

        vendor.setRecordStatus(true);
        Vendor savedVendor = vendorRepository.save(vendor);
        return modelMapper.map(savedVendor, VendorDTO.class);
    }

    /**
     * Used to get the list of vendor
     *
     * @return - the list of vendor
     */
    public List<VendorDTO> findAll() {
        List<Vendor> vendorList = vendorRepository.findAllByRecordStatusTrueOrderByIdDesc();
        return vendorList.stream()
                .map(vendor -> modelMapper.map(vendor, VendorDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Used to get the vendor by id
     *
     * @param id - vendor identifier
     * @return - vendor object
     */
    public VendorDTO findById(Long id) {
        Vendor vendor = findEntityById(id);
        return modelMapper.map(vendor, VendorDTO.class);
    }

    private Vendor findEntityById(Long id) {
        Optional<Vendor> optVendor = vendorRepository.findByIdAndRecordStatusTrue(id);
        if (!optVendor.isPresent()) {
            throw new VendorNotFoundException("Vendor Not Found!");
        }
        return optVendor.get();
    }

    /**
     * Used to get the vendor by name
     *
     * @param name - vendor name
     * @return - vendor object
     */
    public VendorDTO findByName(String name) {
        Optional<Vendor> optVendor = vendorRepository.findByNameAndRecordStatusTrue(name);
        if (!optVendor.isPresent()) {
            throw new VendorNotFoundException("Vendor Not Found!");
        }
        return modelMapper.map(optVendor.get(), VendorDTO.class);
    }

    /**
     * Used to logically delete the vendor by updating recordStatus flag to false
     *
     * @param id - vendor identifier
     * @return - removed vendor identifier
     */
    public Long delete(Long id) {
        Vendor vendor = findEntityById(id);
        vendor.setRecordStatus(false);
        Vendor updatedVendor = vendorRepository.save(vendor);
        return updatedVendor.getId();
    }
}
