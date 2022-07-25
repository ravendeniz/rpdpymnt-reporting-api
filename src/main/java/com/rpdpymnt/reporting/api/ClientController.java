package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.ClientResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.rpdpymnt.reporting.api.BaseController.BASE_PATH;
import static com.rpdpymnt.reporting.api.BaseController.CLIENT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Api(tags = CLIENT, description = "Transaction service")
@RequestMapping(path = BASE_PATH)
@Controller
@Validated
public class ClientController extends BaseController {

    private final ClientService ClientService;

    public ClientController(ModelMapper mapper, com.rpdpymnt.reporting.service.ClientService clientService) {
        super(mapper);
        ClientService = clientService;
    }

    @ApiOperation(value = "Transaction Report (ALL USERS)", tags = {USERS})
    @ApiResponses({@ApiResponse(code = SC_OK, message = SUCCESSFUL_OPERATION),
            @ApiResponse(code = SC_BAD_REQUEST, message = INVALID_PARAMETERS)})
    @PostMapping(value = "/client", produces = JSON)
    public ResponseEntity<?> client(
            @RequestBody TransactionRequest transactionRequest) {
        ClientResponse clientResponse = ClientService.getClientInfo(transactionRequest);
        return ResponseEntity.ok(clientResponse);
    }
}
