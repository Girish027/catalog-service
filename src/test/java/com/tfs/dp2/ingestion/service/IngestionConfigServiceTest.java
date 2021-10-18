package com.tfs.dp2.ingestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientOnBoarder;
import com.tfs.dp2.ingestion.db.IngestionConfigRepository;
import com.tfs.dp2.ingestion.exceptions.StreamAlreadyExistsException;
import com.tfs.dp2.ingestion.exceptions.StreamInformationNotFoundException;
import com.tfs.dp2.ingestion.model.SinkConfig;
import com.tfs.dp2.ingestion.model.StreamDetails;
import com.tfs.dp2.ingestion.model.StreamInfo;
import com.tfs.dp2.ingestion.model.ValidatorConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = IngestionConfigService.class)
public class IngestionConfigServiceTest {

    @Autowired
    private IngestionConfigService ingestionConfigService;

    @MockBean
    private IngestionConfigRepository ingestionConfigRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    private String stream_id = "TIE";
    private String streamDetailsString = "{\"stream_id\":\"TIE\",\"stream_name\":\"TIE\",\"contact_email\":\"tie@247.ai\",\"parser_config\":{\"class\":\"com.tfs.ingestion.parser.JsonEventParser\",\"properties\":{\"event_id_path\":\"eventId\",\"client_id_path\":\"client.id\",\"event_time_path\":\"eventTime\",\"event_time_pattern\":\"epoch\"}},\"validator_config\":[{\"validator_class\":\"com.tfs.ingestion.parser.JsonValidator\",\"validator_rules\":[\"$.eventType == null\",\"$.accountId == null\"]}],\"sink_config\":null,\"tags\":[\"tag1\",\"tag2\"]}";

    StreamDetails streamDetails = mapper.readValue(streamDetailsString,StreamDetails.class);

    String streamInfoString = "{\"stream_name\":\"TIE\",\"contact_email\":\"tie@247.ai\",\"parser_config\":{\"class\":\"com.tfs.ingestion.parser.JsonEventParser\",\"properties\":{\"event_id_path\":\"eventId\",\"client_id_path\":\"client.id\",\"event_time_path\":\"eventTime\",\"event_time_pattern\":\"epoch\"}},\"tags\":[\"tag1\",\"tag2\"]}";

    StreamInfo streamInfo = mapper.readValue(streamInfoString,StreamInfo.class);

    String validatorsString = "[{\"validator_class\":\"com.tfs.ingestion.parser.JsonValidator\",\"validator_rules\":[\"$.eventType == null\",\"$.accountId == null\"]}]";

    List<ValidatorConfig> validators = mapper.readValue(validatorsString, List.class);

    String sinksString = "[{\"sink_type\":\"kafka\",\"sink_properties\":{\"topic\":\"custom_topic_${clientId}\"}},{\"sink_type\":\"hdfs\",\"sink_properties\":{\"retention\":\"12m\",\"root_path\":\"/custom/path/${clientId}\"}},{\"sink_type\":\"cos\",\"sink_properties\":{\"region\":\"US South\",\"root_path\":\"/custom/path/${clientId}\"}}]";

    List<SinkConfig> sinkConfigs = mapper.readValue(sinksString, List.class);


    public IngestionConfigServiceTest() throws IOException {
    }


    @Test
    public void insertStreamInfoTest() throws  StreamAlreadyExistsException {
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(streamInfo);
        try {
            ingestionConfigService.insertStreamInfo(stream_id, streamInfo);
        } catch (Exception exception){
            Assert.assertEquals(StreamAlreadyExistsException.class, exception.getClass());
        }
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(null);
        ingestionConfigService.insertStreamInfo(stream_id, streamInfo);
    }

    @Test
    public void updateStreamInfoTest() throws  StreamInformationNotFoundException {

        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(null);
        try {
            ingestionConfigService.updateStreamInfo(stream_id, streamInfo);
        } catch (Exception exception){
            Assert.assertEquals(StreamInformationNotFoundException.class, exception.getClass());
        }
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(streamInfo);
        ingestionConfigService.updateStreamInfo(stream_id, streamInfo);

    }

    @Test
    public void insertOrUpdateValidatorsTest() throws  StreamInformationNotFoundException {

        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(null);
        try {
            ingestionConfigService.updateStreamInfo(stream_id, streamInfo);
        } catch (Exception exception){
            Assert.assertEquals(StreamInformationNotFoundException.class, exception.getClass());
        }
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(streamInfo);
        ingestionConfigService.insertOrUpdateValidators(stream_id, validators);
    }

    @Test
    public void insertOrUpdateSinksTest() throws  StreamInformationNotFoundException {

        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(null);
        try {
            ingestionConfigService.updateStreamInfo(stream_id, streamInfo);
        } catch (Exception exception){
            Assert.assertEquals(StreamInformationNotFoundException.class, exception.getClass());
        }
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(streamInfo);
        ingestionConfigService.insertOrUpdateSinks(stream_id, sinkConfigs);

    }

    @Test
    public void getStreamTest() throws StreamInformationNotFoundException {
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(null);
        try {
            ingestionConfigService.getStream(stream_id);
        } catch (Exception exception){
            Assert.assertEquals(StreamInformationNotFoundException.class, exception.getClass());
        }
        Mockito.when(ingestionConfigRepository.findStreamInfo(stream_id)).thenReturn(streamInfo);
        StreamDetails streamDetails = ingestionConfigService.getStream(stream_id);
        Assert.assertEquals(streamDetails.getStreamId(),stream_id);

    }

}
