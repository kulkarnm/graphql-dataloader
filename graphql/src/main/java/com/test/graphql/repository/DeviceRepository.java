package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.entity.Device;
public interface DeviceRepository {
    List<Device> getDevicesByDeviceIds(Set<Long> deviceIds, GraphQLContext context);
}
