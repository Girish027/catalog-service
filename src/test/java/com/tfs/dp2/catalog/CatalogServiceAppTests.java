package com.tfs.dp2.catalog;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.dataset.DatasetRepository;
import com.tfs.dp2.catalog.datasetRules.RulesRepository;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import com.tfs.dp2.catalog.services.Validator;
import com.tfs.dp2.catalog.services.ViewGroupService;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterRepository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestDataRepository;
import com.tfs.dp2.catalog.exporters.ExporterConfigRepository;
import com.tfs.dp2.catalog.exporters.ExporterMappingRepository;
import com.tfs.dp2.catalog.exporters.ExporterOverrridesRepository;
import com.tfs.dp2.catalog.exporters.ExporterRepository;
import com.tfs.dp2.catalog.view.View;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewInformationRepository;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnRepository;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSourceRepository;
import com.tfs.dp2.catalog.viewoutputgran.ViewOutputGranRepository;
import com.tfs.dp2.catalog.viewsource.ViewSourceRepository;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinitionRepository;

/**
 * @author Bikesh Singh
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ViewController.class, secure = false)

public class CatalogServiceAppTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private Validator validator;

	@MockBean(name="viewRepository")
    ViewRepository viewRepository;
    @MockBean
    ViewColumnRepository viewColumnRepository;
    @MockBean
    ClientInformationRepository clientInformationRepository;
    @MockBean
    ViewOutputGranRepository viewOutputGranRepository;
    @MockBean
    ViewHDFSSourceRepository viewHDFSSourceRepository;
    @MockBean
    DruidIngestDataRepository druidIngestDataRepository;
    @MockBean
    ViewSQLDefinitionRepository viewSQLDefinitionRepository;
    @MockBean
    ViewSourceRepository viewSourceRepository;
    @MockBean
    ViewInformationRepository viewInformationRepository;
    @MockBean
    DatasetRepository datasetRepository;
    @MockBean
    RulesRepository rulesRepository;
    @MockBean
    ViewGroupRepository viewGroupRepository;
    @MockBean
    ViewGroupService viewGroupService;
    @MockBean
    private SourceAdapterRepository sourceAdapterRepository;
    @MockBean
    private ExporterConfigRepository exporterConfigRepository;
    @MockBean
    private ExporterMappingRepository exporterMappingRepository;
    @MockBean
    private ExporterRepository exporterRepository;
    @MockBean
    private ExporterOverrridesRepository exporterOverrridesRepository;
    @MockBean
	private PipelineOnBoarderUtils pipelineOnBoarderUtils;
    

    
    /**
     * View is not active
     * @throws Exception
     */
	@Test
	public void testGetOrchestratorSnapshotByViewName1() throws Exception {
		
		mockMvc.perform(get("/catalog/orchestrator/snapshot/{viewname}","AssistInteractions").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	
	/**
	 * View is active
	 * 
	 * @throws Exception
	 */
/*	@Test
	public void testGetOrchestratorSnapshotByViewName2() throws Exception {
		String viewName = "AssistInteractions";
		View view = new View();
		view.setIsActive(1);
		view.setViewName(viewName);
		view.setOwner("Emil");
		view.setJobStartTime("1505484000000");
		view.setJobEndTime("1506812400000");
		view.setComplexity("3");
		view.setViewId("viewinfo_1513070000");
		Object[] obj = {"siriusxm", null, -1};
		List<Object[]> clients = new ArrayList<>();
		clients.add(obj);
		when(viewRepository.findByViewName(viewName)).thenReturn(view);
		when(viewRepository.findByViewName(viewName)).thenReturn(view);
		when(viewOutputGranRepository.findUniqueCronString(view.getViewId())).thenReturn(Arrays.asList("0 * * * *"));
		when(viewOutputGranRepository.findOrchestratorSnapshotClientListByViewId(view.getViewId(), "0 * * * *"))
				.thenReturn(clients);

		mockMvc.perform(
				get("/catalog/orchestrator/snapshot/{viewname}", viewName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].clientCronExpressionList", hasSize(1)))
				.andExpect(jsonPath("$[0].clientCronExpressionList[0].cronExpression", is("0 * * * *")))
				.andExpect(jsonPath("$[0].clientCronExpressionList[0].clientExecProps[0].name", is("siriusxm")))
				.andExpect(jsonPath("$[0].userName", is("Emil")))
				.andExpect(jsonPath("$[0].jobStartTime", is("1505484000000")))
				.andExpect(jsonPath("$[0].jobEndTime", is("1506812400000")))
				.andExpect(jsonPath("$[0].complexity", is("3")));
		}*/
}
