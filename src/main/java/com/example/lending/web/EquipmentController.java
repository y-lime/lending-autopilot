package com.example.lending.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lending.domain.Equipment;
import com.example.lending.service.EquipmentService;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<Equipment> list() {
        return equipmentService.findAll();
    }

    @GetMapping("/{id}")
    public Equipment get(@PathVariable("id") long id) {
        return equipmentService.get(id);
    }
}
