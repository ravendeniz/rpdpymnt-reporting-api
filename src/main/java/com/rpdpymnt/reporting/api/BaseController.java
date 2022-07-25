package com.rpdpymnt.reporting.api;

import org.modelmapper.ModelMapper;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class BaseController {

    static final String CORE = "core";
    static final String USERS = "users";
    static final String TRANSACTION = "transaction";
    static final String CLIENT = "client";
    public static final String BASE_PATH = "/api/";
    static final String JSON = APPLICATION_JSON_VALUE;
    static final String TOKEN_HEADER = "authorization";
    static final String AUTH_USER_ID = "authuserid";
    static final String HAS_ADMINISTRATOR_ROLE = "hasRole('ROLE_ADMINISTRATOR')";
    static final String HAS_ADMIN_OR_SERVICE_ROLE = "hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_SERVICE')";

    protected static final String SUCCESSFUL_OPERATION = "Successful operation.";
    protected static final String INVALID_PARAMETERS = "Invalid parameters.";
    protected static final String NOT_FOUND = "Not found.";
    protected static final String FORBIDDEN = "Forbidden";
    protected static final String UNAUTHORISED = "Unauthorised.";

    private final ModelMapper mapper;

    public BaseController(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public <S, D> D map(S sourceInstance, Class<D> destinationTypeClass) {
        if (!ObjectUtils.isEmpty(mapper)
                && !ObjectUtils.isEmpty(mapper.getConfiguration())) {
            mapper.getConfiguration().setAmbiguityIgnored(true);
        }
        return mapper.map(sourceInstance, destinationTypeClass);
    }

    public <S, D> D map(S sourceInstance, Type destinationTypeClass) {
        if (!ObjectUtils.isEmpty(mapper)
                && !ObjectUtils.isEmpty(mapper.getConfiguration())) {
            mapper.getConfiguration().setAmbiguityIgnored(true);
        }
        return mapper.map(sourceInstance, destinationTypeClass);
    }

    <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        if (!ObjectUtils.isEmpty(mapper)
                && !ObjectUtils.isEmpty(mapper.getConfiguration())) {
            mapper.getConfiguration().setAmbiguityIgnored(true);
        }
        return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
    }

}
