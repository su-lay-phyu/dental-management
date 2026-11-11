package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.entities.Branch;
import com.dental_management.demo.model.entities.City;
import com.dental_management.demo.model.entities.Status;
import com.dental_management.demo.model.request.BranchRequest;
import com.dental_management.demo.model.response.BranchResponse;
import com.dental_management.demo.repository.BranchRepository;
import com.dental_management.demo.repository.CityRepository;
import com.dental_management.demo.service.BranchService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final CityRepository cityRepository;

    @Override
    public BranchResponse createBranch(BranchRequest request) {

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "City not found"
                ));

        Branch branch = new Branch();

        branch.setName(request.getName());
        branch.setPhone(request.getPhone());
        branch.setAddress(request.getAddress());
        branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        branch.setCity(city);
        branch.setStatus(Status.ACTIVE);

        Branch savedBranch = branchRepository.save(branch);

        return convertToResponse(savedBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> getAllBranches() {

        List<Branch> branches = branchRepository.findAll();

        List<BranchResponse> responses = new ArrayList<>();

        for (Branch branch : branches) {
            responses.add(convertToResponse(branch));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse getBranchById(Long id) {

        Branch branch = findBranchById(id);

        return convertToResponse(branch);
    }

    @Override
    public BranchResponse updateBranch(Long id, BranchRequest request) {

        Branch branch = findBranchById(id);

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "City not found"
                ));

        branch.setName(request.getName());
        branch.setPhone(request.getPhone());
        branch.setAddress(request.getAddress());
        branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        branch.setCity(city);

        Branch updatedBranch = branchRepository.save(branch);

        return convertToResponse(updatedBranch);
    }

    @Override
    public BranchResponse updateBranchStatus(Long id, String status) {

        Branch branch = findBranchById(id);

        try {
            Status newStatus = Status.valueOf(status.toUpperCase());

            if (newStatus != Status.ACTIVE && newStatus != Status.INACTIVE) {
                throw new IllegalArgumentException();
            }

            branch.setStatus(newStatus);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be ACTIVE or INACTIVE"
            );
        }

        return convertToResponse(branchRepository.save(branch));
    }

    @Override
    public void deleteBranch(Long id) {

        Branch branch = findBranchById(id);

        // Soft delete
        branch.setStatus(Status.INACTIVE);

        branchRepository.save(branch);
    }

    private Branch findBranchById(Long id) {

        return branchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Branch not found"
                ));
    }

    private BranchResponse convertToResponse(Branch branch) {

        BranchResponse response = new BranchResponse();

        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setPhone(branch.getPhone());
        response.setAddress(branch.getAddress());
        response.setStatus(branch.getStatus());
        response.setLatitude(branch.getLatitude());
        response.setLongitude(branch.getLongitude());

        if (branch.getCity() != null) {
            response.setCityId(branch.getCity().getId());
            response.setCityName(branch.getCity().getName());
        }

        return response;
    }
}