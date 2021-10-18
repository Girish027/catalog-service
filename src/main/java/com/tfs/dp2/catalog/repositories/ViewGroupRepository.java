package com.tfs.dp2.catalog.repositories;

import com.tfs.dp2.catalog.entities.ViewGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ViewGroupRepository extends JpaRepository<ViewGroup,String> {

    ViewGroup findById(String id);

    ViewGroup findByName(String name);

    @Query(value = "select * from view_group vg, view_information v  where v.view_id IN ?1 AND v.view_group = vg.id AND v.is_active = 1",nativeQuery = true)
    List<ViewGroup> findByViewID(List<String> viewIdList);

}
