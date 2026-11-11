
package com.dental_management.demo.service;

import com.dental_management.demo.model.request.RoomRequest;
import com.dental_management.demo.model.response.RoomResponse;

import java.util.List;

public interface RoomService {

    RoomResponse createRoom(RoomRequest request);

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getRoomsByBranch(Long branchId);

    RoomResponse getRoomById(Long id);

    RoomResponse updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);
}
