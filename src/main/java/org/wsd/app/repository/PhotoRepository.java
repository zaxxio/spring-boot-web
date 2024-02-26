package org.wsd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wsd.app.domain.PhotoEntity;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
//    @QueryHints(value = {
//            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
//            @QueryHint(name = "org.hibernate.cacheMode", value = "NORMAL"),
//    })
//    List<Photo> findAll();
}
