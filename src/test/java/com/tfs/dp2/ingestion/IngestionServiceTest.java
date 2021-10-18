package com.tfs.dp2.ingestion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.ingestion.controller.IngestionConfigController;
import com.tfs.dp2.ingestion.model.StreamDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = IngestionConfigController.class, secure = false)
public class IngestionServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngestionConfigController ingestionConfigController;

    private static ObjectMapper mapper = new ObjectMapper();

    private String stream_id = "TIE";
    private String streamDetailsString = "{\"stream_id\":\"TIE\",\"stream_name\":\"TIE\",\"contact_email\":\"tie@247.ai\",\"parser_config\":{\"class\":\"com.tfs.ingestion.parser.JsonEventParser\",\"properties\":{\"event_id_path\":\"eventId\",\"client_id_path\":\"client.id\",\"event_time_path\":\"eventTime\",\"event_time_pattern\":\"epoch\"}},\"validator_config\":[{\"validator_class\":\"com.tfs.ingestion.parser.JsonValidator\",\"validator_rules\":[\"$.eventType == null\",\"$.accountId == null\"]}],\"sink_config\":null,\"tags\":[\"tag1\",\"tag2\"]}";

    StreamDetails streamDetails = mapper.readValue(streamDetailsString,StreamDetails.class);

    public IngestionServiceTest() throws IOException {
    }

    @Test
    public void getStreams() throws Exception {
        Mockito.when(ingestionConfigController.getStream(Mockito.anyString())).thenReturn(streamDetails);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ingestion/v1/streams/TIE");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals(mapper.readValue(result.getResponse().getContentAsString(),StreamDetails.class).getStreamId(),stream_id);
    }
   }
