
package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByBranch_Id(Long branchId);
}
