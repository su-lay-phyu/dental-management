package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.BranchRequest;
import com.dental_management.demo.model.response.BranchResponse;
import com.dental_management.demo.service.BranchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponse> createBranch(
            @Valid @RequestBody BranchRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchService.createBranch(request));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {

        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchRequest request) {

        return ResponseEntity.ok(
                branchService.updateBranch(id, request)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BranchResponse> updateBranchStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                branchService.updateBranchStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(
            @PathVariable Long id) {

        branchService.deleteBranch(id);

        return ResponseEntity.ok("Branch deactivated successfully");
    }
}