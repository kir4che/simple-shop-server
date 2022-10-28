package com.kir4che.simpleshopserver.controller;

import com.kir4che.simpleshopserver.constant.ProductCategory;
import com.kir4che.simpleshopserver.dto.ProductQueryParams;
import com.kir4che.simpleshopserver.dto.ProductRequest;
import com.kir4che.simpleshopserver.model.Product;
import com.kir4che.simpleshopserver.service.ProductService;
import com.kir4che.simpleshopserver.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 查詢商品
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProduct(productId);

        // 檢查該商品是否存在
        if (product != null) return ResponseEntity.status(HttpStatus.OK).body(product);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 查詢商品列表：分類、關鍵字搜尋、排序
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(required = false) ProductCategory category,
                                                           @RequestParam(required = false) String search,
                                                           @RequestParam(defaultValue = "created_date") String orderBy,
                                                           @RequestParam(defaultValue = "desc") String sort,
                                                           @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
                                                           @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy); // 要依什麼排序
        productQueryParams.setSort(sort); // 升序、降序
        productQueryParams.setLimit(limit); // 限制回傳的資料筆數
        productQueryParams.setOffset(offset); // 回傳資料列前跳過多少資料列

        // 取得 Product List
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得 Product 總數
        Integer total = productService.countProduct(productQueryParams);

        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    // 新增商品
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProduct(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // 修改商品
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {

        Product product = productService.getProduct(productId);

        // 修改商品的數據
        productService.updateProduct(productId, productRequest);

        Product updatedProduct = product;

        // 檢查該商品是否存在
        if (product != null) return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 刪除商品
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);

        // 回傳一個 ResponseEntity，其 http status code 會是 204 No Content，表示該數據已被刪掉。
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
