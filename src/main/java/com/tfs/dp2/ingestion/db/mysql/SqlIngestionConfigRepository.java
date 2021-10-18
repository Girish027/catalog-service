package com.tfs.dp2.ingestion.db.mysql;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.tfs.dp2.ingestion.db.IngestionConfigRepository;
import com.tfs.dp2.ingestion.db.mysql.entities.StreamSinkEntity;
import com.tfs.dp2.ingestion.db.mysql.entities.StreamValidatorEntity;
import com.tfs.dp2.ingestion.db.mysql.repositories.StreamSinksRepository;
import com.tfs.dp2.ingestion.db.mysql.repositories.StreamValidatorsRepository;
import com.tfs.dp2.ingestion.model.SinkConfig;
import com.tfs.dp2.ingestion.model.StreamInfo;
import com.tfs.dp2.ingestion.model.ValidatorConfig;
import com.tfs.dp2.ingestion.db.mysql.entities.StreamInfoEntity;
import com.tfs.dp2.ingestion.db.mysql.repositories.StreamInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class SqlIngestionConfigRepository implements IngestionConfigRepository {

    @Autowired
    private StreamInfoRepository streamInfoRepository;

    @Autowired
    private StreamSinksRepository streamSinksRepository;

    @Autowired
    private StreamValidatorsRepository streamValidatorsRepository;

    @Override
    @Timed
    @ExceptionMetered
    public StreamInfo findStreamInfo(String streamId) {
        Optional<StreamInfoEntity> streamInfoEntity =
                Optional.ofNullable(streamInfoRepository.findOne(streamId));
        return streamInfoEntity.map(StreamInfoEntity::getStream_Info).orElse(null);
    }

    @Override
    @Timed
    @ExceptionMetered
    public List<ValidatorConfig> findStreamValidators(String streamId) {
        Optional<StreamValidatorEntity> streamValidatorEntity =
                Optional.ofNullable(streamValidatorsRepository.findOne(streamId));
        return streamValidatorEntity.map(StreamValidatorEntity::getValidatorConfigs).orElse(null);
    }

    @Override
    @Timed
    @ExceptionMetered
    public List<SinkConfig> findStreamSinks(String streamId) {
        Optional<StreamSinkEntity> streamSinkEntity =
                Optional.ofNullable(streamSinksRepository.findOne(streamId));
        return streamSinkEntity.map(StreamSinkEntity::getSinkConfigs).orElse(null);
    }

    @Override
    @Timed
    @ExceptionMetered
    public void insertStreamInfo(String streamId, StreamInfo streamInfo) {
        StreamInfoEntity streamInfoEntity = new StreamInfoEntity();
        streamInfoEntity.setStream_id(streamId);
        streamInfoEntity.setStream_Info(streamInfo);
        streamInfoRepository.save(streamInfoEntity);
    }


    @Override
    @Timed
    @ExceptionMetered
    public void insertStreamValidators(String streamId, List<ValidatorConfig> validatorConfigList) {
        StreamValidatorEntity streamValidatorEntity = new StreamValidatorEntity();
        streamValidatorEntity.setStream_id(streamId);
        streamValidatorEntity.setValidatorConfigs(validatorConfigList);
        streamValidatorsRepository.save(streamValidatorEntity);
    }

    @Override
    @Timed
    @ExceptionMetered
    public void insertStreamSinks(String streamId, List<SinkConfig> sinkConfigList) {
        StreamSinkEntity streamSinkEntity = new StreamSinkEntity();
        streamSinkEntity.setStream_id(streamId);
        streamSinkEntity.setSinkConfigs(sinkConfigList);
        streamSinksRepository.save(streamSinkEntity);
    }
}
