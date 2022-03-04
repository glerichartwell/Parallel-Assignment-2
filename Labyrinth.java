/**
 * Labyrinth
 * 
 * Glenn Hartwell
 * COP4520
 * 
 * I try to make my code as self documenting as possible.
 * In the case of this assignment I named variables and locks
 * based on the narrative in the homework. I hope you enjoy.
 */

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Labyrinth
{

    private boolean cupcake;
    private boolean partyOver;
    private int numEaten;
    private int numGuests;

    public ReentrantLock locky = new ReentrantLock();

    Labyrinth(int numGuests)
    {
        this.cupcake = true;
        this.partyOver = false;
        this.numEaten = 0;
        this.numGuests = numGuests;
    }

    public boolean isPartyOver() 
    {
        return partyOver;
    }

    public void endParty() 
    {
        this.partyOver = true;    
    }

    public int getNumEaten() 
    {
        return numEaten;
    }

    public void setNumEaten(int numEaten) 
    {
        this.numEaten = numEaten;
    }

    public boolean hasCupcake() 
    {
        return cupcake;
    }

    public void replaceCupcake() 
    {
        this.cupcake = true;
    }

    public void eatCupcake()
    {
        this.cupcake = false;
    }

    public int getNumGuests() 
    {
        return numGuests;
    }


    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        int numGuests = 0;
        System.out.print("Enter the number of guests at the Minotaur's party: ");
        try
        {
            numGuests = scan.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Just enter a number next time...");
            System.exit(1);
        }
        
        long start = System.currentTimeMillis();
        try
        {
            // subtract one because the Counter does not need to count intself
            Labyrinth minotaurLabyrinth = new Labyrinth(numGuests - 1);

            // Important Singleton Thread
            Counter counter = new Counter(minotaurLabyrinth);
            Thread cThread = new Thread(counter);
            cThread.start();

            for (int i = 0; i < numGuests - 1; i++)
            {
                Guest guest = new Guest(minotaurLabyrinth);
                Thread thread = new Thread(guest);
                thread.start();
            }
            // Hang main on counter to make sure we can track execution time
            cThread.join();
            scan.close();
        }
        catch (Exception e)
        {
            System.out.println("You must have done something wrong because it sure wasn't me!");
            System.out.println("Just kidding it probably just means a thread couldn't start for some reason");
            System.exit(1);
        }
        
        long end = System.currentTimeMillis();
        System.out.println("It took the guests " + (end - start) + " ms to figure it out!");
    }
}

class Guest implements Runnable
{

    private Labyrinth labyrinth;
    private boolean fullOfCupcake;

    Guest(Labyrinth labyrinth)
    {
        this.labyrinth = labyrinth;
        this.fullOfCupcake = false;
    }

    @Override
    public void run() 
    {   
        while (!labyrinth.isPartyOver())
        {
            tryLabyrinth();
        }
    }

    public void tryLabyrinth()
    {
        labyrinth.locky.lock();
        try
        {
            // Make sure no one is being greedy with the cupcakes
            // and make sure that there is even one to eat
            if (!fullOfCupcake && labyrinth.hasCupcake())
            {
                System.out.println("A guest has eaten a cupcake.");
                eatCupcake();
            }
            else
            {
                // System.out.println("There was no cupcake to eat.");
            }
        }
        finally
        {
            labyrinth.locky.unlock();
        }
    }

    // yummy
    public void eatCupcake() 
    {
        this.fullOfCupcake = true;
        labyrinth.eatCupcake();
    }

}

// This Guest keeps track of how many unique people have entered the maze
class Counter implements Runnable
{

    private Labyrinth labyrinth;


    Counter(Labyrinth labyrinth)
    {
        this.labyrinth = labyrinth;
    }

    @Override
    public void run() 
    {   
        while (!labyrinth.isPartyOver())
        {
            tryLabyrinth();
        }
    }

    public void tryLabyrinth()
    {
        labyrinth.locky.lock();
        try
        {
            if (!labyrinth.hasCupcake())
            {
                labyrinth.setNumEaten(labyrinth.getNumEaten() + 1);
                System.out.println("Count: " + labyrinth.getNumEaten());

                // We win!
                if (labyrinth.getNumEaten() == labyrinth.getNumGuests())
                {
                    System.out.println("All of the guests have entered the labyrinth!");
                    labyrinth.endParty();
                }

                System.out.println("The counter replaced the cupcake.");
                labyrinth.replaceCupcake();
            }

        }
        finally
        {
            labyrinth.locky.unlock();
        }

    }
}