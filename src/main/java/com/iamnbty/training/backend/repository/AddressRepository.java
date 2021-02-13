package com.iamnbty.training.backend.repository;

import com.iamnbty.training.backend.entity.Address;
import com.iamnbty.training.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findByUser(User user);

}