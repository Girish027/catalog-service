package com.tfs.dp2.catalog.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by bikesh.singh on 12-03-2018.
 */

@Repository
@Transactional
public interface ViewInformationRepository extends JpaRepository<ViewInformation, String> {

    ViewInformation findByViewName(String viewName);
}
