//
// EVENTLIST.JAVA
// Skeleton code for your EventList collection type.
//
import java.util.*;

class EventList {

	Event head = new Event((int)Double.NEGATIVE_INFINITY, "head");
	Event tail = new Event((int)Double.POSITIVE_INFINITY, "tail");

	Random randseq;
	int maxHeight = 1;
	int highestUsed = 1;

	////////////////////////////////////////////////////////////////////
	// Here's a suitable geometric random number generator for choosing
	// pillar heights.  We use Java's ability to generate random booleans
	// to simulate coin flips.
	////////////////////////////////////////////////////////////////////

	int randomHeight()
	{
		int v = 1;
		while (randseq.nextBoolean()) { v++; }
		return v;
	}

	//
	// Constructor
	//
	public EventList()
	{
		randseq = new Random(9989); // You may seed the PRNG however you like.

		head.pillar = new Event[maxHeight];
		tail.pillar = new Event[maxHeight];

		for (int i = 0; i < head.pillar.length; i++) {
			head.pillar[i] = tail;
		}

		for (int j = 0; j < tail.pillar.length; j++) {
			tail.pillar[j] = null;
		} 
	}

	//
	// Add an Event to the list.
	//
	public void insert(Event e)
	{
		e.pillarHeight = randomHeight();
		if (e.pillarHeight > highestUsed) {
			highestUsed = e.pillarHeight -1;
		}

		if (e.pillarHeight > maxHeight) {	//dynamically resizing head and tail pillars
			while (e.pillarHeight > maxHeight) {
				maxHeight *= 2;
			}
			Event [] temp = head.pillar;
			head.pillar = new Event[maxHeight];
			for (int i = 0; i < temp.length; i++) {
				head.pillar[i] = temp[i];
			}
			for (int j = temp.length; j < head.pillar.length; j++) {
				head.pillar[j] = tail;
			}
			tail.pillar = new Event[maxHeight];
			for (int i = 0; i < tail.pillar.length; i++) {
				tail.pillar[i] = null;
			}
		}

		e.pillar = new Event[e.pillarHeight];

		Event prev = head; 
		Event next = tail;
		int currentHeight = highestUsed;

		while (currentHeight >= 0) {
			while (prev.pillar[currentHeight].year < e.year) {
				prev = prev.pillar[currentHeight];
			}
			next = prev.pillar[currentHeight];
			if (currentHeight < e.pillarHeight) {
				e.pillar[currentHeight] = next;	//set the next pointer at this pillar height to J
				prev.pillar[currentHeight] = e;

			}
			currentHeight--;
		}
	}

	//
	// Remove all Events in the list with the specified year.
	//
	public void remove(int year)
	{

		Event prev = head;

		int currentHeight = highestUsed;

		while(currentHeight >= 0) {
			while (prev.pillar[currentHeight].year < year) {
				prev = prev.pillar[currentHeight];
			}
			Event end = prev.pillar[currentHeight];
			while (end.year <= year) {	//this takes time m, for the number of nodes m with year to be deleted
				end = end.pillar[currentHeight];
			}
			prev.pillar[currentHeight] = end;	//cutting out the middle; java's garbage disposal will delete
			currentHeight --;
		}	
	}

	//
	// Find all events with greatest year <= input year
	//
	public Event [] findMostRecent(int year)
	{
		Event [] result;

		Event prev = head;
		Event beginning = head;

		int currentHeight = highestUsed;

		while (currentHeight >= 0) {
			prev = beginning;	//make sure to go down on the pillar before the greatest year identified so far
			while (prev.pillar[currentHeight].year <= year) {
				if (prev.pillar[currentHeight].year > beginning.year) {
					beginning = prev;	
				}
				prev = prev.pillar[currentHeight];	//cycle through all pillars
			}
			currentHeight --;
		}

		if ((beginning.pillar[0].year > beginning.year) && (beginning.pillar[0].year <= year)) {
			beginning = beginning.pillar[0];
		}

		int count = 0;
		Event start = beginning;
		int yearVal = start.year;

		while (beginning.year == yearVal) {
			count++;
			beginning = beginning.pillar[0];
		}

		if (count == 0) {
			return null;
		} else {
			result = new Event[count];
			for (int i = 0; i < count; i++) {
				result[i] = start;
				start = start.pillar[0];
			}
			return result;
		}
	}

	//
	// Find all Events within the specific range of years (inclusive).
	//
	public Event [] findRange(int first, int last)
	{
		Event [] result; 

		Event prev = head;
		Event next = tail;

		int currentHeight = highestUsed;

		while (currentHeight >= 0) {
			while (prev.pillar[currentHeight].year < first) {
				prev = prev.pillar[currentHeight];	
			}
			next = prev.pillar[currentHeight];
			currentHeight --;
		}

		Event beginning = next;

		Event end = next;
		int count = 0;	//used to allot array space

		while (end.year <= last) {	
			count++;
			end = end.pillar[0];
		}

		if (count == 0) {
			return null;
		} else {
			result = new Event[count];
			for (int i = 0; i < count; i++) {
				result[i] = beginning;
				beginning = beginning.pillar[0];
			}
			return result;
		}
	}
}
