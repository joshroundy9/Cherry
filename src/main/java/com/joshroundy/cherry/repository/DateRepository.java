package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DateRepository extends JpaRepository<DateEntity, Integer> {
    //Optional<List<DateEntity>> findByUserID(Integer UserID);
}
