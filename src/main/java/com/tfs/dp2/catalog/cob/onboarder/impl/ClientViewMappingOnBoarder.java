package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.ClientViewMappingAlreadyExistException;
import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.impl.decorators.BasicClientViewMapper;
import com.tfs.dp2.catalog.cob.onboarder.impl.decorators.ClientViewMapperAliasDecorator;
import com.tfs.dp2.catalog.cob.onboarder.impl.decorators.ClientViewMapperCronDecorator;
import com.tfs.dp2.catalog.cob.onboarder.impl.decorators.ClientViewMapperDefaultDecorator;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClientViewMappingOnBoarder extends PipelineOnBoarder {

    @Autowired
    private ClientInformationRepository clientInformationRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private ViewController viewController;

    private ClientViewMapper clientViewMapper;

    public ClientViewMappingOnBoarder() {
        clientViewMapper = new ClientViewMapperCronDecorator(new ClientViewMapperAliasDecorator(new ClientViewMapperDefaultDecorator(new BasicClientViewMapper())));
    }

    @Override
    protected ProvisionalStatus onBoardRequest(ValidateRequestBody provisionRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException {
        logOnBoardingMessage("mapping", provisionRequestBody);
        String clientName = provisionRequestBody.getClientName();
        String accountName = provisionRequestBody.getAccountName();
        String product = provisionRequestBody.getCobValidateRequestBody().get().getProduct();
        submitViewClientMappingRequest(provisionRequestBody, provisionalStatus);
        return provisionalStatus;

    }

    private void submitViewClientMappingRequest(ValidateRequestBody provisionRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException {
        String product = provisionRequestBody.getCobValidateRequestBody().get().getProduct();
        int successCount = 0;
        for (Object[] objects : viewRepository.findByViewGroup(product)) {
            Optional<ClientViewMapping> optionalClientViewMapping = buildClientViewMappingRequest(provisionRequestBody, new ClientViewMapper.DefClientViewMap(objects[0].toString(), objects[1].toString()));
            if (optionalClientViewMapping.isPresent()) {
                try {
                    viewController.insertClientViewMapping(optionalClientViewMapping.get());
                    successCount += 1;
                } catch (ClientViewMappingAlreadyExistException e) {
                    log.warn(String.format("Mapping already exists for client %s and view %s", provisionRequestBody.getClientName(), product));
                }catch (InvalidExecutionPropertiesException e) {
                    log.warn(String.format("Invalid execution properties provided for client %s and view %s", provisionRequestBody.getClientName(), product));
                }
                catch (RuntimeException e) {
                    String message = String.format("Unable to map view %s and client %s", product, provisionRequestBody.getClientName());
                    log.error(message, e);
                    updateProvisionStatus(provisionalStatus, ProvisionStatus.PROVISIONED_WITH_ERRORS);
                }
            }
        }
        ;

        updateProvisionStatus(successCount, provisionalStatus);

    }

    private void updateProvisionStatus(int successCount, ProvisionalStatus provisionalStatus) {
        if (successCount > 0)
            updateProvisionStatus(provisionalStatus, ProvisionStatus.SUCCESSFULLY_PROVISIONED);
        else
            updateProvisionStatus(provisionalStatus, ProvisionStatus.ALREADY_PROVISIONED);
    }

    Optional<ClientViewMapping> buildClientViewMappingRequest(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) throws COBInternalException {
        return clientViewMapper.map(provisionRequestBody, defClientViewMap);
    }

}
