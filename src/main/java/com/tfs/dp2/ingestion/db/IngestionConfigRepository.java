package com.tfs.dp2.ingestion.db;

import com.tfs.dp2.ingestion.model.SinkConfig;
import com.tfs.dp2.ingestion.model.ValidatorConfig;
import com.tfs.dp2.ingestion.model.StreamInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngestionConfigRepository {

    StreamInfo findStreamInfo(String streamId);

    List<ValidatorConfig> findStreamValidators(String streamId);

    List<SinkConfig> findStreamSinks(String streamId);

    void insertStreamInfo(String streamId, StreamInfo streamInfo);

    void insertStreamValidators(String streamId, List<ValidatorConfig>  validatorConfigList);

    void insertStreamSinks(String streamId, List<SinkConfig> sinkConfigList);
}
