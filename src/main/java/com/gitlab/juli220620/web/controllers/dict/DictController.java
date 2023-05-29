package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.CurrencyDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    public ResponseEntity<List<BaseFlowerDictEntity>> getFlowers(HttpServletRequest http) {
        return getDict(http, baseFlowerDictRepo.findAll());
    }

    @GetMapping("/pot")
    public ResponseEntity<List<PotDictEntity>> getPots(HttpServletRequest http) {
        return getDict(http, potDictRepo.findAll());
    }

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyDictEntity>> getCurrencies(HttpServletRequest http) {
        return getDict(http, currencyDictRepo.findAll());
    }


    private <E> ResponseEntity<List<E>> getDict(HttpServletRequest http, List<E> list) {
        return authUtils.authorized(http, token ->
                list.stream()
                        .peek(Hibernate::initialize)
                        .collect(Collectors.toList()));
    }
}
