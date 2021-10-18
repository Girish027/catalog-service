package com.tfs.dp2.ingestion.controller;


import com.codahale.metrics.annotation.Timed;
import com.tfs.dp2.ingestion.exceptions.StreamAlreadyExistsException;
import com.tfs.dp2.ingestion.exceptions.StreamInformationNotFoundException;
import com.tfs.dp2.ingestion.model.SinkConfig;
import com.tfs.dp2.ingestion.model.StreamDetails;
import com.tfs.dp2.ingestion.model.StreamInfo;
import com.tfs.dp2.ingestion.model.ValidatorConfig;
import com.tfs.dp2.ingestion.service.IngestionConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ingestion/v1")
public class IngestionConfigController {

    @Autowired
    private IngestionConfigService ingestionConfigService;

    /* Update existing StreamInfo. */
    @PutMapping(path = "/streams/{stream_id}")
    @Timed
    public void putStream(@Valid @PathVariable("stream_id") String streamId, @Valid @RequestBody StreamInfo streamInfo)
            throws StreamInformationNotFoundException {
         ingestionConfigService.updateStreamInfo(streamId, streamInfo);
    }

    /* Add New StreamInfo. */
    @PostMapping(path = "/streams/{stream_id}")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public void addStream(@PathVariable("stream_id") String streamId, @Valid @RequestBody StreamInfo streamInfo)
            throws StreamAlreadyExistsException {
        ingestionConfigService.insertStreamInfo(streamId, streamInfo);
    }

    /* Add New StreamDetails. */
    @PostMapping(path = "/streams/{stream_id}/validators")
    @Timed
    public void addValidationsToStream(@PathVariable("stream_id") String streamId, @Valid @RequestBody List<ValidatorConfig> validators)
            throws StreamInformationNotFoundException {
        ingestionConfigService.insertOrUpdateValidators(streamId, validators);
    }

    /* Add New StreamDetails. */
    @PostMapping(path = "/streams/{stream_id}/sinks")
    @Timed
    public void addSinksToStream(@PathVariable("stream_id") String streamId, @Valid @RequestBody List<SinkConfig> sinkConfigs)
            throws StreamInformationNotFoundException {
         ingestionConfigService.insertOrUpdateSinks(streamId, sinkConfigs);
    }

    /* Returns details of particular StreamDetails identified by StreamId */
    @GetMapping(path = "/streams/{id}")
    @Timed
    public StreamDetails getStream(@PathVariable("id") String streamId)
            throws StreamInformationNotFoundException {
        log.debug("Calling of Ingestion API to get particular StreamDetails Id ::"+ streamId);
        return ingestionConfigService.getStream(streamId);
    }
}


