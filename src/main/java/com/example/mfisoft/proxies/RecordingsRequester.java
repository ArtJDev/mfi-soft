package com.example.mfisoft.proxies;

import com.example.mfisoft.models.Recordings;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "recordings", url = "${recordings.url}")
public interface RecordingsRequester {
    @GetMapping
    List<Recordings> getRecords(@SpringQueryMap Params params);
}
