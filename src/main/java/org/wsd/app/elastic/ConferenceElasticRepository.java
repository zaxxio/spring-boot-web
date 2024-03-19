package org.wsd.app.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ConferenceElasticRepository extends ElasticsearchRepository<Conference, String> {
}
