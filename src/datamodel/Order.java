package datamodel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * Class of entity type <i>Order</i>.
 * <p>
 * Order defines a relationship between a Customer and the seller over items  to purchase.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Order {

    /**
     * Unique id, null or "" are invalid values, id can be set only once.
     */
    private String id = null;

    /**
     * Reference to owning Customer, final, never null.
     */
    private final Customer customer;

    /**
     * Date/time the order was created.
     */
    private Date created;

    /**
     * Items that are ordered as part of this order.
     */
    private final List<OrderItem> items = new ArrayList<>();


    /**
     * Constructor with customer owning the order.
     * @param customer customer who ows the order
     * @throws IllegalArgumentException if customer argument is null or has no valid id
     */
    public Order(Customer customer) {
        if(customer==null)
            throw new IllegalArgumentException("Customer empty.");
        if(customer.getId()==null || customer.getId() < 0L)
            throw new IllegalArgumentException("Customer has invalid id.");
        //
        this.customer = customer;
        this.created = new Date();
    }

    /**
     * Id getter.
     * @return order id, returns {@code null}, if id is unassigned
     */
    public String getId() {
        return id;
    }

    /**
     * Id setter. Id can only be set once with valid id, id is immutable after assignment.
     * @param id only a valid id (not null or "") sets the id attribute on first invocation
     * @throws IllegalArgumentException if id argument is invalid ({@code id==null} or {@code id==""})
     * @return chainable self-reference.
     */
    public Order setId(String id) {
        if(id==null || id.length()==0)
            throw new IllegalArgumentException("invalid id (null or \"\").");
        //
        // set id only once; id cannot be changed afterwards
        this.id = this.id==null? id : this.id;
        return this;
    }

    /**
     * Customer getter.
     * @return owning customer, cannot be null.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * CreationDate getter, returns the time/date when the order was created.
     * @return time/date when order was created as long in ms since 01/01/1970
     */
    public long getCreationDate() {
        return created.getTime();
    }

    /**
     * CreationDate setter for date/time in a valid range of {@code 01/01/2020, 00:00:00 <= datetime <= today +1 day, 23:59:59}.
     * 
     * Attemting to set createDate older than the lower bound {@code 01/01/2020, 00:00:00} or past the upper bound {@code today +1 day, 23:59:59} will raise an IllegalArgumentException.
     * @param datetime time/date when order was created (in milliseconds since 01/01/1970)
     * @throws IllegalArgumentException if datetime is outside the valid range {@code 01/01/2020, 00:00:00 <= datetime <= today +1 day, 23:59:59}
     * @return chainable self-reference
     */
    public Order setCreationDate(long datetime) {
        Instant dti = Instant.ofEpochMilli(datetime);
        LocalDateTime ldt = LocalDateTime.ofInstant(dti, ZoneOffset.UTC);
        // if(datetime < lowerBound || datetime > upperBound)
        if(ldt.isBefore(lowerLDT) || ldt.isAfter(upperLDT))
            throw new IllegalArgumentException("invalid datetime argument (outside bounds 01/01/2020, 00:00:00 <= datetime <= today +1 day, 23:59:59).");
        //
        created.setTime(datetime);
        return this;
    }

    /**
     * Number of items that are part of the order.
     * @return number of items ordered
     */
    public int itemsCount() {
        return items.size();
    }

    /**
     * Ordered items getter.
     * @return immutable collection of ordered items
     */
    public Iterable<OrderItem> getItems() {
        return items;
    }

    /**
     * Create new item and add to order.
     * @param article ordered article
     * @param units units ordered
     * @throws IllegalArgumentException if article is null or units not a positive {@code units > 0} number
     * @return chainable self-reference
     */
    public Order addItem(Article article, int units) {
        if(article==null)
            throw new IllegalArgumentException("article is null.");
        //
        if(units <= 0)
            throw new IllegalArgumentException("units <= 0.");
        //
        OrderItem item = new OrderItem(article, units);
        items.add(item);
        return this;
    }

    /**
     * Delete i-th item from order, {@code i >= 0 && i < items.size()}, otherwise method has no effect
     * @param i index of item to delete, only a valid index deletes item otherwise method has no effect
     */
    public void deleteItem(int i) {
        if(i >= 0 && i < items.size()) {
            items.remove(i);
        }
    }

    /**
     * Delete all ordered items.
     */
    public void deleteAllItems() {
        items.clear();
    }


    /**
     * Bounds for valid Order creation date. An Order must be created after {@code 01/01/2020, 00:00:00}
     * and before {@code today +1 day, 23:59:59}.
     */
    static final Instant instant = Instant.now();
    static final LocalDateTime nowLDT = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    // lower bound of valid order creation date-time is 01/01/2020, 00:00:00
    static final LocalDateTime lowerLDT = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    static final LocalDateTime upperLDT = LocalDateTime.of(
            nowLDT.get(ChronoField.YEAR),
            nowLDT.get(ChronoField.MONTH_OF_YEAR),
            nowLDT.get(ChronoField.DAY_OF_MONTH),
            23, 59, 59)
        //
        .plusDays(1);   // upper bound is tomorrow 23:59:59
}