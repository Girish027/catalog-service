package com.tfs.dp2.catalog.controller;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.COBMalformedRequestException;
import com.tfs.dp2.catalog.baseexceptions.COBValidationException;
import com.tfs.dp2.catalog.cob.request.entity.handler.COBServiceHandler;
import com.tfs.dp2.catalog.cob.response.entity.COBResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/catalog/v1/")
public class COBController {

    @Autowired
    COBServiceHandler serviceHandler;

    @PostMapping(value = "/clients/{clientName}/accounts/{accountName}/validate")
    public COBResponse validateClientProvisionRequest(@PathVariable(value = "clientName") String clientName, @PathVariable(value = "accountName") String accountName, @Valid @RequestBody String body) throws COBValidationException, COBMalformedRequestException, COBInternalException {
        return serviceHandler.validateClientProvisionRequest(clientName, accountName, body);
    }

    @PostMapping(value = "/clients/{clientName}/accounts/{accountName}")
    public ResponseEntity<COBResponse> provisionClient(@PathVariable(value = "clientName") String clientName, @PathVariable(value = "accountName") String accountName, @Valid @RequestBody String body) throws COBMalformedRequestException, COBInternalException {
        log.info(String.format("Provision request payload:\n%s for client:%s and account:%s",body,clientName,accountName));
        return serviceHandler.provisionClient(clientName, accountName, body);
    }


}
