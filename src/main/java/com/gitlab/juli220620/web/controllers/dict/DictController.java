package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.CurrencyDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
public class DictController {

    private final AuthUtils authUtils;

    private final BaseFlowerDictRepo baseFlowerDictRepo;
    private final PotDictRepo potDictRepo;
    private final CurrencyDictRepo currencyDictRepo;

    @GetMapping("/flower")
    public ResponseEntity<List<BaseFlowerDto>> getFlowers(HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                baseFlowerDictRepo.findAll().stream()
                .map(BaseFlowerDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/pot")
    public ResponseEntity<List<PotDto>> getPots(HttpServletRequest http) {
        return authUtils.authorized(http, token -> potDictRepo.findAll().stream()
                .map(PotDto::new)
                .toList());
    }

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyDto>> getCurrencies(HttpServletRequest http) {
        return authUtils.authorized(http, token -> currencyDictRepo.findAll().stream()
                .map(CurrencyDto::new)
                .toList());
    }
}
