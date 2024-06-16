package system.impl;

import java.util.Optional;

import datamodel.*;
import system.DataFactory;


class DataFactories {

    /**
     * internal Customer-, Article- and Order-Factories that implement
     * DataFactory<T, ID> interfaces.
     */
    private final DataFactory<Customer, Long> customerFactory;
    private final DataFactory<Article, String> articleFactory;
    private final DataFactory<Order, String> orderFactory;

    DataFactories() {
          this.customerFactory = new DataFactory<>() {
          @Override public Customer create(Long id, String name) {
              Customer customer = new Customer(name).setId(id);
              return customer;
          }

        @Override
        public Optional<Customer> create(Long id, Optional<Customer> customer) {
            throw new UnsupportedOperationException("method not supported for type Customer");
        }
        };

        this.articleFactory = new DataFactory<>() {
          @Override public Article create(String id, String name) {
              Article article = new Article().setId(id);
              return article.setDescription(name);
          }
        
          @Override public Optional<Article> create(String id, Optional<Customer> customer) {
              throw new UnsupportedOperationException("method not supported for type Article");
          }
        };

        this.orderFactory = new DataFactory<>() {
          @Override public Order create(String id, String name) {
              throw new UnsupportedOperationException("method not supported for type Order");
          }
        
          @Override public Optional<Order> create(String id, Optional<Customer> customer) {
              if(customer.isPresent()) {
                  Order order = new Order(customer.get()).setId(id);
                  return Optional.of(order);
              }
              return Optional.empty();
          }
        };
    }

    DataFactory<Customer, Long> customerFactory() {
        return customerFactory;
    }
    
    DataFactory<Article, String> articleFactory() {
        return articleFactory;
    }
    
    DataFactory<Order, String> orderFactory() {
        return orderFactory;
    }
}