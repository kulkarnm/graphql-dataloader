package com.test.graphql.fetchers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;

@Component
public class RootDataFetcher {

	@Autowired
    private CustomerDataFetcher customerDataFetcher;
    @Autowired
    private AccountDataFetcher accountDataFetcher;
    @Autowired
    private DeviceDataFetcher deviceDataFetcher;
    
    
    public DataFetcher getAccountDataFetcher() {
    	return environment -> accountDataFetcher.getAccount(environment);
    }
    
    public DataFetcher getCustomerDataFetcher() {
    	return environment -> customerDataFetcher.getCustomer(environment);
    }
    
    public DataFetcher getDeviceDataFetcher() {
    	return environment -> deviceDataFetcher.getDevice(environment);
    }
}
