package com.dental_management.demo.service;

import com.dental_management.demo.model.request.BranchRequest;
import com.dental_management.demo.model.response.BranchResponse;

import java.util.List;

public interface BranchService {

    BranchResponse createBranch(BranchRequest request);

    List<BranchResponse> getAllBranches();

    BranchResponse getBranchById(Long id);

    BranchResponse updateBranch(Long id, BranchRequest request);

    BranchResponse updateBranchStatus(Long id, String status);

    void deleteBranch(Long id);
}