
package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.RoomRequest;
import com.dental_management.demo.model.response.BranchResponse;
import com.dental_management.demo.model.response.RoomResponse;
import com.dental_management.demo.model.entities.Branch;
import com.dental_management.demo.model.entities.Room;
import com.dental_management.demo.repository.BranchRepository;
import com.dental_management.demo.repository.RoomRepository;
import com.dental_management.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {

        Branch branch = getBranch(request.getBranchId());

        Room room = new Room();
        room.setName(request.getName().trim());
        room.setBranch(branch);

        Room savedRoom = roomRepository.save(room);

        return mapToResponse(savedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {

        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> responses = new ArrayList<>();

        for (Room room : rooms) {
            responses.add(mapToResponse(room));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByBranch(Long branchId) {

        getBranch(branchId);

        List<Room> rooms = roomRepository.findByBranch_Id(branchId);
        List<RoomResponse> responses = new ArrayList<>();

        for (Room room : rooms) {
            responses.add(mapToResponse(room));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found: " + id));

        return mapToResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found: " + id));

        Branch branch = getBranch(request.getBranchId());

        room.setName(request.getName().trim());
        room.setBranch(branch);

        Room updatedRoom = roomRepository.save(room);

        return mapToResponse(updatedRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found: " + id));

        roomRepository.delete(room);
    }

    private Branch getBranch(Long branchId) {

        return branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found: " + branchId));
    }

    private RoomResponse mapToResponse(Room room) {

        RoomResponse response = new RoomResponse();

        response.setId(room.getId());
        response.setName(room.getName());

        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setId(room.getBranch().getId());
        branchResponse.setName(room.getBranch().getName());

        response.setBranch(branchResponse);

        return response;
    }
}
