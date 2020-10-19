//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: (P3 BOOKHashTable)
// Course: (CS 400, Fall 2019)
//
// Author: (Shaurya Kethireddy)
// Email: (skethireddy@wisc.edu)
// Lecturer's Name: (Debra Deppeler)
// Description: This project is a hashtable which is implemented with arraylists
// in an arraylist with time complexitity O(n)
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test HashTable class implementation to ensure that required functionality works for all cases.
 */
public class BookHashTableTest {

  // Default name of books data file
  public static final String BOOKS = "books_clean.csv";

  // Empty hash tables that can be used by tests
  static BookHashTable bookObject;
  static ArrayList<Book> bookTable;

  static final int INIT_CAPACITY = 2;
  static final double LOAD_FACTOR = 0.49;

  static Random RNG = new Random(0); // seeded to make results repeatable (deterministic)

  /** Create a large array of keys and matching values for use in any test */
  @BeforeAll
  public static void beforeClass() throws Exception {
    bookTable = BookParser.parse(BOOKS);
  }

  /** Initialize empty hash table to be used in each test */
  @BeforeEach
  public void setUp() throws Exception {
    // TODO: change HashTable for final solution
    bookObject = new BookHashTable(INIT_CAPACITY, LOAD_FACTOR);
  }

  /** Not much to do, just make sure that variables are reset     */
  @AfterEach
  public void tearDown() throws Exception {
    bookObject = null;
  }

  private void insertMany(ArrayList<Book> bookTable)
      throws IllegalNullKeyException, DuplicateKeyException {
    for (int i = 0; i < bookTable.size(); i++) {
      bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
    }
  }

  /** IMPLEMENTED AS EXAMPLE FOR YOU
   * Tests that a HashTable is empty upon initialization
   */
  @Test
  public void test000_collision_scheme() {
    if (bookObject == null)
      fail("Gg");
    int scheme = bookObject.getCollisionResolutionScheme();
    if (scheme < 1 || scheme > 9)
      fail("collision resolution must be indicated with 1-9");
  }


  /** IMPLEMENTED AS EXAMPLE FOR YOU
   * Tests that a HashTable is empty upon initialization
   */
  @Test
  public void test000_IsEmpty() {
    // "size with 0 entries:"
    assertEquals(0, bookObject.numKeys());
  }

  /** IMPLEMENTED AS EXAMPLE FOR YOU
   * Tests that a HashTable is not empty after adding one (key,book) pair
   * @throws DuplicateKeyException 
   * @throws IllegalNullKeyException 
   */
  @Test
  public void test001_IsNotEmpty() throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    String expected = "" + 1;
    // "size with one entry:"

    assertEquals(expected, "" + bookObject.numKeys());
  }

  /** IMPLEMENTED AS EXAMPLE FOR YOU 
  * Test if the hash table  will be resized after adding two (key,book) pairs
  * given the load factor is 0.49 and initial capacity to be 2.
  */
  @Test
  public void test002_Resize() throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    int cap1 = bookObject.getCapacity();
    bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
    int cap2 = bookObject.getCapacity();
    // "size with one entry:"
    assertTrue(cap2 > cap1 & cap1 == 2);
  }

  /**
   * this tests adding 5 books, removing 2 book, and then adding 8 and at between each operation
   * it checks if the number of keys in hashtable are correct
   * @throws DuplicateKeyException 
   * @throws IllegalNullKeyException 
   */
  @Test
  public void test003_insert5_remove2_insert8_checkKeys() {
    for (int i = 0; i < 5; i++) { // add 5 elements
      try {
        bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
      } catch (IllegalNullKeyException | DuplicateKeyException e) {
        fail("unnecessary exception thrown");
      }
    }
    if (bookObject.numKeys() != 5) // check keys
      fail("numKeys was supposed to be 5 but was " + bookObject.numKeys());

    for (int i = 0; i < 2; i++) { // add two elements
      try {
        bookObject.remove(bookTable.get(i).getKey());
      } catch (IllegalNullKeyException e) {
        fail("unnecessary exception thrown");
      }
    }
    if (bookObject.numKeys() != 3) // check keys
      fail("numKeys was supposed to be 3 but was " + bookObject.numKeys());

    for (int i = 9; i < 17; i++) { // add eight elements
      try {
        bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
      } catch (IllegalNullKeyException | DuplicateKeyException e) {
        fail("unnecessary exception thrown");
      }
    }

    if (bookObject.numKeys() != 11) // check keys
      fail("numKeys was supposed to be 11 but was " + bookObject.numKeys());
  }

  /**
   * this test checks if the correct exceptions are being thrown and test possible cases
   * @throws DuplicateKeyException 
   * @throws IllegalNullKeyException 
   */
  @Test
  public void test004_check_for_exceptions_being_thrown() {
    for (int i = 0; i < 5; i++) { // add 5 elements
      try {
        bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
      } catch (IllegalNullKeyException | DuplicateKeyException e) {
        fail("unnecessary exception thrown");
      }
    }

    for (int i = 0; i < 1; i++) { // add a duplicate book
      try {
        bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
        fail("exception should've been thrown"); // check if exception is thrown
      } catch (IllegalNullKeyException | DuplicateKeyException e) {
      }
    }
    if (bookObject.numKeys() != 5) // check keys and make sure nothing is added
      fail("numKeys was supposed to be 5 but was " + bookObject.numKeys());

    try {
      bookObject.insert(null, null); // check null exception
      fail("null exception should've been thrown");
    } catch (IllegalNullKeyException | DuplicateKeyException e) {
    }
  }

  /**
   * this test checks if the correct book is returned when passed in a key
   * @throws DuplicateKeyException 
   * @throws IllegalNullKeyException 
   */
  @Test
  public void test005_correct_book_returned() {
    try {
      for (int i = 0; i < 5; i++) { // add 5 books
        bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
      }
    } catch (Exception e) {
      fail("unnecessary exception thrown");
    }
    Book check = null;
    try {
      check = bookObject.get(bookTable.get(4).getKey()); // get the last book added
    } catch (IllegalNullKeyException | KeyNotFoundException e) {
      fail("unnecessary exception thrown");
    }
    if (bookTable.get(4) != check) { // compare to see if correct value returned
      fail("wrong book value was returned");
    }
  }

  /**
   * checks if capacity of hashtable is correctly calculated and make sure it doesn't 
   * resize when not needed
   * @throws IllegalNullKeyException
   * @throws DuplicateKeyException
   */
  @Test
  public void test006_check_capacity_when_resizing()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert("s", null); // insert one element
    if (bookObject.getCapacity() != 2) {
      fail("capacity should've been 2 but was " + bookObject.getCapacity());
    }

    bookObject.insert("h", null); // resizing has to be done
    if (bookObject.getCapacity() != 5) {
      fail("capacity should've been 5 but was " + bookObject.getCapacity());
    }

    bookObject.insert("a", null); // no resizing
    if (bookObject.getCapacity() != 5) {
      fail("capacity should've been 5 but was " + bookObject.getCapacity());
    }
    bookObject.insert("u", null); // resize
    if (bookObject.getCapacity() != 11) {
      fail("capacity should've been 11 but was " + bookObject.getCapacity());
    }
  }

  /**
   * check that load factor isn't being manipulated and staying the constant even when resizing
   * @throws IllegalNullKeyException
   * @throws DuplicateKeyException
   */
  @Test
  public void test007_check_loadFactor_doesntChange()
      throws IllegalNullKeyException, DuplicateKeyException {
    bookObject.insert("s", null); // add one element
    if (bookObject.getLoadFactorThreshold() != 0.49) {
      fail("load factor threshold should've been 0.49 but was "
          + bookObject.getLoadFactorThreshold());
    }

    bookObject.insert("h", null); // resizing
    if (bookObject.getLoadFactorThreshold() != 0.49) {
      fail("load factor threshold should've been 0.49 but was "
          + bookObject.getLoadFactorThreshold());
    }

    bookObject.insert("a", null);
    bookObject.insert("u", null); // resizing
    if (bookObject.getLoadFactorThreshold() != 0.49) {
      fail("load factor threshold should've been 0.49 but was "
          + bookObject.getLoadFactorThreshold());
    }
  }

  /**
   * check that size of hashtable doesn't reduce when removing existing books from table
   * @throws IllegalNullKeyException
   * @throws DuplicateKeyException
   */
  @Test
  public void test008_checkCapacityDoesntReduce()
      throws IllegalNullKeyException, DuplicateKeyException {
    for (int i = 0; i < 10; i++) { // add nine books
      bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
    }

    for (int i = 0; i < 10; i++) { // remove all the nine books
      bookObject.remove(bookTable.get(i).getKey());
    }
    if (bookObject.getCapacity() != 23) { // capacity shouldn't be reduced
      fail(
          "capacity is supposed to be 23 since resized 4 times but is " + bookObject.getCapacity());
    }
  }


}
