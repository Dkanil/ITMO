package org.example.lab4.controller;

import org.example.lab4.DTO.PointRequest;
import org.example.lab4.DTO.PointResponse;
import org.example.lab4.service.JwtCore;
import org.example.lab4.service.PointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RestController
@RequestMapping("/home")
public class PointController {

    private final PointService pointService;
    private final JwtCore jwtCore;

    public PointController(PointService pointService, JwtCore jwtCore) {
        this.pointService = pointService;
        this.jwtCore = jwtCore;
    }

    @PostMapping("/submit")
    public PointResponse submit(@RequestBody PointRequest point, @RequestHeader("Authorization") String authHeader) { // todo мб валидацию
        String username = jwtCore.extractUsername(authHeader.replace("Bearer ", ""));
        return pointService.processAndSavePoint(point, username);
    }

    @GetMapping("/points")
    public ResponseEntity<List<PointResponse>> getUserPoints(@RequestHeader("Authorization") String authHeader) {
        String username = jwtCore.extractUsername(authHeader.replace("Bearer ", ""));
        List<PointResponse> response = pointService.findAllByUsername(username)
                .stream()
                .map(p -> new PointResponse(
                        p.getX(),
                        p.getY(),
                        p.getR(),
                        p.getUsername(),
                        p.isHit(),
                        p.getTimestamp()))
                .toList();
        return ResponseEntity.ok(response);
    }
}