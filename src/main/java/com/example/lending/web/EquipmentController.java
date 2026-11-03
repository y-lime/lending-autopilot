package com.example.lending.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<Equipment> list(@RequestParam(name = "category", required = false) String category) {
        if (category != null) {
            return equipmentService.findByCategory(category);
        }
        return equipmentService.findAll();
    }

    @GetMapping("/{id}")
    public Equipment get(@PathVariable("id") long id) {
        return equipmentService.get(id);
    }
}
