package com.test.graphql.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.graphql.entity.Device;
import com.test.graphql.repository.DeviceRepository;

@Component
public class DeviceBatchDataLoader {

    @Autowired
    private DeviceRepository deviceRepository;

    public MappedBatchLoaderWithContext<Long, Device> deviceBatchLoader = new MappedBatchLoaderWithContext<Long, Device>() {
        @Override
        public CompletionStage<Map<Long, Device>> load(Set<Long> keys, BatchLoaderEnvironment env) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        Map<Long, Device> results = new HashMap<>();
                        List<Device> deviceList = deviceRepository.getDevicesByDeviceIds(keys);

                        if (null == deviceList || deviceList.size() == 0) {
                            return results;
                        }

                        for (Device device : deviceList) {
                            results.put(device.getId(), device);
                        }
                        return results;
                    });
        }
    };
} 
