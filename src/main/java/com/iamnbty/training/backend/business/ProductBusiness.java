package com.iamnbty.training.backend.business;

import com.iamnbty.training.backend.exception.BaseException;
import com.iamnbty.training.backend.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductBusiness {

    public String getProductById(String id) throws BaseException {
        // TODO: get data from Database
        if (Objects.equals("1234", id)) {
            throw ProductException.notFound();
        }

        return id;
    }

}
