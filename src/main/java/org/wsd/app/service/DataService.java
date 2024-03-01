package org.wsd.app.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class DataService {

    @Cacheable(value = "dataCache")
    public List<String> getData() {
        System.out.println("Fetching data from method");
        // Simulate fetching data from a database or external service
        return Arrays.asList("Data1", "Data2", "Data3");
    }

    public void clearCache() {
        // This method will clear the cache for the dataCache cache
    }
}
