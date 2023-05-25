package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyDictRepo extends CrudRepository<CurrencyDictEntity, String> {

}
