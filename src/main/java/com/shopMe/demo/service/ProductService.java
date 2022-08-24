package com.shopMe.demo.service;


import com.shopMe.demo.dto.product.ProductDto;
import com.shopMe.demo.exceptions.ProductNotExistException;
import com.shopMe.demo.model.Category;
import com.shopMe.demo.model.Product;
import com.shopMe.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDto> listProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products) {
            productDtos.add(new ProductDto(product));
        }
        return productDtos;
    }

    public static Product getProductFromDto(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        return product;
    }


    public void addProduct(ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public void updateProduct(Integer productID, ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        product.setId(productID);
        productRepository.save(product);
    }


    public Product getProductById(Integer productId) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new ProductNotExistException("Product id is invalid " + productId);
        return optionalProduct.get();
    }



        public Optional<Product> readCategory(Integer categoryId) {
            return productRepository.findById(categoryId);
        }

    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }


    public List<ProductDto> searchProduct(String productName) {
        List<Product> results = productRepository.findByProductName(productName);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : results) {
            productDtos.add(new ProductDto(product));
        }
        return productDtos;
    }
}
