package com.example.lending.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.lending.domain.Equipment;
import com.example.lending.domain.EquipmentStatus;

@Mapper
public interface EquipmentMapper {

    @Select("SELECT id, name, category, status FROM equipment ORDER BY id")
    List<Equipment> findAll();

    @Select("SELECT id, name, category, status FROM equipment WHERE category = #{category} ORDER BY id")
    List<Equipment> findByCategory(@Param("category") String category);

    @Select("SELECT id, name, category, status FROM equipment WHERE id = #{id}")
    Optional<Equipment> findById(@Param("id") long id);

    @Update("UPDATE equipment SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") long id, @Param("status") EquipmentStatus status);
}
