package com.example.lending.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lending.domain.Equipment;
import com.example.lending.mapper.EquipmentMapper;

@Service
@Transactional(readOnly = true)
public class EquipmentService {

    private final EquipmentMapper equipmentMapper;

    public EquipmentService(EquipmentMapper equipmentMapper) {
        this.equipmentMapper = equipmentMapper;
    }

    public List<Equipment> findAll() {
        return equipmentMapper.findAll();
    }

    public List<Equipment> findByCategory(String category) {
        return equipmentMapper.findByCategory(category);
    }

    public Equipment get(long id) {
        return equipmentMapper.findById(id)
                .orElseThrow(() -> new NotFoundException("備品が見つかりません: id=" + id));
    }
}
