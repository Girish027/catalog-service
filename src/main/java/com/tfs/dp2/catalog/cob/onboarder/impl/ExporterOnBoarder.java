package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExporterOnBoarder extends PipelineOnBoarder {

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ViewController viewController;
    @Autowired
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    private static String INSERTED = "inserted";

    @Override
    protected ProvisionalStatus onBoardRequest(ValidateRequestBody provisionRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException {
        logOnBoardingMessage("exporter", provisionRequestBody);
        String product = provisionRequestBody.getCobValidateRequestBody().get().getProduct();
        String client = provisionRequestBody.getAccountName();
        int successCount = 0;
        for (Object[] objects : viewRepository.findByViewGroup(product)) {
            List<String> exporters = getExporters(client, objects[1].toString());
            String status = viewController.insertExporterMapping(client, objects[0].toString(), exporters);
            if (status.equals(INSERTED)) {
                successCount += 1;
            }
        }
        ;
        updateProvisionStatus(successCount, provisionalStatus);
        return provisionalStatus;
    }

    private List<String> getExporters(String client, String defaultClientViewMapping) throws COBInternalException {
        Optional<DefaultClientViewMapping> optionalDefaultClientViewMapping = pipelineOnBoarderUtils.getDefaultClientViewMapping(client, defaultClientViewMapping);
        if (optionalDefaultClientViewMapping.isPresent()) {
            List<String> exporterList = optionalDefaultClientViewMapping.get().getExporterList();
            if (exporterList != null)
                return exporterList;
        }
        return new ArrayList<>();
    }

    private void updateProvisionStatus(int successCount, ProvisionalStatus provisionalStatus) {
        if (successCount > 0)
            updateProvisionStatus(provisionalStatus, ProvisionStatus.SUCCESSFULLY_PROVISIONED);
        else
            updateProvisionStatus(provisionalStatus, ProvisionStatus.ALREADY_PROVISIONED);
    }

}
