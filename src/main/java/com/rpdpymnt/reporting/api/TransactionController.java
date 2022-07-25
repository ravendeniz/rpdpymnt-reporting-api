package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.ReportRequest;
import com.rpdpymnt.reporting.dto.ReportResponse;
import com.rpdpymnt.reporting.dto.TransactionListRequest;
import com.rpdpymnt.reporting.dto.TransactionListResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.dto.TransactionResponse;
import com.rpdpymnt.reporting.service.TransactionService;
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
import static com.rpdpymnt.reporting.api.BaseController.TRANSACTION;
import static javax.servlet.http.HttpServletResponse.*;

@Api(tags = TRANSACTION, description = "Transaction service")
@RequestMapping(path = BASE_PATH)
@Controller
@Validated
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    public TransactionController(ModelMapper mapper, TransactionService transactionService) {
        super(mapper);
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "Transaction Report (ALL USERS)", tags = {USERS})
    @ApiResponses({@ApiResponse(code = SC_OK, message = SUCCESSFUL_OPERATION),
            @ApiResponse(code = SC_BAD_REQUEST, message = INVALID_PARAMETERS)})
    @PostMapping(value = "/transactions/report", produces = JSON)
    public ResponseEntity<?> transactionsReport(
            @RequestBody ReportRequest reportRequest) {
        ReportResponse reportResponse = transactionService.getReport(reportRequest);
        return ResponseEntity.ok(reportResponse);
    }

    @ApiOperation(value = "Transaction List (ALL USERS)", tags = {USERS})
    @ApiResponses({@ApiResponse(code = SC_OK, message = SUCCESSFUL_OPERATION),
            @ApiResponse(code = SC_BAD_REQUEST, message = INVALID_PARAMETERS)})
    @PostMapping(value = "/transaction/list", produces = JSON)
    public ResponseEntity<?> transactionList(
            @RequestBody TransactionListRequest transactionListRequest) {
        TransactionListResponse transactionListResponse = transactionService.getTransactionList(transactionListRequest);
        return ResponseEntity.ok(transactionListResponse);
    }

    @ApiOperation(value = "Transaction List (ALL USERS)", tags = {USERS})
    @ApiResponses({@ApiResponse(code = SC_OK, message = SUCCESSFUL_OPERATION),
            @ApiResponse(code = SC_BAD_REQUEST, message = INVALID_PARAMETERS)})
    @PostMapping(value = "/transaction", produces = JSON)
    public ResponseEntity<?> transaction(
            @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.getTransaction(transactionRequest);
        return ResponseEntity.ok(transactionResponse);
    }
}
