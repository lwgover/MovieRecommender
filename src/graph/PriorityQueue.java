package graph;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A priority queue class implemented using a min heap.
 * Priorities cannot be negative.
 *
 * @author Lucas Gover
 * @version 9/25/2021
 *
 */
public class PriorityQueue {

    protected Map<Integer, Integer> location;
    protected List<Pair<Integer, Integer>> heap;

    /**
     *  Constructs an empty priority queue
     */
    public PriorityQueue() {
        heap = new ArrayList<Pair<Integer, Integer>>();
        location = new HashMap<Integer, Integer>();
    }

    /**
     *  Insert a new element into the queue with the
     *  given priority.
     *
     *	@param priority priority of element to be inserted
     *	@param element element to be inserted
     *	<br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element does not already appear in the priority queue.</li>
     *	<li> The priority is non-negative.</li>
     *	</ul>
     *
     */
    public void push(int priority, int element) {
        assert !isPresent(element);
        assert priority >= 0;
        heap.add(new Pair<Integer,Integer>(priority,element));
        int finalLocation = percolateUp(heap.size()-1);
        location.put(element,finalLocation);
    }

    /**
     *  Remove the highest priority element
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     *
     */
    public Integer pop(){
        assert !isEmpty() : "The priority queue is empty";
        swap(0,size()-1);
        location.remove(heap.get(size()-1).element);
        Integer removed = heap.remove(heap.size()-1).element;
        pushDown(0);
        return removed;
    }


    /**
     *  Returns the highest priority in the queue
     *  @return highest priority value
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */
    public int topPriority() {
        assert !isEmpty() : "Priority queue is empty";
        Pair<Integer, Integer> temp = heap.get(0);
        return temp.priority;
    }


    /**
     *  Returns the element with the highest priority
     *  @return element with highest priority
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */
    public int topElement() {
        assert !isEmpty() : "Priority queue is empty";
        Pair<Integer, Integer> temp = heap.get(0);
        return temp.element;
    }


    /**
     *  Change the priority of an element already in the
     *  priority queue.
     *
     *  @param newpriority the new priority
     *  @param element element whose priority is to be changed
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	<li> The new priority is non-negative </li>
     *	</ul>
     */
    public void changePriority(int newpriority, int element) {
        assert isPresent(element) : "Element not Present";
        assert newpriority >=0 : "Priority is negative";
        int curr = location.get(element);
        if(heap.get(curr).priority > newpriority){
            heap.get(curr).priority = newpriority;
            percolateUp(curr);
        }else {
            heap.get(curr).priority = newpriority;
            pushDown(curr);
        }
    }


    /**
     *  Gets the priority of the element
     *
     *  @param element the element whose priority is returned
     *  @return the priority value
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	</ul>
     */
    public int getPriority(int element) {
        assert location.containsKey(element);
        return heap.get(location.get(element)).priority;
    }

    /**
     *  Returns true if the priority queue contains no elements
     *  @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return heap.size() <= 0;
    }

    /**
     *  Returns true if the element exists in the priority queue.
     *  @return true if the element exists, false otherwise
     */
    public boolean isPresent(int element) {
        return location.containsKey(element);
    }

    /**
     *  Removes all elements from the priority queue
     */
    public void clear() {
        heap.clear();
        location.clear();
    }

    /**
     *  Returns the number of elements in the priority queue
     *  @return number of elements in the priority queue
     */
    public int size() {
        return heap.size();
    }



    /*********************************************************
     * 				Private helper methods
     *********************************************************/


    /**
     * Push down the element at the given position in the heap
     * @param start_index the index of the element to be pushed down
     * @return the index in the list where the element is finally stored
     */
    private int pushDown(int start_index) {
        int left = left(start_index);
        int right = right(start_index);
        if (left >= heap.size()) {
            return start_index;
        }
        if (right >= heap.size()) {
            if (heap.get(start_index).priority > heap.get(left).priority) {
                swap(start_index, left);
                return left;
            }
            return left;
        }
        if (heap.get(left).priority > heap.get(right).priority) {
            swap(start_index, right);
            return pushDown(right);
        } else {
            swap(start_index, left);
            return pushDown(left);
        }
    }

    /**
     * Percolate up the element at the given position in the heap
     * @param start_index the index of the element to be percolated up
     * @return the index in the list where the element is finally stored
     */
    private int percolateUp(int start_index) {
        int p = parent(start_index);
        while(heap.get(start_index).priority < heap.get(p).priority) { //While violates the minHeap property
            swap(start_index,p);
            start_index = p;
            p = parent(start_index);
        }
        return start_index;
    }


    /**
     * Swaps two elements in the priority queue by updating BOTH
     * the list representing the heap AND the map
     * @param i The index of the element to be swapped
     * @param j The index of the element to be swapped
     */
    private void swap(int i, int j) {
        //update heap
        location.remove(heap.get(i).element);
        location.remove(heap.get(j).element);
        location.put(heap.get(j).element,i);
        location.put(heap.get(i).element,j);

        Pair<Integer,Integer> temp = heap.get(i);
        heap.set(i,heap.get(j));
        heap.set(j,temp);
        //update hashmap
    }

    /**
     * Computes the index of the element's left child
     * @param parent index of element in list
     * @return index of element's left child in list
     */
    private int left(int parent) {
        return (2 * parent) + 1;
    }

    /**
     * Computes the index of the element's right child
     * @param parent index of element in list
     * @return index of element's right child in list
     */
    private int right(int parent) {
        return (2 * parent) + 2;
    }

    /**
     * Computes the index of the element's parent
     * @param child index of element in list
     * @return index of element's parent in list
     */

    private int parent(int child) {
        return (child - 1) / 2;
    }


    /*********************************************************
     * 	These are optional private methods that may be useful
     *********************************************************/


    /**
     * Returns true if element is a leaf in the heap
     * @param i index of element in heap
     * @return true if element is a leaf
     */
    private boolean isLeaf(int i){
        return left(i) >= heap.size();
    }

    /**
     * Returns true if element has two children in the heap
     * @param i index of element in the heap
     * @return true if element in heap has two children
     */
    private boolean hasTwoChildren(int i) {
        return right(i) < heap.size();
    }

    /**
     * Print the underlying list representation
     */
    public void printHeap() {
        printMapRecursive(0,0);
        System.out.print("[");
        for(int i = 0; i < heap.size(); i++){
            System.out.print("(p:" + heap.get(i).priority + ",e:" + heap.get(i).element + "),");
        }
        System.out.println("] ");
    }

    /**
     * Print the entries in the location map
     */
    private void printMap() {System.out.println(location.entrySet());}
    private void printMapRecursive(int depth, int index){
        if(isLeaf(index)){
            for(int i = 0; i < depth; i++){
                System.out.print("\t");
            }
            Pair<Integer, Integer> temp = heap.get(index);
            System.out.println(temp.element);
            return;
        }
        if(hasTwoChildren(index)){
            printMapRecursive(depth+1,left(index));
            for(int i = 0; i < depth; i++){
                System.out.print("\t");
            }
            Pair<Integer, Integer> temp = heap.get(index);
            System.out.println(temp.element);
            printMapRecursive(depth+1,right(index));
            return;
        }
        printMapRecursive(depth+1,left(index));
        for(int i = 0; i < depth; i++){
            System.out.print("\t");
        }
        Pair<Integer, Integer> temp = heap.get(index);
        System.out.println(temp.element);
    }


}

