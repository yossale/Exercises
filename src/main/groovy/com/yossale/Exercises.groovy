package com.yossale

import groovyx.gpars.GParsExecutorsPool
import junit.framework.Assert
import org.apache.commons.io.IOUtils

/**
 * Created with IntelliJ IDEA.
 * User: yossale
 * Date: 7/12/13
 * Time: 11:38 AM
 */

/* Question 1:
Write a method that given an integer N, counts from 1 to N printing out one line for each number:
for numbers that are multiples of 3 it prints "Fizz",
for numbers that are multiples of 5 it prints "Buzz",
for numbers that are multiples of both 3 and 5 it prints "FizzBuzz", and for all other numbers it prints the number itself.
 */

def question1(int n) {

    println("This is a classical tribute to Joel's `how to interview` blog post")

    if (n < 1) {
        println("$n is smaller than 1, aborting")
    }

    println ("Running with n = $n")

    (1..n).each { i ->

        def str = ""

        if (i % 3 == 0) {
            str += "Fizz"
        }

        if (i % 5 == 0) {
            str += "Buzz"
        }

        println(str.length() == 0 ? i : str)
    }
}

println("Question 1")
question1(16)
println("----------------------------------------------------------------------------------------------------------")

/*
Question 3
Write a program that retrieves the HTML content of the Google homepage at http://www.google.com and writes it to a file (without images, css, etc. - just the HTML). The implementation should use Sockets directly, and must not use any existing HTTP related classes (neither JDK nor 3rd party)

 */

//This doesn't work, because google is redirecting the request - which we can't follow, because
// that's the whole point of using http and not just sockets...

def String question3(String url) {

    Socket socket = new Socket(url, 80);
    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
    outputStream.write("GET /search HTTP/1.0\n\n".getBytes());
    return IOUtils.toString(inputStream, "utf-8")
}

println("Question 3")
println question3("www.google.com")
println("----------------------------------------------------------------------------------------------------------")

/*
Question 4:

Write a method that given an integer N, computes the sum of 1+2+…+N. Write a unit test for the method.

Only works for n > 0
 */


def question4(int n) {
    return (n == 1) ? 1 : (n + question4(n - 1))
}

println("Question 4")

println("running for n = 5 : " + question4(5));
println("----------------------------------------------------------------------------------------------------------")

/*
Question 5:

Implement a thread-safe counter, supporting the operations: initialize to specified value, increase by 1, decrease by 1, and get current value.

 */


//We could just use AtomicInteger...
public class ThreadSafeCounter {

    private Integer counter;

    public ThreadSafeCounter() {
        counter = 0;
    }

    private int update(int update) {

        /*Even though we could synchronize on the method itself, I
        Think it's usually a good idea to be very specific about what you're synchronizing */
        synchronized (this) {
            counter += update;
            return counter
        }
    }

    public int inc() {
        return update(1)
    }

    public int dec() {
        return update(-1)
    }

    public int get() {
        return update(0)
    }
}



println("Question 5")

println("Trying to test Thread Safety")

boolean failed = false

def countTo = 100000

def counter = new ThreadSafeCounter()
try {
    GParsExecutorsPool.withPool(10) {
        (1..countTo).eachParallel {
            counter.inc()
        }
    }
} catch (e) {
    println ("Error while running ThreadSafeCounter with multi threads ")
    failed = true

} finally {
    println (failed ? "Damn, we failed... " : "Yeah! No concurrency issues!")
}

println("----------------------------------------------------------------------------------------------------------")

Assert.assertEquals("Should have counted to " + countTo, countTo, counter.get())




/* Question 6

Write a method which given two arrays, returns their intersection (an array containing the items that exist in both of the original arrays).

 */

def List<Integer> question6(List<Integer> arrayA, List<Integer> arrayB) {
    def searchHash = arrayA.collectEntries { i -> [i, i] }
    def res = arrayB.findAll { b -> searchHash.containsKey(b) }
    return res
}


println("Question 6")

def arrayA = (1..10).findAll { it % 2 == 0 }
def arrayB = (1..10).findAll { it % 5 == 0 }
Collections.shuffle(arrayA)
Collections.shuffle(arrayB)

println("First array: $arrayA")
println("Second array: $arrayB")

def intersect = question6(arrayA, arrayB)

println ("Intesection : $intersect")

if (null == intersect.find { (it % 2 != 0) || (it % 5 != 0) }) {
    println ("Yeah! works as planned!")
} else {
    println ("Something is wrong :( ")
}

println("----------------------------------------------------------------------------------------------------------")



