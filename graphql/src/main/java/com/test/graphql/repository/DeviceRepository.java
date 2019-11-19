package com.test.graphql.repository;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Device;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
public interface DeviceRepository {
    List<Device> getDevicesByDeviceIds(Set<Long> deviceIds);
}
