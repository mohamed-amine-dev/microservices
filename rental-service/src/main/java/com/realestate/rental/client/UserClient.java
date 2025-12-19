package com.realestate.rental.client;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.common.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/profile/{id}")
    ResponseWrapper<UserResponse> getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/email/{email}")
    ResponseWrapper<UserResponse> getUserByEmail(@PathVariable("email") String email);
}
