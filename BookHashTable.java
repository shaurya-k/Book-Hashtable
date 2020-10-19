
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
import java.util.ArrayList;
//
//
// used arraylist with arraylist
// added elements to buckey arraylist
//
// if open addressing: describe probe sequence
// if buckets: describe data structure for each bucket
//
// hashed string by converting it to an int by using hashCode() and then taking modulo of capacity
// NOTE: you are not required to design your own algorithm for hashing,
// since you do not know the type for K,
// you must use the hashCode provided by the <K key> object

/**
 * HashTable implementation that uses:
 * 
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {

  private static int numKeys;
  private static boolean DEBUG = false;
  public static final String COLLISION_RESOLUTION = "ArrayList of LinkedLists";

  /** The initial capacity that is used if none is specifed user */
  static final int DEFAULT_CAPACITY = 101;

  /** The load factor that is used if none is specified by user */
  static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

  private ArrayList<ArrayList<MyObject>> hashtable;
  private double loadFactorThreshold;
  private int capacity;

  /**
   * inner class which stores the key and value into one object
   */
  private class MyObject {
    String key;
    Book value;

    MyObject(String key, Book value) {
      this.key = key; // assign
      this.value = value; // assign
    }

    public String getKey() {
      return key;
    }

    public Book getValue() {
      return value;
    }
  }

  /**
   * REQUIRED default no-arg constructor
   * Uses default capacity and sets load factor threshold 
   * for the newly created hash table.
   */
  public BookHashTable() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
  }


  /**
   * Creates an empty hash table with the specified capacity 
   * and load factor.
   * @param initialCapacity number of elements table should hold at start.
   * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
   */
  public BookHashTable(int initialCapacity, double loadFactorThreshold) {
    if (initialCapacity >= 1) // this number has to be greater than or equal to 1
      this.capacity = initialCapacity;
    else
      this.capacity = DEFAULT_CAPACITY; // base case assign to default
    hashtable = new ArrayList<ArrayList<MyObject>>(this.capacity); // create a new arraylist of
                                                                   // specified size

    for (int i = 0; i < this.capacity; i++)
      hashtable.add(i, new ArrayList<MyObject>()); // add a new arraylist to each element
    numKeys = 0; // initialize
    if (loadFactorThreshold <= 0) // if user inputs negative number, assign to default
      this.loadFactorThreshold = DEFAULT_LOAD_FACTOR_THRESHOLD;
    else
      this.loadFactorThreshold = loadFactorThreshold;

  }

  // Add the key,value pair to the data structure and increase the number of keys.
  // If key is null, throw IllegalNullKeyException;
  // If key is already in data structure, throw DuplicateKeyException();
  /** @throws IllegalNullKeyException 
  * @throws DuplicateKeyException 
  */
  @Override
  public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    Book duplicate = null;
    try {
      duplicate = get(key);
    } catch (KeyNotFoundException e) {
    }
    if (duplicate != null)
      throw new DuplicateKeyException();
    if (this.loadFactorThreshold <= (double) numKeys / (double) this.capacity)
      resizeTable(); // if past the loadfactor threshold

    MyObject toAdd = new MyObject(key, value); // creates object to add
    int index = hashIndex(key);

    ArrayList<MyObject> addList = hashtable.get(index); // sets temp variable to arraylist at index
    addList.add(toAdd); // add the object to the arraylist
    numKeys++; // increment
  }

  // If key is found,
  // remove the key,value pair from the data structure
  // decrease number of keys.
  // return true
  // If key is null, throw IllegalNullKeyException
  // If key is not found, return false
  /** @throws IllegalNullKeyException 
   */
  @Override
  public boolean remove(String key) throws IllegalNullKeyException {
    if (key == null) { // null check
      throw new IllegalNullKeyException();
    }
    try {
      get(key);
    } catch (Exception e) {
      return false;
    }
    int index = hashIndex(key); // assign index
    ArrayList<MyObject> removeList = hashtable.get(index); // get the arraylist from index
    if (removeList == null) // null check
      return false;
    for (int i = 0; i < removeList.size(); i++) {
      if (removeList.get(i).getKey().equals(key)) { // comparison
        removeList.remove(i);
        numKeys--; // decrement
        return true;
      }
    }
    return false;
  }

  // Returns the value associated with the specified key
  // Does not remove key or decrease number of keys
  //
  // If key is null, throw IllegalNullKeyException
  // If key is not found, throw KeyNotFoundException()
  /** @throws IllegalNullKeyException 
   * @throws DuplicateKeyException 
   */
  @Override
  public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
    if (key == null) // null check
      throw new IllegalNullKeyException();
    if (numKeys == 0) // empty hashtable check
      throw new KeyNotFoundException();
    int index = hashIndex(key); // assign int to the index
    ArrayList<MyObject> pointer = hashtable.get(index); // get the arraylist in the bucket
    if (pointer == null)
      throw new KeyNotFoundException();
    for (int i = 0; i < pointer.size(); i++) { // loop through the arraylist
      if (pointer.get(i).getKey().equals(key)) // comparison check from book key to method key
        return pointer.get(i).getValue();
    }

    throw new KeyNotFoundException(); // if no match is found

  }

  // Returns the number of key,value pairs in the data structure
  @Override
  public int numKeys() {
    return numKeys;
  }

  // Returns the load factor for this hash table
  // that determines when to increase the capacity
  // of this hash table
  @Override
  public double getLoadFactorThreshold() {
    return this.loadFactorThreshold;
  }

  // Capacity is the size of the hash table array
  // This method returns the current capacity.
  //
  // The initial capacity must be a positive integer, 1 or greater
  // and is specified in the constructor.
  //
  // REQUIRED: When the load factor is reached,
  // the capacity must increase to: 2 * capacity + 1
  //
  // Once increased, the capacity never decreases
  @Override
  public int getCapacity() {
    return this.capacity;
  }

  // Returns the collision resolution scheme used for this hash table.
  // Implement this ADT with one of the following collision resolution strategies
  // and implement this method to return an integer to indicate which strategy.
  //
  // 1 OPEN ADDRESSING: linear probe
  // 2 OPEN ADDRESSING: quadratic probe
  // 3 OPEN ADDRESSING: double hashing
  // 4 CHAINED BUCKET: array list of array lists
  // 5 CHAINED BUCKET: array list of linked lists
  // 6 CHAINED BUCKET: array list of binary search trees
  // 7 CHAINED BUCKET: linked list of array lists
  // 8 CHAINED BUCKET: linked list of linked lists
  // 9 CHAINED BUCKET: linked list of of binary search trees
  @Override
  public int getCollisionResolutionScheme() {
    return 4;
  }

  // helper method takes in a non null string and converts it into a number which can be used
  // as an index
  private int hashIndex(String key) {
    int hashCode = Math.abs(key.hashCode()); // java hashcode of the string
    int index = hashCode % (this.capacity); // hashTable.size();
    return index;
  }

  // helper method which resizes the current table and inserts all the books which were previously
  // in the hashtable, not in the same spot, but correctly with the insert function
  private void resizeTable() {
    int doubleSize = 1 + (this.capacity * 2); // times two plus one
    int oldSize = this.capacity; // temp variable
    this.capacity = doubleSize;
    ArrayList<ArrayList<MyObject>> temporary = hashtable;
    hashtable = new ArrayList<ArrayList<MyObject>>(doubleSize); // create new "hashtable"
    numKeys = 0; // reset

    for (int i = 0; i < this.capacity; i++) // iterate
      hashtable.add(i, new ArrayList<MyObject>()); // initialize elements
    for (int i = 0; i < oldSize; i++) { // iterate
      ArrayList<MyObject> books = temporary.get(i);
      if (!books.isEmpty()) {
        for (int j = 0; j < books.size(); j++) {
          try {
            insert(books.get(j).getKey(), books.get(j).getValue());
          } catch (Exception e) {
          }
        }
      }
    }
  }
}
