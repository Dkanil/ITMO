package org.example.lab4.controller;

import org.example.lab4.DTO.PointRequest;
import org.example.lab4.DTO.PointResponse;
import org.example.lab4.service.JwtCore;
import org.example.lab4.service.PointService;
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

    @PostMapping
    public PointResponse submit(@RequestBody PointRequest point, @RequestHeader String token) { // todo мб валидацию
        String username = jwtCore.extractUsername(token);
        return pointService.processAndSavePoint(point, username);
    }

    @GetMapping
    public ResponseEntity<List<PointResponse>> getUserPoints(@RequestHeader String token) {
        String username = jwtCore.extractUsername(token);
        List<PointResponse> response = pointService.findAllByUsername(username)
                .stream()
                .map(p -> new PointResponse(
                        p.getX(),
                        p.getY(),
                        p.getR(),
                        p.getUsername(),
                        p.isHit()))
                .toList();
        return ResponseEntity.ok(response);
    }
}