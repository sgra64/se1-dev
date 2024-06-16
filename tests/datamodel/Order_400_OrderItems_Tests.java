package datamodel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;


/**
 * Tests for Order class: [400..499] OrderItems tests.
 * 
 * @author sgra64
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Order_400_OrderItems_Tests {
    //
    final Customer c1 = new Customer().setId(1L);
    final datamodel.Order o1 = new datamodel.Order(c1);
    final Article a1 = new Article("Article_1", 100).setId("SKU00001");
    final Article a2 = new Article("Article_2", 100).setId("SKU00002");
    final Article a3 = new Article("Article_3", 100).setId("SKU00003");
    //
    final datamodel.Order o3 = new datamodel.Order(c1)
        .addItem(a1, 1)
        .addItem(a2, 2)
        .addItem(a3, 3);


    /*
     * Test case: add order items, regular case.
     */
    @Test @Order(400)
    void test400_addItems() {
        assertEquals(0, o1.itemsCount());
        o1.addItem(a1, 1);
        assertEquals(1, o1.itemsCount());
        o1.addItem(a1, 1);
        assertEquals(2, o1.itemsCount());
    }


    /*
     * Test case: get items in order of addition, regular case.
     */
    @Test @Order(410)
    void test410_getItemsInOrder() {
        assertEquals(o3.itemsCount(), 3);
        List<Article> articles = List.of(a1, a2, a3);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(articles.get(i), item.getArticle());
        }
        assertEquals(i, o3.itemsCount());
    }


    /*
     * Test case: delete items, regular case.
     */
    @Test @Order(420)
    void test420_deleteItemsFirst() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(0);   // delete first item
        List<Article> articles = List.of(a2, a3);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(item.getArticle(), articles.get(i));
        }
        assertEquals(2, i);
    }

    @Test @Order(421)
    void test421_deleteItemsLast() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(1);   // delete second item
        List<Article> articles = List.of(a1, a3);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(item.getArticle(), articles.get(i));
        }
        assertEquals(2, i);
    }

    @Test @Order(422)
    void test422_deleteItemsMiddle() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(2);   // delete last item
        List<Article> articles = List.of(a1, a2);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(item.getArticle(), articles.get(i));
        }
        assertEquals(2, i);
    }

    @Test @Order(423)
    void test423_deleteItemsFirstAndLast() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(0);   // delete first and last item
        o3.deleteItem(1);   // [1] is now last
        List<Article> articles = List.of(a2);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(item.getArticle(), articles.get(i));
        }
        assertEquals(1, i);
    }

    @Test @Order(424)
    void test424_deleteItemsAll() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(2);   // delete all items, last first
        o3.deleteItem(1);
        o3.deleteItem(0);
        assertEquals(0, o3.itemsCount());   // count is zero
        assertFalse(o3.getItems().iterator().hasNext());    // list is empty
        // reload and
        o3.addItem(a1, 1)
            .addItem(a2, 2)
            .addItem(a3, 3);
        o3.deleteItem(0);   // delete all items starting with first
        o3.deleteItem(0);
        o3.deleteItem(0);
        assertEquals(0, o3.itemsCount());   // count is zero
        assertFalse(o3.getItems().iterator().hasNext());    // list is empty
    }


    /*
     * Test case: delete items, irregular out-of-bounds cases.
     */
    @Test @Order(430)
    void test430_deleteItemsIrregularCases() {
        assertEquals(o3.itemsCount(), 3);
        o3.deleteItem(-1);  // out-of-bounds case, ignored
        o3.deleteItem(Integer.MAX_VALUE);
        o3.deleteItem(Integer.MIN_VALUE);
        o3.deleteItem(o3.itemsCount());
        //
        // items should be unchanged
        assertEquals(3, o3.itemsCount());
        List<Article> articles = List.of(a1, a2, a3);
        var it = o3.getItems().iterator();
        int i = 0;
        for( ; it.hasNext(); i++) {
            var item = it.next();
            assertEquals(item.getArticle(), articles.get(i));
        }
        assertEquals(i, o3.itemsCount());
    }


    /*
     * Test case: delete all items, regular case.
     */
    @Test @Order(440)
    void test440_deleteAllItems() {
        assertEquals(3, o3.itemsCount());
        o3.deleteAllItems();
        assertEquals(0, o3.itemsCount());   // count is zero
        assertFalse(o3.getItems().iterator().hasNext());    // list is empty
    }
}
