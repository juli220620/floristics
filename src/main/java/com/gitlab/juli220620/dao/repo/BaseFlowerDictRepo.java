package com.gitlab.juli220620.dao.repo;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BaseFlowerDictRepo extends CrudRepository<BaseFlowerDictEntity, String> {

}
