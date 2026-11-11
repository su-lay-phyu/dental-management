
package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.SpecialtyRequest;
import com.dental_management.demo.model.response.SpecialtyResponse;
import com.dental_management.demo.service.impl.SpecialtyServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyServiceImpl specialtyService;

    @PostMapping
    public ResponseEntity<SpecialtyResponse> createSpecialty(
            @Valid @RequestBody SpecialtyRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specialtyService.createSpecialty(request));
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {

        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                specialtyService.getSpecialtyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyRequest request) {

        return ResponseEntity.ok(
                specialtyService.updateSpecialty(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(
            @PathVariable Long id) {

        specialtyService.deleteSpecialty(id);

        return ResponseEntity.noContent().build();
    }
}
