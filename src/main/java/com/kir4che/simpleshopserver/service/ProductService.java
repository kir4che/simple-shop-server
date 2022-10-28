package com.kir4che.simpleshopserver.service;

import com.kir4che.simpleshopserver.dto.ProductQueryParams;
import com.kir4che.simpleshopserver.dto.ProductRequest;
import com.kir4che.simpleshopserver.model.Product;

import java.util.List;

public interface ProductService {
    Integer countProduct(ProductQueryParams productQueryParams);
    Product getProduct(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);
}
