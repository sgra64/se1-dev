package datamodel;

/**
 * Class of a line item referring to an article that is part of an Order. Orders may have multiple order items.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class OrderItem {

    /**
     * Ordered article, is never null.
     */
    private final Article article;

    /**
     * Number of units ordered, is always a positive number {@code > 0}.
     */
    private int unitsOrdered;


    /**
     * Constructor of ordered line item with article and units arguments.
     * @param article ordered article
     * @throws IllegalArgumentException if article is null
     * @param unitsOrdered number of articles ordered
     * @throws IllegalArgumentException if {@code units <= 0}
     */
    public OrderItem(Article article, int unitsOrdered) {
        if(article==null)
            throw new IllegalArgumentException("article is null.");
        //
        this.article = article;
        setUnitsOrdered(unitsOrdered);
    }

    /**
     * Article getter.
     * @return article ordered with this OrderItem
     */
    public Article getArticle() {
        return article;
    }

    /**
     * units ordered getter.
     * @return units of article ordered with this OrderItem
     */
    public int getUnitsOrdered() {
        return unitsOrdered;
    }

    /**
     * Update units of an article ordered.
     * @param units updated number of units ordered
     * @throws IllegalArgumentException if {@code units <= 0}
     */
    public void setUnitsOrdered(int units) {
        if(units <= 0)
            throw new IllegalArgumentException("units <= 0.");
        //
        this.unitsOrdered = units;
    }
}