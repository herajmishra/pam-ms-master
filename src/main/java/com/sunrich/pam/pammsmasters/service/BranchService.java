package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Branch;
import com.sunrich.pam.pammsmasters.exception.BranchNotFoundException;
import com.sunrich.pam.pammsmasters.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    /**
     * Used to save or update the branch
     *
     * @param branch - domain object
     * @return - the saved/updated branch object
     */
    public Branch saveOrUpdate(Branch branch) {
        branch.setRecordStatus(true);
        return branchRepository.save(branch);
    }

    /**
     * Used to get the list of branches
     *
     * @return - list of branches
     */
    public List<Branch> findAll() {
        return branchRepository.findAllByRecordStatusTrueOrderByIdDesc();
    }

    /**
     * Used to get the branch by id
     *
     * @param id - branch identifier
     * @return - branch object
     */
    public Branch findById(Long id) {
        Optional<Branch> optBranch = branchRepository.findByIdAndRecordStatusTrue(id);
        if (!optBranch.isPresent()) {
            throw new BranchNotFoundException("branch not found");
        }
        return optBranch.get();
    }

    /**
     * Used to get the branch by name
     *
     * @param name - branch name
     * @return - branch object
     */
    public Branch findByName(String name) {
        Optional<Branch> optBranch = branchRepository.findByNameAndRecordStatusTrue(name);
        if (!optBranch.isPresent()) {
            throw new BranchNotFoundException("Branch not found!");
        }
        return optBranch.get();
    }

    /**
     * Used to logically delete the branch by updating recordStatus flag to false
     *
     * @param id - branch identifier
     * @return - removed branch identifier
     */
    public Long delete(Long id) {
        Branch branch = findById(id);
        branch.setRecordStatus(false);
        branchRepository.save(branch);
        return branch.getId();
    }
}
