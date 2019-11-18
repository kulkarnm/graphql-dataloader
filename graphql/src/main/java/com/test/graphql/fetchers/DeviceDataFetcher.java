package com.test.graphql.fetchers;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Device;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DeviceDataFetcher {
    public CompletableFuture<Device> getDevice(DataFetchingEnvironment env) {
        Long deviceId = Long.parseLong(env.getArgument("deviceId"));

        DataLoader<Long, Device> deviceDataLoader = env.getDataLoader("deviceDataLoader");

        return deviceDataLoader.load(deviceId);
    }
} 
