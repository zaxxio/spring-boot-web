package org.wsd.app.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceElasticRepository extends ElasticsearchRepository<Conference, String> {
}
