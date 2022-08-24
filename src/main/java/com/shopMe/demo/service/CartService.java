package com.shopMe.demo.service;

import com.shopMe.demo.dto.cart.AddToCartDto;
import com.shopMe.demo.dto.cart.CartDto;
import com.shopMe.demo.dto.cart.CartItemDto;
import com.shopMe.demo.exceptions.CartItemNotExistException;
import com.shopMe.demo.model.Cart;
import com.shopMe.demo.model.Product;
import com.shopMe.demo.model.User;
import com.shopMe.demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public void addToCart(AddToCartDto addToCartDto, Product product, User user) {

        Cart list = cartRepository.findByProductId(product.getId());
        if(Objects.nonNull(list)){
            list.setQuantity(list.getQuantity()+ addToCartDto.getQuantity());
        cartRepository.save(list);
        } else {
            Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
            cartRepository.save(cart);
        }

    }

    public CartDto listCartItems(User user) {
        // first get all the cart items for user
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        // convert cart to cartItemDto
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartItemDto cartItemDto = new CartItemDto(cart);
            cartItems.add(cartItemDto);
        }

        // calculate the total price
        double totalCost = 0;
        for (CartItemDto cartItemDto :cartItems){
            totalCost += cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity();
        }

        // return cart DTO
        return new CartDto(cartItems,totalCost);
    }
    public static CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }

    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

    public void updateCartItem(AddToCartDto cartDto, User user,Product product){
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(int id,int userId) throws CartItemNotExistException {
        if (!cartRepository.existsById(id))
            throw new CartItemNotExistException("Cart id is invalid : " + id);
        cartRepository.deleteById(id);

    }

    public void deleteCartItems(int userId) {
        cartRepository.deleteAll();
    }


    public void updateQuantity(AddToCartDto addToCartDto, Product product, User user) {
        Cart list = cartRepository.findByProductId(product.getId());
            list.setQuantity(addToCartDto.getQuantity());
            cartRepository.save(list);

    }
}
