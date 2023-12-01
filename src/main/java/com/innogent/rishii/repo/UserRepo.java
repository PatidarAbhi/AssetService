package com.innogent.rishii.repo;

import com.innogent.rishii.dtos.UserDTO;
import com.innogent.rishii.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    @Query("SELECT NEW com.innogent.rishii.dtos.UserDTO(u.id, u.name, u.email) " +
            "FROM Users u WHERE u.id = :userId")
     Optional<UserDTO> findUserDtoById(Long userId);


}
