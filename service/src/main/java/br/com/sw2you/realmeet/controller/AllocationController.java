package br.com.sw2you.realmeet.controller;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import br.com.sw2you.realmeet.api.facade.AllocationsApi;
import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.service.AllocationService;
import br.com.sw2you.realmeet.util.ResponseEntityUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllocationController implements AllocationsApi {

    private final Executor controllersExecutor;
    private final AllocationService service;

    public AllocationController(Executor controllersExecutor, AllocationService service) {
        this.controllersExecutor = controllersExecutor;
        this.service = service;
    }

    @Override
    public CompletableFuture<ResponseEntity<AllocationDTO>> createAllocation(CreateAllocationDTO createAllocationDTO) {
        return supplyAsync(() -> service.createAllocation(createAllocationDTO), controllersExecutor).thenApply(ResponseEntityUtils::created);
    }

}