package com.tfs.dp2.catalog;

import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewDefinition;
import com.tfs.dp2.catalog.view.ViewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 30-03-2018.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = ViewController.class, secure = false)
public class CatalogServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViewController viewController;
    @MockBean
    private ViewRepository viewRepository;

    ViewDefinition mockViewDefinition = new ViewDefinition();
    List<ViewDefinition> mockViewDefinitionList = new ArrayList<ViewDefinition>();

    @Test
    public void retrieveViewDefinitionTest(){
        System.out.println("here retrieveViewDefinition");

        try {
            Mockito.when(viewController.getViewDefinitionByViewName(Mockito.anyList())).thenReturn(mockViewDefinitionList);
            System.out.println("here11222");
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/view-definitions");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response "+result.getResponse().getContentAsString());


            Assert.assertEquals("", result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("here13333");
      }

}
