package org.example.lab4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

    private final PointService pointService;

    public MainController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/main")
    public String savePoint(@RequestParam(name="name", required=false, defaultValue="defaultUser") String name) {
        return pointService.savePoint(name, 0.0, 0.0, 1.0).toString();
    }

    @GetMapping()
    public String getAllUserPoints(@RequestParam(name="name", required=false, defaultValue="defaultUser") String name) {
        return pointService.getAllPoints().toString();
    }
}