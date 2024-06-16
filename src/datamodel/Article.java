package datamodel;

/**
 * Class of entity type <i>Article</i>.
 * <p>
 * Articles can be referenced in orders as ordered items .
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Article {

    /**
     * Default constructor
     */
    public Article() {
    }

    /**
     * Unique article id, not null or empty "", id can be set only once
     */
    private String id = null;

    /**
     * Article description, never null, may be empty ""
     */
    private String description = "";

    /**
     * Price in cent per article, negative values are invalid
     */
    private long unitPrice = 0L;

    /**
     * Currency in which price is quoted, EUR is the default currency
     */
    private Currency currency = Currency.EUR;

    /**
     * Tax rate that applies to article, German regular Value-Added Tax (VAT) is default
     */
    private TAX taxRate = TAX.GER_VAT;


    /**
     * Constructor with description and price.
     * @param description article name or descriptive text of article
     * @param unitPrice price (in cent) for one unit of the article
     * @throws IllegalArgumentException when description is null or empty "" or price negative {@code < 0}.
     */
    public Article(String description, long unitPrice) {
        setDescription(description);
        setUnitPrice(unitPrice);
    }

    /**
     * Id getter.
     * @return article order id, returns {@code null}, if id is unassigned
     */
    public String getId() {
        return id;
    }

    /**
     * Id setter. Id can only be set once with valid id, id is immutable after assignment.
     * @param id only valid id (not null or "") updates id attribute on first invocation
     * @throws IllegalArgumentException if id argument is invalid ({@code id==null} or {@code id==""})
     * @return chainable self-reference
     */
    public Article setId(String id) {
        if(id==null || id.length()==0)
            throw new IllegalArgumentException("invalid id (null or \"\").");
        //
        // set id only once; id cannot be changed afterwards
        this.id = this.id==null? id : this.id;
        return this;
    }

    /**
     * Article description getter.
     * @return article description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Article description setter.
     * @param description article name or descriptive text of article
     * @throws IllegalArgumentException when description is null or empty ""
     * @return chainable self-reference
     */
    public Article setDescription(String description) {
        if(description==null || description.length()==0)
            throw new IllegalArgumentException("invalid description (null or \"\").");
        //
        this.description = description;
        return this;
    }

    /**
     * UnitPrice getter.
     * @return price in cent for one article unit
     */
    public long getUnitPrice() {
        return unitPrice;
    }

    /**
     * UnitPrice setter.
     * @param unitPrice price in cent for one article, only valid price ( {@code >= 0} ) updates attribute
     * @return chainable self-reference
     */
    public Article setUnitPrice(long unitPrice) {
        if(unitPrice < 0)
            throw new IllegalArgumentException("invalid unitPrice ( < 0).");
        //
        this.unitPrice = unitPrice;
        return this;
    }

    /**
     * Currency getter.
     * @return currency in which unitPrice is quoted.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Currency setter.
     * @param currency in which unitPrice is quoted
     * @throws IllegalArgumentException if currency is null
     * @return chainable self-reference
     */
    public Article setCurrency(Currency currency) {
        if(currency==null)
            throw new IllegalArgumentException("invalid currency (null).");
        //
        this.currency = currency;
        return this;
    }

    /**
     * TAX rate getter.
     * @return tax rate applicable for article
     */
    public TAX getTax() {
        return taxRate;
    }

    /**
     * TAX rate setter.
     * @param tax rate that applies to article
     * @throws IllegalArgumentException if tax is null
     * @return chainable self-reference
     */
    public Article setTax(TAX tax) {
        if(tax==null)
            throw new IllegalArgumentException("invalid tax (null).");
        //
        this.taxRate = tax;
        return this;
    }
}