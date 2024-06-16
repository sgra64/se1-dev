package system.impl;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import datamodel.TAX;
import system.DataFactory;
import system.Repository;


class JSONDataFactories {

    interface JSONDataFactory<T, ID> {

        /**
         * Attempt to create object of type Customer from content of JSON node.
         * @param jn JSON node with content for object.
         * @return object, if mapped with success.
         */
        Optional<T> create(JsonNode jn);

    }

    JSONDataFactory<Customer, Long> JsonCustomerFactory(DataFactory<Customer, Long> dataFactory) {
        return new JSONDataFactory<Customer, Long>() {

            /**
             * Attempt to create object of type Customer from content of JSON node.
             * @param jn JSON node with content for object.
             * @return object, if mapped with success.
             */
            @Override
            public Optional<Customer> create(JsonNode jn) {
                Long id = Optional.ofNullable(jn.get("id")).map(jn2 -> jn2.asLong()).orElse(-1L);
                String name = Optional.ofNullable(jn.get("name")).map(jn2 -> jn2.asText()).orElse(null);
                //
                if(id >= 0 && name != null) {
                    Customer customer = dataFactory.create(id, name);
                    Optional.ofNullable(jn.get("contacts"))
                        .filter(ja -> ja.isArray())
                        .ifPresent(ja ->
                            ja.forEach(jn2 -> customer.addContact(jn2.asText()))
                        );
                    return Optional.of(customer);
                }
                return Optional.empty();
            }
        };
    }

    JSONDataFactory<Article, String> JsonArticleFactory(DataFactory<Article, String> dataFactory) {
        return new JSONDataFactory<Article, String>() {

            /**
             * Attempt to create object of type Article from content of JSON node.
             * @param jn JSON node with content for object.
             * @return object, if mapped with success.
             */
            @Override
            public Optional<Article> create(JsonNode jn) {
                String id = Optional.ofNullable(jn.get("id")).map(jn2 -> jn2.asText()).orElse(null);
                String description = Optional.ofNullable(jn.get("description")).map(jn2 -> jn2.asText()).orElse(null);
                long unitPrice = Optional.ofNullable(jn.get("price")).map(jn2 -> jn2.asLong()).orElse(-1L);
                //
                if(id != null && id.length() > 0 && description != null && unitPrice >= 0) {
                    //
                    String tax = Optional.ofNullable(jn.get("tax")).map(jn2 -> jn2.asText()).orElse("");
                    Article article = dataFactory.create(id, description)
                            .setUnitPrice(unitPrice);
                    if(tax.equals("reduced")) {
                        article.setTax(TAX.GER_VAT_REDUCED);
                    }
                    return Optional.of(article);
                }
                return Optional.empty();
            }
        };
    }

    JSONDataFactory<Order, String> JsonOrderFactory(
        DataFactory<Order, String> dataFactory,
        Repository<Customer, Long> customerRepository,
        Repository<Article, String> articleRepository)
    {
        return new JSONDataFactory<Order, String>() {

            /**
             * Attempt to create object of type Order from content of JSON node.
             * @param jn JSON node with content for object.
             * @return object, if mapped with success.
             */
            @Override
            public Optional<Order> create(JsonNode jn) {
                if(customerRepository!=null && articleRepository!=null) {
                    String id = Optional.ofNullable(jn.get("id")).map(jn2 -> jn2.asText()).orElse(null);
                    long customer_id = Optional.ofNullable(jn.get("customer_id")).map(jn2 -> jn2.asLong()).orElse(-1L);
                    Optional<Customer> copt = customerRepository.findById(customer_id);
                    boolean hasItems = Optional.ofNullable(jn.get("items")).map(ja -> ja.isArray() && ja.size() > 0).orElse(false);
                    //
                    if(id != null && id.length() > 0 && copt.isPresent() && hasItems) {
                        var order = dataFactory.create(id, copt);
                        jn.get("items").forEach(jn2 -> {
                            int units = Optional.ofNullable(jn2.get("units")).map(jn3 -> jn3.asInt()).orElse(-1);
                            Optional.ofNullable(jn2.get("article_id"))
                                .filter(jn3 -> units > 0)
                                .ifPresent(jn3 -> articleRepository.findById(jn3.asText())
                                    .ifPresent(a -> order.map(ord2 -> ord2.addItem(a, units)))
                                );
                        });
                        return order;
                    }
                } else {
                    System.out.println(this.getClass().getName() + "createOrder(JsonNode jn): customerRepository or articleRepository is null");
                }
                return Optional.empty();
            }
        };
    }
}