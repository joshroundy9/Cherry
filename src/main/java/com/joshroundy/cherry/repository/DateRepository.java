package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DateRepository extends JpaRepository<DateEntity, Integer> {
    List<DateEntity> findByUserID(Integer userID);
    Optional<DateEntity> findByUserIDAndDate(Integer userID, Date date);
}