
/**
 * CrystalVase
 * 
 * Glenn Hartwell
 * COP4520
 * 
 * I try to make my code as self documenting as possible.
 * In the case of this assignment I named variables and locks
 * based on the narrative in the homework. I hope you enjoy.
 */

 
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CrystalVase {

    final boolean AVAILABLE = true;
    final boolean BUSY = false;
    final boolean ACTIVE = false;
    final boolean ENDED = true;

    // Custom TTAS Lock to simulate the Sign on the door
    Sign sign = new Sign();
    
    private boolean partyStatus;
    private int numGuestsViewed;
    private int numGuests;

    CrystalVase(int numGuests)
    {
        this.partyStatus = ACTIVE;
        this.numGuestsViewed = 0;
        this.numGuests = numGuests;
    }

    public int getNumGuests()
    {
        return numGuests;
    }

    public int getNumGuestsViewed() {
        return numGuestsViewed;
    }

    public void incNumGuestsViewed()
    {
        this.numGuestsViewed++;    
    }

    public boolean partyEnded()
    {
        return this.partyStatus == ENDED;   
    }

    public void endParty()
    {
        this.partyStatus = ENDED; 
        System.exit(0);  
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
        
        try
        {   
            System.out.println("The Minotaur's party is starting and will end if all the guests view the vase or if the party has lasted longer than 1 minute!");
            CrystalVase crystalVase = new CrystalVase(numGuests);
            
            // imagine if you had for some reason put a while (true) loop around this for loop and couldn't figure out why
            // your threads were counting past the number of guests :sweat_smile:
            for (int i = 0; i < numGuests; i++)
            {
                Guest guest = new Guest(crystalVase);
                Thread thread = new Thread(guest);
                thread.start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        scan.close();
    }
    
}

class Sign implements Lock
{
    AtomicBoolean state = new AtomicBoolean(false);

    public void busy()
    {
        lock();    
    }
    
    public void available()
    {
        unlock();
    }

    @Override
    public void lock()
    {
        while (true)
        {
            while (state.get()) 
            {
                // Walk around the castle
            }
            if (!state.getAndSet(true))
                return;
        }
    }

    @Override
    public void unlock()
    {
        state.set(false);
    }


    // These functions below are here to satisfy Lock reqs we only need the first two in our case.
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}

class Guest implements Runnable
{
    final boolean AVAILABLE = true;
    final boolean BUSY = false;

    private CrystalVase crystalVase;
    private boolean seenVase;

    Guest(CrystalVase crystalVase)
    {
        this.crystalVase = crystalVase;
        this.seenVase = false;
    }

    @Override
    public void run() 
    {
        long start = System.currentTimeMillis();
        while (!crystalVase.partyEnded())
        {
            if (crystalVase.getNumGuestsViewed() == crystalVase.getNumGuests())
            {
                System.out.println("All of the guests got to see the crystal vase before the party ended!");
                crystalVase.endParty();
            }
            else if (System.currentTimeMillis() - start > (long) 60000)
            {
                System.out.println("There were " + crystalVase.getNumGuestsViewed() + " guests that got to see the crystal vase before the party ended.");
                crystalVase.endParty();
            }
            stare();
        }
    }

    public void stare()
    {
        crystalVase.sign.busy();
        try
        {
            if (!seenVase)
            {
                crystalVase.incNumGuestsViewed();
                this.seenVase = true;
                System.out.println(crystalVase.getNumGuestsViewed() + " unique guests have viewed the vase so far.");
            }
            
            // Oh no the guest fell asleep while looking at the Minotaur's vase!
            // Feel free to change the time here to make the Guests to nap with the vase for more or less time 

            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch (Exception e){}
        finally 
        {
            crystalVase.sign.available();
        }
    }
}