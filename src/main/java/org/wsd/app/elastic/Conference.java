package org.wsd.app.elastic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "conference")
public class Conference {
    @Id
    private String conferenceId;
    private String conferenceName;
    private Date timestamp;
}
