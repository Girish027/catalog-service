package com.tfs.dp2.catalog.cob.onboarder;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.COBResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public abstract class PipelineOnBoarder {

    protected Optional<PipelineOnBoarder> nextChain = Optional.empty();

    public void setNextChain(PipelineOnBoarder pipelineOnboarder) {
        this.nextChain = Optional.of(pipelineOnboarder);
    }

    private void forwardToNextChain(ValidateRequestBody validateRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException {
        if (nextChain.isPresent()) {
            nextChain.get().onBoardChain(validateRequestBody, provisionalStatus);
        }
    }

    public ProvisionStatus onBoard(ValidateRequestBody provisionRequestBody) throws COBInternalException {
        if (provisionRequestBody == null)
            throw new IllegalArgumentException("provisionRequestBody or errorResponseBuilder cannot be null.");
        ProvisionalStatus provisionalStatus = new ProvisionalStatus();
        onBoardChain(provisionRequestBody, provisionalStatus);
        return provisionalStatus.getProvisionStatus();
    }

    private void onBoardChain(ValidateRequestBody provisionRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException {
        forwardToNextChain(provisionRequestBody, onBoardRequest(provisionRequestBody, provisionalStatus));
    }

    abstract protected ProvisionalStatus onBoardRequest(ValidateRequestBody validateRequestBody, ProvisionalStatus provisionalStatus) throws COBInternalException;

    public enum ProvisionStatus {
        ALREADY_PROVISIONED {
            public ResponseEntity<COBResponse> getResponse() {
                COBResponse cobResponse = new COBResponse(SUCCESS_MESSAGE);
                return formResponse(cobResponse, HttpStatus.OK);
            }
        },
        SUCCESSFULLY_PROVISIONED {
            public ResponseEntity<COBResponse> getResponse() {
                COBResponse cobResponse = new COBResponse(SUCCESS_MESSAGE);
                return formResponse(cobResponse, HttpStatus.CREATED);
            }
        },
        PROVISIONED_WITH_ERRORS {
            public ResponseEntity<COBResponse> getResponse() {
                COBExceptionResponse cobExceptionResponse = new COBExceptionResponse(FAILURE_MESSAGE, COBExceptionResponse.Code.NO_RETRY.name());
                return formResponse(cobExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };

        private static ResponseEntity<COBResponse> formResponse(COBResponse cobResponse, HttpStatus httpStatus) {
            ResponseEntity<COBResponse> cobResponseResponseEntity = new ResponseEntity<>(cobResponse, httpStatus);
            log.info(String.format("Response code:%s, Response body:%s", cobResponseResponseEntity.getStatusCode(), cobResponseResponseEntity.getBody().getMessage()));
            return cobResponseResponseEntity;
        }

        private static final String SUCCESS_MESSAGE = "Operation completed successfully.";
        private static final String FAILURE_MESSAGE = "Unable to provision COB request.";

        public abstract ResponseEntity<COBResponse> getResponse();
    }

    protected void updateProvisionStatus(ProvisionalStatus provisionalStatus, ProvisionStatus newProvisionStatus) {
        if (provisionalStatus.getProvisionStatus() == ProvisionStatus.PROVISIONED_WITH_ERRORS)
            return;
        if (provisionalStatus.getProvisionStatus() == ProvisionStatus.SUCCESSFULLY_PROVISIONED)
            return;
        provisionalStatus.setProvisionStatus(newProvisionStatus);
    }

    protected void logOnBoardingMessage(String key, ValidateRequestBody provisionRequestBody) {
        log.info(String.format("Onboarding %s for client %s, account %s, product %s", key, provisionRequestBody.getClientName(), provisionRequestBody.getAccountName(), provisionRequestBody.getCobValidateRequestBody().get().getProduct()));
    }

    protected class ProvisionalStatus {

        private ProvisionStatus provisionStatus = ProvisionStatus.ALREADY_PROVISIONED;

        public ProvisionStatus getProvisionStatus() {
            return provisionStatus;
        }

        public void setProvisionStatus(ProvisionStatus provisionStatus) {
            this.provisionStatus = provisionStatus;
        }
    }

}
