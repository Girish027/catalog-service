package com.tfs.dp2.ingestion.service;

import com.tfs.dp2.ingestion.db.IngestionConfigRepository;
import com.tfs.dp2.ingestion.exceptions.StreamAlreadyExistsException;
import com.tfs.dp2.ingestion.exceptions.StreamInformationNotFoundException;
import com.tfs.dp2.ingestion.model.SinkConfig;
import com.tfs.dp2.ingestion.model.StreamDetails;
import com.tfs.dp2.ingestion.model.StreamInfo;
import com.tfs.dp2.ingestion.model.ValidatorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class IngestionConfigService {

    @Autowired
    private IngestionConfigRepository ingestionConfigRepository;

    @Transactional
    public void insertStreamInfo(String streamId, StreamInfo streamInfo)
            throws StreamAlreadyExistsException {
        StreamInfo sInfo = ingestionConfigRepository.findStreamInfo(streamId);
        if(sInfo == null) {
            ingestionConfigRepository.insertStreamInfo(streamId, streamInfo);
        } else {
            log.info("StreamId :" + streamId + " Already exists.");
            throw new StreamAlreadyExistsException("StreamId:" + streamId+ "already exists!!");
        }
    }

    @Transactional
    public void updateStreamInfo(String streamId, StreamInfo streamInfo)
            throws StreamInformationNotFoundException {
        StreamInfo sInfo = ingestionConfigRepository.findStreamInfo(streamId);
        if(sInfo != null) {
            ingestionConfigRepository.insertStreamInfo(streamId, streamInfo);
        } else {
            log.info("StreamId :" + streamId + " does not exists.");
            throw new StreamInformationNotFoundException("StreamId :"
                    + streamId + " does not exists." );
        }
    }

    @Transactional
    public void  insertOrUpdateValidators(String streamId, List<ValidatorConfig> validators)
            throws StreamInformationNotFoundException {
        StreamInfo streamInfo = ingestionConfigRepository.findStreamInfo(streamId);
        if(streamInfo != null){
            ingestionConfigRepository.insertStreamValidators(streamId,validators);
        } else {
            log.info("StreamId :" + streamId + " does not exists.");
            throw new StreamInformationNotFoundException("StreamId :"
                    + streamId + " does not exists." );
        }
    }

    @Transactional
    public void insertOrUpdateSinks(String streamId, List<SinkConfig> sinkConfigs)
            throws StreamInformationNotFoundException {
        StreamInfo streamInfo = ingestionConfigRepository.findStreamInfo(streamId);
        if(streamInfo != null){
            ingestionConfigRepository.insertStreamSinks(streamId,sinkConfigs);
        } else {
            log.info("StreamId :" + streamId + " does not exists.");
            throw new StreamInformationNotFoundException("StreamId :"
                    + streamId + " does not exists." );
        }
    }

    @Transactional
    public StreamDetails getStream(String streamId)
            throws StreamInformationNotFoundException {
        StreamInfo streamInfo = ingestionConfigRepository.findStreamInfo(streamId);
        if(streamInfo != null) {
            StreamDetails streamDetails = new StreamDetails();
            streamDetails.setStreamId(streamId);
            List<ValidatorConfig> validatorConfigList = ingestionConfigRepository.findStreamValidators(streamId);
            List<SinkConfig> sinkConfigList = ingestionConfigRepository.findStreamSinks(streamId);
            streamDetails.setStreamName(streamInfo.getStreamName());
            streamDetails.setContact_email(streamInfo.getContact_email());
            streamDetails.setTags(streamInfo.getTags());
            streamDetails.setParserConfig(streamInfo.getParserConfig());
            streamDetails.setValidatorConfig(validatorConfigList);
            streamDetails.setSinkConfig(sinkConfigList);
            return streamDetails;
        } else {
            log.info("StreamId :" + streamId + " does not exists.");
            throw new StreamInformationNotFoundException("StreamId :"
                    + streamId + " does not exists." );
        }
    }
}
