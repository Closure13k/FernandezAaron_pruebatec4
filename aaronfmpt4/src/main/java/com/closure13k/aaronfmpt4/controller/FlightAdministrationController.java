package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.service.IFlightAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightAdministrationController {
    private final IFlightAdministrationService service;
}
