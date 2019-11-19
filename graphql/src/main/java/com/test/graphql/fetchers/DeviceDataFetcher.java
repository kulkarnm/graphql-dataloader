package com.test.graphql.fetchers;

import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import com.test.graphql.entity.Device;

import graphql.schema.DataFetchingEnvironment;

@Component
public class DeviceDataFetcher {
    public CompletableFuture<Device> getDevice(DataFetchingEnvironment env) {
        Long deviceId = Long.parseLong(env.getArgument("deviceId"));

        DataLoader<Long, Device> deviceDataLoader = env.getDataLoader("DeviceLoader");

        return deviceDataLoader.load(deviceId);
    }
} 
