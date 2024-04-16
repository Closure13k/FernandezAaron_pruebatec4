package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightController {

    private final IFlightService service;
}
