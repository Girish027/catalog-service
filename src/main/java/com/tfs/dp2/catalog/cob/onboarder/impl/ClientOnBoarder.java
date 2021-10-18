package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.ClientAlreadyExistException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ClientOnBoarder extends PipelineOnBoarder {

    @Autowired
    private ViewController viewController;
    @Autowired
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    @Override
    protected ProvisionalStatus onBoardRequest(ValidateRequestBody provisionRequestBody,ProvisionalStatus provisionalStatus) {
        String timezone = "";
        logOnBoardingMessage("client", provisionRequestBody);
        String clientName = provisionRequestBody.getClientName();
        String accountName = provisionRequestBody.getAccountName();
        Optional<COBValidateRequestBody> timezoneMapped = provisionRequestBody.getCobValidateRequestBody();
        if(timezoneMapped.isPresent()) {
            timezone = timezoneMapped.get().getAccountProperties().getTimezone();
        }

        try {
            viewController.insertClient(pipelineOnBoarderUtils.formClientOnBoardingRequest(clientName, accountName, timezone));
            updateProvisionStatus(provisionalStatus,ProvisionStatus.SUCCESSFULLY_PROVISIONED);
        } catch (ClientAlreadyExistException e) {
            log.info("Account {} is already provisioned for Client {}", new Object[]{accountName, clientName});
            log.error(e.getMessage(), e);
            updateProvisionStatus(provisionalStatus,ProvisionStatus.ALREADY_PROVISIONED);
        }
        return provisionalStatus;
    }


}
