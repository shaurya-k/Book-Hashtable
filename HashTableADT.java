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
public interface HashTableADT<K extends Comparable<K>, V> extends DataStructureADT<K,V> {

    // Notice:
    // THIS INTERFACE EXTENDS AND INHERITS ALL METHODS FROM DataStructureADT
    // and adds the following operations:

    // Returns the load factor for this hash table
    // that determines when to increase the capacity 
    // of this hash table
    public double getLoadFactorThreshold() ;

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
     public int getCapacity() ;
    

     // Returns the collision resolution scheme used for this hash table.
     // Implement this ADT with one of the following collision resolution strategies
     // and implement this method to return an integer to indicate which strategy.
     //
     // 4 CHAINED BUCKET: array list of array lists
     public int getCollisionResolutionScheme() ;

}
